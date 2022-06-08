package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;

/**
 * {@link ElementHandler} for loading {@link Label} elements
 */
public class LabelElementHandler extends ElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link Label}.
     */
    public LabelElementHandler() {
        super(Label.class);
    }

    /**
     * Constructor only used for overriding this Class.
     *
     * @param forClass Class extends {@link Label}
     * @see ElementHandler#ElementHandler(Class)
     */
    protected LabelElementHandler(Class<? extends Label> forClass) {
        super(forClass);
    }

    /**
     * Loads a Label and applies their attributes.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link Label} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        Label label = (Label) obj;
        try {
            loader.applyAttributes(label, element.getAttributes());
            label.setAlignment((int) ClassReflection.getDeclaredField(Align.class,
                    element.getAttributes().get("align", "left")).get(null));
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        return false;
    }
}
