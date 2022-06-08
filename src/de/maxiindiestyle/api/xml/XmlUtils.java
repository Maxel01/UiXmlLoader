package de.maxiindiestyle.api.xml;

import com.badlogic.gdx.utils.XmlReader;

/**
 * Helper methods for handling XML-files.
 */
public class XmlUtils {

    /**
     * Finds an element containing the value of an attribute.
     * The method searches recursively, so it searches in all child elements.
     *
     * @param root      root {@link XmlReader.Element} to search an element with given attribute and value.
     * @param attribute attribute name
     * @param value     value of the attribute
     * @return {@link XmlReader.Element} containing the value of an attribute or null if nothing is found.
     */
    public static XmlReader.Element findElementByAttribute(XmlReader.Element root, String attribute, String value) {
        if (root.getChildCount() == 0) return null;
        for (int i = 0; i < root.getChildCount(); i++) {
            XmlReader.Element element = root.getChild(i);
            if (element.getAttribute(attribute).equals(value)) return element;
            XmlReader.Element found = findElementByAttribute(element, attribute, value);
            if (found != null) return found;
        }
        return null;
    }
}
