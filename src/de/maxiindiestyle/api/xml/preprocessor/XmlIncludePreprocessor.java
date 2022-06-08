package de.maxiindiestyle.api.xml.preprocessor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import de.maxiindiestyle.api.xml.XmlArguments;

import java.util.Map;

public class XmlIncludePreprocessor extends XmlPreprocessor {

    @Override
    public String preprocess(String xml, FileHandle fileHandle) {
        for (int index = xml.indexOf("<include"); index != -1; index = xml.indexOf("<include", index + 1)) {
            xml = executeInclude(xml, index, fileHandle);
        }
        return xml;
    }

    private String executeInclude(String xml, int index, FileHandle fileHandle) {
        String includeTxt = xml.substring(index, xml.indexOf("/>", index) + 2);
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(includeTxt);

        String src = element.get("src", null);
        String replaceTxt = "";
        if (src != null && fileHandle.sibling(src).exists()) {
            replaceTxt = XmlPreprocessor.preprocessedXml(fileHandle.sibling(src));
        } else {
            System.err.println("Src not found for '" + includeTxt + "'!");
        }
        XmlArguments args = new XmlArguments(element.get("replace", ""));
        for (Map.Entry<String, String> entry : args.args.entrySet()) {
            replaceTxt = replaceTxt.replaceAll(entry.getKey(), entry.getValue());
        }
        xml = xml.replace(includeTxt, replaceTxt);
        //System.out.println("include text: " + includeTxt);
        return xml;
    }
}
