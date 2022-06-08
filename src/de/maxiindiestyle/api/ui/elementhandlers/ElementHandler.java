package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.utils.XmlReader;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;
import de.maxiindiestyle.api.ui.xml.XmlStage;

/**
 * ElementHandler for handling the loading procedure of an element in {@link UiXmlLoader#loadElement(XmlReader.Element, XmlStage)}.<br>
 * Use static ElementHandlers defined in this class instead of creating your own objects.
 */
@SuppressWarnings("unused")
public abstract class ElementHandler {

    /**
     * Defines the {@link Class} for which this ElementHandler is made.
     *
     * @see UiXmlLoader#putElementHandler(ElementHandler)
     */
    protected final Class<?> forClass;

    /**
     * Creates the ElementHandler for the given class.
     *
     * @param forClass sets {@link #forClass} attribute
     */
    public ElementHandler(Class<?> forClass) {
        this.forClass = forClass;
    }

    /**
     * Method invoked when an Element is loaded by {@link UiXmlLoader#loadElement(XmlReader.Element, XmlStage)}.
     * This method is for individual Element handling like applying attributes, loading child-elements...<br>
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link #forClass} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     * @see UiXmlLoader#printExampleXML() for attributes and structure
     */
    public abstract void load(Object obj, XmlReader.Element element, UiXmlLoader loader);

    /**
     * Method invoked when an Element is loaded by {@link UiXmlLoader#reloadElement(Object, String)}.
     * This method is for another handling of reloaded Elements, but can also do the same things as {@link #load(Object, XmlReader.Element, UiXmlLoader)}.<br>
     * If the method returns false, the element is loaded as it isn't reloaded.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link #forClass} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     * @return if reload is handled or not.
     * @see #load(Object, XmlReader.Element, UiXmlLoader)
     */
    public abstract boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader);

    /**
     * Getter for {@link #forClass}
     *
     * @return {@link #forClass}
     */
    public Class<?> getForClass() {
        return forClass;
    }
}
