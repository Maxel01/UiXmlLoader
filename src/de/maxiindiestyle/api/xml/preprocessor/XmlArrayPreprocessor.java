package de.maxiindiestyle.api.xml.preprocessor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public class XmlArrayPreprocessor extends XmlPreprocessor {
    @Override
    public String preprocess(String xml, FileHandle fileHandle) {
        XmlReader reader = new XmlReader();
        XmlReader.Element element = reader.parse(xml);
        executeElement(element);
        return element.toString();
    }

    private void executeElement(XmlReader.Element element) {
        if (element.getChildCount() > 0) {
            for (int i = 0; i < element.getChildCount(); i++) {
                executeElement(element.getChild(i));
            }
            return;
        }
        String elementText = element.toString();
        XmlReader.Element parentElement = element.getParent();
        if (elementText.contains("[") && elementText.contains("]")) {
            parentElement.removeChild(element);
            // TODO multiple arrays
            String arrayText = elementText.substring(elementText.indexOf("[") + 1, elementText.indexOf("]"));
            String[] args = arrayText.split(", ");
            for (String arg : args) {
                String xmlArgText = element.toString().replace("[" + arrayText + "]", arg);
                parentElement.addChild(new XmlReader.Element(xmlArgText.substring(1, xmlArgText.length() - 2), parentElement));
            }
        }
    }
}
