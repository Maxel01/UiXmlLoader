package de.maxiindiestyle.api.ui.xml.elementhandlers;

import de.maxiindiestyle.api.ui.elementhandlers.ElementHandler;
import de.maxiindiestyle.api.ui.elementhandlers.LabelElementHandler;
import de.maxiindiestyle.api.ui.xml.XmlLabel;

/**
 * {@link ElementHandler} for loading {@link XmlLabel} elements.
 */
public class XmlLabelElementHandler extends LabelElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link XmlLabel}.
     */
    public XmlLabelElementHandler() {
        super(XmlLabel.class);
    }
}
