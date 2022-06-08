package de.maxiindiestyle.api.xml.preprocessor;

import com.badlogic.gdx.files.FileHandle;
@SuppressWarnings("unused")
public abstract class XmlPreprocessor {

    public XmlPreprocessor() {
    }

    public abstract String preprocess(String xml, FileHandle fileHandle);

    public static String preprocessedXml(FileHandle fileHandle) {
        String xml = fileHandle.readString();
        xml = new XmlVarargsPreprocessor().preprocess(xml, fileHandle);
        xml = new XmlArrayPreprocessor().preprocess(xml, fileHandle);
        return new XmlIncludePreprocessor().preprocess(xml, fileHandle);
    }
}
