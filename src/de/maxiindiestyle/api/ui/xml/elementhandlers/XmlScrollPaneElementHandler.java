package de.maxiindiestyle.api.ui.xml.elementhandlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.maxiindiestyle.api.ui.elementhandlers.ElementHandler;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;
import de.maxiindiestyle.api.ui.xml.XmlScrollPane;

/**
 * {@link ElementHandler} for loading {@link Table} elements.
 */
public class XmlScrollPaneElementHandler extends ElementHandler {
    /**
     * Creates the ElementHandler for the Class {@link XmlScrollPane}.
     */
    public XmlScrollPaneElementHandler() {
        super(XmlScrollPane.class);
    }

    /**
     * Loads a {@link XmlScrollPane}, applies attributes,
     * and adds children {@link Actor} to the widget.<br>
     * Adding elements with {@link Table#add(Actor)} is not available.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link XmlScrollPane} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        ScrollPane scrollPane = (ScrollPane) obj;
        loader.applyAttributes(scrollPane, element.getAttributes());
        scrollPane.setFillParent(Boolean.parseBoolean(element.getAttributes().get("fillParent", "false")));
        for (int i = 0; i < element.getChildCount(); i++) {
            Object childObj = loader.loadElement(element.getChild(i), loader.getStage());
            if (ClassReflection.isInstance(Actor.class, childObj)) {
                scrollPane.setActor((Actor) childObj);
            }
        }
    }

    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        return false;
    }
}
