package de.maxiindiestyle.api.ui.xml;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.maxiindiestyle.api.ui.elementhandlers.*;
import de.maxiindiestyle.api.ui.xml.elementhandlers.XmlLabelElementHandler;
import de.maxiindiestyle.api.ui.xml.elementhandlers.XmlScrollPaneElementHandler;
import de.maxiindiestyle.api.xml.XmlArguments;
import de.maxiindiestyle.api.xml.XmlUtils;
import de.maxiindiestyle.api.xml.preprocessor.XmlPreprocessor;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;

// TODO-API JavaDoc and Cleanup, Prints...
@SuppressWarnings("unused")
public class UiXmlLoader {

    private static final ArrayList<String> packages;
    private static final HashMap<String, ElementHandler> elementHandlers;
    private final static HashMap<String, Class<?>> classes;

    private final FileHandle fileHandle;
    private final String xml;
    private XmlStage stage;
    private XmlReader.Element rootElement;
    private final Skin defaultSkin;

    static {
        packages = new ArrayList<>();
        addPackage(""); // full classpath
        addPackage("com.badlogic.gdx.scenes.scene2d.ui.");
        addPackage("de.maxiindiestyle.api.ui.");
        addPackage("de.maxiindiestyle.api.ui.xml.");

        elementHandlers = new HashMap<>();
        putElementHandler(new ActorElementHandler());
        putElementHandler(new ChartElementHandler());
        putElementHandler(new LabelElementHandler());
        putElementHandler(new LabelChartElementHandler());
        putElementHandler(new XmlScrollPaneElementHandler());
        putElementHandler(new TabElementHandler());
        putElementHandler(new TabbedTableElementHandler());
        putElementHandler(new TableElementHandler());
        putElementHandler(new XmlLabelElementHandler());

        classes = new HashMap<>();
    }

    public UiXmlLoader(FileHandle fileHandle, Skin defaultSkin) {
        this.fileHandle = fileHandle;
        this.defaultSkin = defaultSkin;
        this.xml = XmlPreprocessor.preprocessedXml(fileHandle);
    }

    /**
     * Loads an XmlStage with the file {@link #fileHandle} which was set in the constructor.<br>
     * See example.xml file in this package to see the structure of the Xml-file and the attributes supported for the elements.
     *
     * @return loaded {@link de.maxiindiestyle.api.ui.xml.XmlStage}
     */
    public XmlStage loadStage() {
        XmlReader reader = new XmlReader();
        rootElement = xml == null ? reader.parse(fileHandle) : reader.parse(xml);
        if (!rootElement.getName().equals("Stage")) {
            return null;
        }
        stage = new XmlStage(defaultSkin, this);
        applyAttributes(stage, rootElement.getAttributes());
        for (int i = 0; i < rootElement.getChildCount(); i++) {
            Object obj = loadElement(rootElement.getChild(i), stage);
            if (ClassReflection.isInstance(Actor.class, obj))
                stage.addActor((Actor) obj);
        }
        return stage;
    }

    public Object loadElement(XmlReader.Element element, XmlStage stage) {
        try {
            Class<?> clazz = getClassFromPackages(element);
            if (clazz == null) return null;
            Object obj = getInstance(element, clazz);

            stage.putActor(element.getAttribute("name"), obj);
            if (element.getBoolean("reloadable", false)) {
                stage.putReloadableActor(element.getAttribute("name"), obj);
            }

            ElementHandler handler = elementHandlers.get(clazz.getName());
            if (handler != null) {
                handler.load(obj, element, this);
            } else if (ClassReflection.isInstance(Actor.class, obj)) {
                System.out.println("Using actor ElementHandler for " + obj.getClass().getSimpleName());
                new ActorElementHandler().load(obj, element, this);
            } else {
                System.out.println("No ElementHandler for " + clazz.getName());
            }

            handleOnLoad(element);
            return obj;
        } catch (ReflectionException e) {
            System.err.println("No valid Object " + element.getName());
            return null;
        }
    }

    public Object reloadElement(Object obj, String name) {
        XmlReader.Element element = XmlUtils.findElementByAttribute(rootElement, "name", name);
        ElementHandler handler;
        if ((handler = elementHandlers.get(obj.getClass().getName())) != null) {
            if (!handler.reload(obj, element, this)) {
                System.out.println(handler.getClass().getSimpleName() + " doesn't support reloading. Element is loaded normally!");
                handler.load(obj, element, this);
            }
        } else {
            if (ClassReflection.isInstance(Actor.class, obj)) {
                ActorElementHandler elementHandler = new ActorElementHandler();
                if (!elementHandler.reload(obj, element, this)) {
                    System.out.println(elementHandler.getClass().getSimpleName() + " doesn't support reloading. Element is loaded normally!");
                    elementHandler.load(obj, element, this);
                } else
                    System.out.println("No ElementHandler for " + obj.getClass().getName());
            }
        }
        handleOnLoad(element);
        return obj;
    }

