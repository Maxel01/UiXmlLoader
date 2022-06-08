package de.maxiindiestyle.api.xml;

import java.util.HashMap;

/**
 * Class representing arguments for usage of XML-Arguments.
 * XML-Arguments declared as an attribute in an XML-Tag will be represented as "key, value" attributes.
 */
public class XmlArguments {

    /**
     * {@link HashMap} storing the attributes as key, value.
     */
    public HashMap<String, String> args;

    /**
     * Wraps a line into XmlArguments.
     *
     * @param lineArgs {@link String} written in form of "key=value;key=value;..."
     */
    public XmlArguments(String lineArgs) {
        args = new HashMap<>();
        String[] argsParts = lineArgs.split(";");
        for (String arg : argsParts) {
            if(!arg.contains("=")) continue;
            args.put(arg.split("=")[0], arg.split("=")[1]);
        }
    }
}
