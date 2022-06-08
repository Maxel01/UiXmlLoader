package de.maxiindiestyle.api.ui.xml;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.maxiindiestyle.api.xml.XmlArguments;

/**
 * A Basic {@link Label} which provides the constructors used for loading objects.
 * Use this class only in XML-Files.
 */
public class XmlLabel extends Label {

    /**
     * Constructor which sets the {@link Skin} and no text.
     *
     * @param skin Skin for the Label
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlLabel(Skin skin) {
        super("", skin);
    }

    /**
     * Constructor which sets the {@link Skin} and no text.
     *
     * @param skin      Skin for the Label
     * @param styleName styleName defined in skin
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlLabel(Skin skin, String styleName) {
        super("", skin, styleName);
    }

    /**
     * Constructor uses the key 'text' of args for setting the text of the Label.
     *
     * @param skin Skin for the Label
     * @param args arguments with key 'text', if not set, no text is set
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlLabel(Skin skin, XmlArguments args) {
        super(args.args.get("text"), skin);
    }

    /**
     * Constructor uses the key 'text' of args for setting the text of the Label.
     *
     * @param skin      Skin for the Label
     * @param styleName name defined in skin
     * @param args      arguments with key 'text', if not set, no text is set
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlLabel(Skin skin, String styleName, XmlArguments args) {
        super(args.args.get("text"), skin, styleName);
    }
}
