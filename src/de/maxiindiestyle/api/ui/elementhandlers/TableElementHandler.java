package de.maxiindiestyle.api.ui.elementhandlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;

/**
 * {@link ElementHandler} for loading {@link Table} elements.
 */
public class TableElementHandler extends ElementHandler {

    /**
     * Creates the ElementHandler for the Class {@link Table}.
     */
    public TableElementHandler() {
        super(Table.class);
    }

    /**
     * Loads a {@link Table}, applies attributes,
     * and adds children {@link Actor} with {@link Table#addActor(Actor)}.<br>
     * Adding elements with {@link Table#add(Actor)} is not available.
     *
     * @param obj     The {@link Object} of the current Element which can be cast to {@link Table} without class check.
     * @param element The Element's {@link XmlReader.Element} with all attributes and children.
     * @param loader  {@link UiXmlLoader} which loads this element.
     */
    @Override
    public void load(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        Table table = (Table) obj;
        loader.applyAttributes(table, element.getAttributes());
        table.setFillParent(Boolean.parseBoolean(element.getAttributes().get("fillParent", "false")));
        for (int i = 0; i < element.getChildCount(); i++) {
            Object childObj = loader.loadElement(element.getChild(i), loader.getStage());
            if (ClassReflection.isInstance(Actor.class, childObj))
                // TODO-unimportant: add feature to load with Table#add(Actor)
                table.addActor((Actor) childObj);
        }
    }

    @Override
    public boolean reload(Object obj, XmlReader.Element element, UiXmlLoader loader) {
        return false;
    }
}
