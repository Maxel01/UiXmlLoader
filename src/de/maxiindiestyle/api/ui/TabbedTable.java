package de.maxiindiestyle.api.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * A {@link Table} with more functions than a normal Table.<br>
 * This Table has always a navigation bar to switch between multiple {@link Tab}s.<br>
 * The Tabs are shown below the navigation bar.
 */
@SuppressWarnings("unused")
public class TabbedTable extends Table {
    // TODO-unimportant: location of navigation bar changeable (top, bottom, left, right).

    // JavaDoc variables
    private float buttonsHeight = 50;
    private float maxButtonWidth = Float.MAX_VALUE;

    private ArrayList<Tab> tabs;
    private ButtonGroup<Button> tabButtonGroup;
    private Table tabButtonsTable;
    private Table tabContent;

    private TabChangedListener tabChangedListener;
    private Tab currentTab;

    /**
     * Creates a TabbedTable and initialises it.
     */
    public TabbedTable() {
        super();
        init();
    }

    /**
     * Creates a TabbedTable, sets to skin to his parent {@link Table} and initialises it.
     *
     * @param skin {@link Skin} for the parent {@link Skin}
     * @see Table#Table(Skin)
     */
    public TabbedTable(Skin skin) {
        super(skin);
        init();
    }

    /**
     * Initialises all required variables and sets the layout of the TabbedTable.
     */
    private void init() {
        tabs = new ArrayList<>();
        tabButtonGroup = new ButtonGroup<>();
        tabButtonGroup.setMaxCheckCount(1);
        tabButtonGroup.setMinCheckCount(1);
        tabButtonsTable = new Table(super.getSkin());
        tabButtonsTable.align(Align.left);
        tabChangedListener = new TabChangedListener();
        tabContent = new Table(super.getSkin());
        resize();

        super.addActor(tabContent);
        super.addActor(tabButtonsTable);
    }

    /**
     * Sets the layout of the TabbedTable. The size of {@link #tabButtonsTable} and {@link #tabContent} are recalculated.
     */
    private void resize() {
        tabButtonsTable.setPosition(0, super.getHeight() - buttonsHeight);
        tabButtonsTable.setSize(super.getWidth(), buttonsHeight);
        tabContent.setPosition(0, 0);
        tabContent.setSize(super.getWidth(), super.getHeight() - buttonsHeight);
    }

    /**
     * Sets the size of the buttons.<br>
     * If {@link #maxButtonWidth} is not defined, the buttons use the whole width.<br>
     * If {@link #maxButtonWidth} is defined, each Button can not be bigger than {@link #maxButtonWidth}.
     */
    private void resizeTabButtons() {
        tabButtonsTable.clearChildren();
        float btnWidth = ((super.getWidth()) / (tabs.size()));
        btnWidth = Math.min(btnWidth, maxButtonWidth);
        for (Tab tab : tabs) {
            tabButtonsTable.add(tab.getButton()).size(btnWidth, buttonsHeight);
        }
    }

    /**
     * Adds the {@link Tab} and adds the TabbedTable to it. The TabButtons are resized.
     *
     * @param tab {@link Tab} to add
     */
    public void addTab(Tab tab) {
        tabs.add(tab);
        tab.setTabbedTable(this);

        tab.getButton().addListener(tabChangedListener);
        tabButtonGroup.add(tab.getButton());
        resizeTabButtons();
    }

    /**
     * Removes the {@link Tab} and removes the TabbedTable. The TabButtons are resized.
     *
     * @param tab {@link Tab} to remove
     */
    public void removeTab(Tab tab) {
        tabs.remove(tab);
        tab.setTabbedTable(null);
        tab.getButton().removeListener(tabChangedListener);
        tabButtonGroup.remove(tab.getButton());
        tabButtonsTable.removeActor(tab.getButton());
        resizeTabButtons();
    }

    /**
     * Reloads the current {@link Tab}. So, a new {@link Table} of a Tab is now shown.
     */
    protected void forceReloadTabContent() {
        tabChangedListener.changed(null, null);
    }

    /**
     * Getter for {@link #buttonsHeight}
     *
     * @return height of a {@link Button}
     */
    public float getButtonsHeight() {
        return buttonsHeight;
    }

    /**
     * Sets the new {@link #buttonsHeight} and the TabbedTable is resized.
     *
     * @param buttonsHeight new {@link Button}'s height
     */
    public void setButtonsHeight(float buttonsHeight) {
        this.buttonsHeight = buttonsHeight;
        resize();
    }

    /**
     * Sets the new width of the TabbedTable and the TabbedTable is resized.
     *
     * @param width new width
     */
    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        resize();
    }

    /**
     * Sets the new height of the TabbedTable and the TabbedTable is resized.
     *
     * @param height new height
     */
    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        resize();
    }

    /**
     * Sets the new size of the TabbedTable and the TabbedTable is resized.
     *
     * @param width  new width
     * @param height new height
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        resize();
    }

    /**
     * Getter for the height of the {@link #tabContent} without the button-bar.
     *
     * @return height of {@link #tabContent}
     */
    public float getContentHeight() {
        return tabContent.getHeight();
    }

    /**
     * Getter for the current {@link Tab}
     *
     * @return current shown {@link Tab}
     */
    public Tab getCurrentTab() {
        return currentTab;
    }

    /**
     * Getter for {@link #maxButtonWidth}
     *
     * @return current {@link #maxButtonWidth} which can be {@link Float#MAX_VALUE}
     */
    public float getMaxButtonWidth() {
        return maxButtonWidth;
    }

    /**
     * Setter for {@link #maxButtonWidth}
     *
     * @param maxButtonWidth any positive number
     */
    public void setMaxButtonWidth(float maxButtonWidth) {
        this.maxButtonWidth = maxButtonWidth;
    }

    /**
     * {@link ChangeListener} for changing the current {@link Tab}.
     */
    private class TabChangedListener extends ChangeListener {
        /**
         * Fires when a {@link Button} of a {@link Tab} was clicked.<br>
         * Sets the new Tab content.
         *
         * @param event not important, can be null
         * @param actor not important, can be null
         */
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            for (Tab currTab : tabs) {
                if (currTab.getButton().isChecked()) {
                    currentTab = currTab;
                    tabContent.clear();
                    tabContent.addActor(currTab.getTabContent());
                    return;
                }
            }
        }
    }
}

