package de.maxiindiestyle.api.ui.xml;

import com.badlogic.gdx.utils.XmlReader;
import de.maxiindiestyle.api.xml.preprocessor.XmlVarargsPreprocessor;

import java.util.ArrayList;

public class Example {

    // Loads everything, when no other more specific method is available
    @UiXmlLoader.XmlOnLoadMethod(name = "*")
    public static void onLoad(UiXmlLoader loader, XmlReader.Element element) {
        // Do something
    }

    // Only loads the element with name 'chart'
    @UiXmlLoader.XmlOnLoadMethod(name = "chart")
    public static void onLoadChart(UiXmlLoader loader, XmlReader.Element element) {
        // Do something
    }

    // Loads all elements that begin with 'Test'
    @UiXmlLoader.XmlOnLoadMethod(name = "*Test")
    public static void onLoadBeginsWithTest(UiXmlLoader loader, XmlReader.Element element) {
        // Do something
    }

    @XmlVarargsPreprocessor.Varargs
    public static ArrayList<String> values() {
        // Put your values in
        return new ArrayList<>();
    }
}
