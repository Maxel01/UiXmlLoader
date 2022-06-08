package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.maxiindiestyle.api.ui.Tab;
import de.maxiindiestyle.api.ui.TabbedTable;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;

/**
 * {@link ElementHandler} for loading {@link TabbedTable} elements.
 */
public class TabbedTableElementHandler extends ElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link TabbedTable}.
     */
    public TabbedTableElementHandler() {
        super(TabbedTable.class);
    }

    /**
     * Loads a {@link TabbedTable}, applies the attributes,
     * and adds the child tabs to the Table.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link #forClass} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        TabbedTable table = (TabbedTable) obj;
        loader.applyAttributes(table, element.getAttributes());
        table.setButtonsHeight(Float.parseFloat(element.getAttribute("buttonsHeight", table.getButtonsHeight() + "")));
        table.setMaxButtonWidth(Float.parseFloat(element.getAttribute("maxButtonWidth", table.getMaxButtonWidth() + "")));
        for (int i = 0; i < element.getChildCount(); i++) {
            // TODO-unimportant: what happens with unused Objects?
            Object childObj = loader.loadElement(element.getChild(i), loader.getStage());
            if (ClassReflection.isInstance(Tab.class, childObj)) {
                table.addTab((Tab) childObj);
            }
        }
    }

    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        return false;
    }
}
