package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;

/**
 * Basic {@link ElementHandler} for loading {@link Actor} elements.
 */
public class ActorElementHandler extends ElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link Actor}.
     */
    public ActorElementHandler() {
        super(Actor.class);
    }

    /**
     * Just applies the basic attributes.
     *
     * @param obj     The {@link Object} of the current Element which can be casted without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        if(element == null) return;
        loader.applyAttributes((Actor) obj, element.getAttributes());
    }

    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        return false;
    }
}
