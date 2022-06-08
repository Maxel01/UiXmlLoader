package de.maxiindiestyle.api.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.maxiindiestyle.api.ui.xml.UiXmlLoader;

/**
 * A Tab with a {@link Button} for the button-bar and a content {@link Table}.<br>
 * The Tab is represented like a Tab in a browser.
 */
@SuppressWarnings("unused")
public class Tab {

    /**
     * {@link Button} for the button-bar of {@link TabbedTable}.
     */
    protected Button btn;
    /**
     * The content of the Tab
     */
    protected Table tabContent;
    /**
     * The {@link TabbedTable} to which the Tab is added.
     */
    protected TabbedTable tabbedTable;

    /**
     * Creates an empty Tab.
     */
    @UiXmlLoader.UiXmlConstructor
    public Tab() {
    }

    /**
     * Creates a Tab with a {@link Button} and a {@link Table} (used as content of the Tab).<br>
     * The Table fills his parent and will be clipped to avoid the content to get over his border.
     *
     * @param btn        Button for the Tab
     * @param tabContent Table as content
     */
    public Tab(Button btn, Table tabContent) {
        this.btn = btn;
        setTabContent(tabContent);
    }

    /**
     * Sets the {@link TabbedTable} and will be called while adding to TabbedTable.
     *
     * @param tabbedTable TabbedTable to which this Tab was added.
     * @see TabbedTable#addTab(Tab)
     */
    protected void setTabbedTable(TabbedTable tabbedTable) {
        if (this.tabbedTable != null && tabbedTable != null) {
            throw new IllegalStateException("A Tab can be added only once to a TabbedTable. Remove it before adding to another");
        }
        this.tabbedTable = tabbedTable;
    }

    /**
     * Sets the {@link Button} of the Tab.
     *
     * @param btn new Button of the Tab
     */
    public void setButton(Button btn) {
        if (tabbedTable != null) {
            throw new IllegalStateException("Remove the Tab from the TabbedTable before changing the Button.");
        }
        this.btn = btn;
    }

    /**
     * Getter for {@link #btn}
     *
     * @return the tab's Button
     */
    public Button getButton() {
        return btn;
    }

    /**
     * Sets the {@link Table} of the Tab.<br>
     * The Table fills his parent and will be clipped to avoid the content to get over his border.<br>
     * Forces the {@link TabbedTable} to reload the current Tab.
     *
     * @param tabContent the new Table
     */
    public void setTabContent(Table tabContent) {
        this.tabContent = tabContent;
        tabContent.setFillParent(true);
        tabContent.setClip(true);
        if (tabbedTable != null) {
            tabbedTable.forceReloadTabContent();
        }
    }

    /**
     * Getter for {@link #tabContent}.
     *
     * @return the tab's content Table
     */
    public Table getTabContent() {
        return tabContent;
    }

    /**
     * Getter for {@link #tabbedTable}
     *
     * @return current {@link TabbedTable} or null.
     */
    public TabbedTable getTabbedTable() {
        return tabbedTable;
    }
}