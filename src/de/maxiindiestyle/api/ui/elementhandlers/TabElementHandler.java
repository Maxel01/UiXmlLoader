package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.maxiindiestyle.api.ui.Tab;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;

/**
 * {@link ElementHandler} for loading {@link Tab} elements.
 */
public class TabElementHandler extends ElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link Tab}.
     */
    public TabElementHandler() {
        super(Tab.class);
    }

    /**
     * Loads the Tab with a default {@link Table} if none is defined,
     * otherwise the last child-{@link Table} and the last child-{@link Button} is set for the Tab.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link Tab} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        Tab tab = (Tab) obj;
        tab.setTabContent(new Table(loader.getDefaultSkin()));
        for (int i = 0; i < element.getChildCount(); i++) {
            // TODO-unimportant: what happens with unused Elements?
            Object childObj = loader.loadElement(element.getChild(i), loader.getStage());
            if (ClassReflection.isInstance(Button.class, childObj))
                tab.setButton((Button) childObj);
            if (ClassReflection.isInstance(Table.class, childObj)
                    && !ClassReflection.isInstance(Button.class, childObj) && !ClassReflection.isInstance(Widget.class, childObj)) {
                tab.setTabContent((Table) childObj);
            }
        }
    }

    /**
     * Reloads only the {@link Table} of the {@link Tab} and forces the TabbedTable to reload.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link Tab} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     * @return reload is always handled
     */
    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        Tab tab = (Tab) obj;
        for (int i = 0; i < element.getChildCount(); i++) {
            // TODO-unimportant: what happens with unused Elements?
            Object childObj = loader.loadElement(element.getChild(i), loader.getStage());
            if (ClassReflection.isInstance(Table.class, childObj)
                    && !ClassReflection.isInstance(Button.class, childObj) && !ClassReflection.isInstance(Widget.class, childObj)) {
                tab.setTabContent((Table) childObj);
            }
        }
        return true;
    }
}
