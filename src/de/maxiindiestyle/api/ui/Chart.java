package de.maxiindiestyle.api.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

// TODO-API JavaDoc, Cleanup
@SuppressWarnings("unused")
public class Chart extends Table {

    private int columns = 2;

    protected int currentCol = 0;
    protected int currentRowCount = 0; // count of rows, changed by addRow()
    protected int itemRow = 0; // for setting background, changed after setting backgrounds for the previous row

    private float chartWidth = 400f;
    private float rowHeight = 25;

    private boolean useBackgrounds = true;
    private boolean isRound = true;
    private boolean hasTwoCells = true;
    private final ChartStyle style;

    private Table[] currentRow = new Table[columns];

    public Chart(Skin skin) {
        this(skin, new ChartStyle());
    }

    public Chart(Skin skin, String styleName) {
        this(skin, skin.get(styleName, ChartStyle.class));
    }

    public Chart(Skin skin, ChartStyle style) {
        super(skin);
        super.setBackground(style.background);
        super.setWidth(chartWidth);
        this.style = style;
        super.align(Align.top);

        if (style.cell == null) {
            useBackgrounds = false;
            isRound = false;
            hasTwoCells = false;
            return;
        }
        checkIfRound();
        checkIfHasTwoCells();
    }

    private void checkIfRound() {
        if (style.topLeft == null || style.topRight == null) isRound = false;
        if (style.bottomLeft == null || style.bottomRight == null) isRound = false;
    }

    private void checkIfHasTwoCells() {
        if (style.cell == null || style.cell1 == null) hasTwoCells = false;
        if (isRound) {
            if (style.bottomLeft1 == null || style.bottomRight1 == null) hasTwoCells = false;
        }
    }

    public void addItems(Actor... actors) {
        for (Actor actor : actors) {
            addItem(actor);
        }
    }


    public void addItem(Actor actor) {
        addItem(actor, new Table());
    }

    public void addItem(Actor actor, Table item) {
        if (currentCol == columns) {
            addRow();
            if (useBackgrounds) {
                for (int col = 0; col < columns; col++) {
                    setBackgroundCell(currentRow[col]);
                    currentRow[col] = null;
                }
            }
            itemRow++;
        }
        if (useBackgrounds) {
            item.setSkin(super.getSkin());
            setBackground(item);
            item.add(actor);
            super.add(item).fillX().expandX().height(rowHeight + 1);
            currentRow[currentCol] = item;
        } else {
            super.add(actor).fillX().expandX().height(rowHeight);
        }
        currentCol++;
    }

    private void setBackground(Table item) {
        setBackgroundCell(item);
        setBackgroundFirstRow(item);
        setBackgroundLastRow(item);
    }

    private void setBackgroundCell(Table item) {
        if (itemRow >= 0) {
            item.setBackground(style.cell);
            if (hasTwoCells && itemRow % 2 == 1) {
                item.setBackground(style.cell1);
            }
        }
    }

    private void setBackgroundFirstRow(Table item) {
        if (itemRow == 0 && isRound) {
            if (currentCol == 0) {
                item.setBackground(style.topLeft);
            } else {
                item.setBackground(style.topRight);
            }
        }
    }

    private void setBackgroundLastRow(Table item) {
        if (hasTwoCells && itemRow % 2 == 1) {
            if (isRound) {
                if (currentCol == 0) {
                    item.setBackground(style.bottomLeft1);
                } else {
                    item.setBackground(style.bottomRight1);
                }
            } else {
                item.setBackground(style.cell1);
            }
        } else if (itemRow != 0 && isRound) {
            if (currentCol == 0) {
                item.setBackground(style.bottomLeft);
            } else {
                item.setBackground(style.bottomRight);
            }
        }
    }

    public void addRow() {
        super.add().height(rowHeight).row();
        currentCol = 0;
        currentRowCount++;
        super.setHeight((currentRowCount + 1) * rowHeight);
    }

    public void setWidth(float width) {
        super.setWidth(width);
        this.chartWidth = width;
    }

    public float getChartWidth() {
        return chartWidth;
    }

    public int getColumnCount() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        Table[] tmp = new Table[columns];
        System.arraycopy(currentRow, 0, tmp, 0, Math.min(currentRow.length, tmp.length));
        currentRow = tmp;
    }

    public int getCurrentRow() {
        return currentRowCount;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public void setUseBackgrounds(boolean useBackgrounds) {
        this.useBackgrounds = useBackgrounds;
    }

    static public class ChartStyle {
        public Drawable background;
        public Drawable topRight, topLeft, cell, bottomLeft, bottomRight;
        public Drawable cell1, bottomLeft1, bottomRight1;
    }
}
