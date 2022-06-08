package de.maxiindiestyle.api.ui.xml;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.maxiindiestyle.api.xml.XmlArguments;

@SuppressWarnings("unused")
public class XmlScrollPane extends ScrollPane {
    /**
     * Constructor which sets the {@link Skin} and no text.
     *
     * @param skin Skin for the ScrollPane
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlScrollPane(Skin skin) {
        super(new Table(), skin);
    }

    /**
     * Constructor which sets the {@link Skin} and no text.
     *
     * @param skin      Skin for the scrollPane
     * @param styleName styleName defined in skin
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlScrollPane(Skin skin, String styleName) {
        super(new Table(), skin, styleName);
    }

    /**
     * Constructor uses the key 'text' of args for setting the text of the ScrollPane.
     *
     * @param skin Skin for the ScrollPane
     * @param args arguments with key 'text', if not set, no text is set
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlScrollPane(Skin skin, XmlArguments args) {
        super(new Table(), skin);
    }

    /**
     * Constructor uses the key 'text' of args for setting the text of the ScrollPane.
     *
     * @param skin      Skin for the ScrollPane
     * @param styleName styleName defined in skin
     * @param args      arguments with key 'text', if not set, no text is set
     */
    @UiXmlLoader.UiXmlConstructor
    public XmlScrollPane(Skin skin, String styleName, XmlArguments args) {
        super(new Table(), skin, styleName);
    }
}