    protected void handleOnLoad(XmlReader.Element element) {
        if (element == null) return;
        if (element.get("onLoad", null) == null) return;

        Class<?> clazz = getClassFromPackages(element.get("onLoad"), element.get("name") + ".onLoad");
        if (clazz == null) return;
        try {
            executeOnLoadMethod(clazz, element);
        } catch (ReflectionException e) {
            System.err.println("No onLoad method declared for '" + element.getName() + "' in Class '" + clazz + "'!");
        }
    }

    private void executeOnLoadMethod(Class<?> clazz, XmlReader.Element element) throws ReflectionException {
        ArrayList<OnLoadMethod> onLoadMethods = getOnLoadMethods(clazz, element);
        onLoadMethods.sort((a, b) -> b.annotationName.length() - a.annotationName.length());
        // Pick first method and invoke (the longest suitable name)
        onLoadMethods.get(0).method.invoke(null, this, element);
    }

    private ArrayList<OnLoadMethod> getOnLoadMethods(Class<?> clazz, XmlReader.Element element) {
        ArrayList<OnLoadMethod> onLoadMethods = new ArrayList<>();
        for (Method method : ClassReflection.getDeclaredMethods(clazz)) {
            if (method.isAnnotationPresent(XmlOnLoadMethod.class)) {
                XmlOnLoadMethod annotation = method.getDeclaredAnnotation(XmlOnLoadMethod.class).getAnnotation(XmlOnLoadMethod.class);
                if (isOnLoadMethodSuitable(annotation.name(), annotation, element)) {
                    onLoadMethods.add(new OnLoadMethod(annotation.name(), method));
                }
            }
        }
        return onLoadMethods;
    }

    private boolean isOnLoadMethodSuitable(String annotationName, XmlOnLoadMethod annotation, XmlReader.Element element) {
        if (annotationName.equals(element.get("name"))) return true;
        if (!annotationName.contains("*")) return false;
        if (annotationName.equals("*")) return true;
        // only one '*' allowed
        String name = annotationName.replace("*", "");
        return element.get("name").endsWith(name);
    }

    private Class<?> getClassFromPackages(XmlReader.Element element) {
        return getClassFromPackages(element.getName(), element.get("name"));
    }

    protected Class<?> getClassFromPackages(String parameterizedClazz, String elementName) {
        String clazz = parameterizedClazz.replace("*", "");
        for (String aPackage : packages) {
            try {
                return getClassFromPackages(aPackage + clazz);
            } catch (ReflectionException ignored) {
            }
        }
        if (parameterizedClazz.contains("*"))
            return getClassFromPackages(parameterizedClazz.substring(parameterizedClazz.indexOf("*") + 1), elementName);
        System.err.println("Class not found! Failed element '" + elementName + "'.\n\tPackages:" + packages);
        return null;
    }

    private Class<?> getClassFromPackages(String clazzName) throws ReflectionException {
        Class<?> clazz = classes.get(clazzName);
        if (clazz == null) {
            clazz = ClassReflection.forName(clazzName);
            classes.put(clazzName, clazz);
        }
        return clazz;
    }

    private Object getInstance(XmlReader.Element element, Class<?> clazz) throws ReflectionException {
        Object obj;
        try {
            if (element.getAttributes().get("styleName") == null) {
                if (element.getAttributes().get("args") != null) {
                    Constructor constructor = ClassReflection.getConstructor(clazz, Skin.class, XmlArguments.class);
                    obj = constructor.newInstance(defaultSkin, new XmlArguments(element.get("args")));
                } else {
                    Constructor constructor = ClassReflection.getConstructor(clazz, Skin.class);
                    obj = constructor.newInstance(defaultSkin);
                }
            } else {
                if (element.getAttributes().get("args") != null) {
                    Constructor constructor = ClassReflection.getConstructor(clazz, Skin.class, String.class, XmlArguments.class);
                    obj = constructor.newInstance(defaultSkin, element.get("styleName"), new XmlArguments(element.get("args")));
                } else {
                    Constructor constructor = ClassReflection.getConstructor(clazz, Skin.class, String.class);
                    obj = constructor.newInstance(defaultSkin, element.get("styleName")); // skin and styleName
                }
            }
        } catch (ReflectionException e) {
            obj = ClassReflection.newInstance(clazz); // empty constructor
        }
        return obj;
    }

