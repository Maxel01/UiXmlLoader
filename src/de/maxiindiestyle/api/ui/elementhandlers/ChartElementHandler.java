package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.maxiindiestyle.api.ui.Chart;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;
import de.maxiindiestyle.api.ui.xml.XmlStage;

/**
 * {@link ElementHandler} for loading {@link Chart} elements.
 */
public class ChartElementHandler extends ElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link Chart}.
     */
    public ChartElementHandler() {
        super(Chart.class);
    }

    /**
     * Constructor only used for overriding this Class.
     *
     * @param forClass Class extends {@link Chart}
     * @see ElementHandler#ElementHandler(Class)
     */
    protected ChartElementHandler(Class<? extends Chart> forClass) {
        super(forClass);
    }

    /**
     * Loads a chart, applies the attributes,
     * loads children elements with {@link UiXmlLoader#loadElement(XmlReader.Element, XmlStage)} and
     * adds {@link Actor}s to the Chart with {@link Chart#addItem(Actor)}.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link Chart} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        Chart chart = (Chart) obj;
        ObjectMap<String, String> attr = element.getAttributes();
        loader.applyAttributes(chart, attr);
        chart.setColumns(Integer.parseInt(attr.get("columns", "2")));
        chart.setFillParent(Boolean.parseBoolean(attr.get("fillParent", "false")));
        chart.setUseBackgrounds(Boolean.parseBoolean(attr.get("useBackgrounds", "false")));
        for (int i = 0; i < element.getChildCount(); i++) {
            // TODO-unimportant: what happens with unused Elements?
            Object childObj = loader.loadElement(element.getChild(i), loader.getStage());
            if (ClassReflection.isInstance(Actor.class, childObj))
                chart.addItem((Actor) childObj);
        }
    }

    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        return false;
    }
}
