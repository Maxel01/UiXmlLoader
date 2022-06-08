package de.maxiindiestyle.api.ui.xml;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.maxiindiestyle.api.xml.XmlArguments;

/**
 * A Basic {@link TextButton} which provides the constructors used for loading objects.
 * Use this class only in XML-Files.
 */
@SuppressWarnings("unused")
public class XmlTextButton extends TextButton {

    /**
     * Constructor which sets the {@link Skin} and no text.
     *
     * @param skin Skin for the TextButton
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlTextButton(Skin skin) {
        super("", skin);
    }

    /**
     * Constructor which sets the {@link Skin} and no text.
     *
     * @param skin      Skin for the TextButton
     * @param styleName styleName defined in skin
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlTextButton(Skin skin, String styleName) {
        super("", skin, styleName);
    }

    /**
     * Constructor uses the key 'text' of args for setting the text of the TextButton.
     *
     * @param skin Skin for the TextButton
     * @param args arguments with key 'text', if not set, no text is set
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlTextButton(Skin skin, XmlArguments args) {
        super(args.args.get("text"), skin);
    }

    /**
     * Constructor uses the key 'text' of args for setting the text of the TextButton.
     *
     * @param skin      Skin for the TextButton
     * @param styleName styleName defined in skin
     * @param args      arguments with key 'text', if not set, no text is set
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlTextButton(Skin skin, String styleName, XmlArguments args) {
        super(args.args.get("text"), skin, styleName);
    }
}