    public void applyAttributes(Actor actor, ObjectMap<String, String> attributes) {
        try {
            actor.setWidth(Float.parseFloat(attributes.get("width")));
            actor.setHeight(Float.parseFloat(attributes.get("height")));
            actor.setX(Float.parseFloat(attributes.get("posx", "0")));
            actor.setY(Float.parseFloat(attributes.get("posy", "0")));
            actor.setScale(Float.parseFloat(attributes.get("scale", "1f")));
            actor.setScaleX(Float.parseFloat(attributes.get("scalex", "1f")));
            actor.setScaleY(Float.parseFloat(attributes.get("scaley", "1f")));
            actor.setRotation(Float.parseFloat(attributes.get("rotation", "0")));
            actor.setVisible(Boolean.parseBoolean(attributes.get("visible", "true")));
            actor.setTouchable(Touchable.valueOf(attributes.get("touchable", actor.getTouchable().name())));
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException occurred while parsing attributes for ID " + attributes.get("name"));
        } catch (Exception e) {
            System.err.println("Exception occurred while parsing attributes for ID " + attributes.get("name"));
        }
    }

    private void applyAttributes(Stage stage, ObjectMap<String, String> attributes) {
        stage.getCamera().viewportWidth = Float.parseFloat(attributes.get("width"));
        stage.getCamera().viewportHeight = Float.parseFloat(attributes.get("height"));
        stage.getCamera().position.x = Float.parseFloat(attributes.get("posx"));
        stage.getCamera().position.y = Float.parseFloat(attributes.get("posy"));
    }

    public static void putElementHandler(ElementHandler handler) {
        elementHandlers.put(handler.getForClass().getName(), handler);
    }

    public static void printExampleXML() {
        System.out.println("<!--Elements that are not listed, are loaded like Actors if they extend Actor-->\n" +
                "<Stage posx=\"Your Value\" posy=\"Your Value\" width=\"Your Value\" height=\"Your Value\">\n" +
                "    <!--onLoad: @see UiXmlLoader.OnLoadMethod-->\n" +
                "    <Actor name=\"Your ID\" width=\"Your Value\" height=\"Your Value\" posx=\"Your Value\" posy=\"Your Value\" scale=\"Your Value\"\n" +
                "           scaleX=\"Your Value\" scaleY=\"Your Value\" rotation=\"Your Value\" visible=\"Your Boolean\"\n" +
                "           touchable=\"Your Touchable\" onLoad=\"Your Class\" styleName=\"Your StyleName\" args=\"key1=value1;key2=value2\"/>\n" +
                "    <!--@see Actor for other Attributes-->\n" +
                "    <Chart columns=\"Your Value\" fillParent=\"Your Boolean\" useBackgrounds=\"Your Boolean\">\n" +
                "        <!--Actors/Items-->\n" +
                "        <Actor/>\n" +
                "        <Actor/>\n" +
                "    </Chart>\n" +
                "    <!--Does not contain right constructor use XmlLabel instead!-->\n" +
                "    <Label align=\"Your Align\"/>\n" +
                "    <!--@see Chart for more information-->\n" +
                "    <LabelChart/>\n" +
                "    <!--No attributes allowed-->\n" +
                "    <Tab>\n" +
                "        <Button/>\n" +
                "        <Table/>\n" +
                "    </Tab>\n" +
                "    <!--@see Actor for other Attributes-->\n" +
                "    <TabbedTable buttonsHeight=\"Your Value\" maxButtonWidth=\"Your Value\">\n" +
                "        <!--@see Tab for more Information-->\n" +
                "        <Tab/>\n" +
                "        <Tab/>\n" +
                "    </TabbedTable>\n" +
                "    <!--@see Actor for other Attributes-->\n" +
                "    <Table fillParent=\"Your Boolean\">\n" +
                "        <!--Actors/Items-->\n" +
                "        <Actor/>\n" +
                "        <Actor/>\n" +
                "    </Table>\n" +
                "    <!--Does not contain right constructor use XmlTextButton instead!-->\n" +
                "    <TextButton/>\n" +
                "    <!--@see Actor for other Attributes-->\n" +
                "    <XmlLabel align=\"Your Align\"/>\n" +
                "    <!--Loaded like an Actor-->\n" +
                "    <XmlTextButton args=\"text=Your Text\"/>\n" +
                "</Stage>");
    }

    public static void addPackage(String newPackage) {
        packages.add(newPackage);
    }

    public XmlStage getStage() {
        return stage;
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface XmlOnLoadMethod {
        String name() default "";
    }

    /**
     * Shows that a constructor could be used by {@link #getInstance(XmlReader.Element, Class)}
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.CONSTRUCTOR)
    public @interface UiXmlConstructor {
    }

    private static class OnLoadMethod {
        private final String annotationName;
        private final Method method;

        public OnLoadMethod(String annotationName, Method method) {
            this.annotationName = annotationName;
            this.method = method;
        }

        @Override
        public String toString() {
            return annotationName + ", " + method;
        }
    }
}
