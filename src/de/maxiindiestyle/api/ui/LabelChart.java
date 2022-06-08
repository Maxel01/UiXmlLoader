package de.maxiindiestyle.api.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.maxiindiestyle.api.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

// TODO-API JavaDoc, cleanup
@SuppressWarnings("unused")
public class LabelChart extends Chart {
    private int defaultStartRow = 0;
    private int defaultEndRow = 0; // - -> rows - defaultEndRow, + -> defaultEndRow
    private int sortedCol = -1;
    private final HashMap<Integer, Comparator<String>> sortComparators;

    public LabelChart(Skin skin) {
        super(skin);
        sortComparators = new HashMap<>();
    }

    public LabelChart(Skin skin, String styleName) {
        super(skin, styleName);
        sortComparators = new HashMap<>();
    }

    public LabelChart(Skin skin, ChartStyle style) {
        super(skin, style);
        sortComparators = new HashMap<>();
    }

    @Override
    public void addItem(Actor actor) {
        if (ClassReflection.isInstance(Label.class, actor)) {
            super.addItem(actor);
        } else {
            try {
                throw new IllegalArgumentException("For LabelChart only Labels are allowed");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void addItem(Table actor) {
        super.addItem(actor.getChildren().get(0), actor);
    }

    public void sort(int colNumber) {
        sort(colNumber, defaultStartRow);
    }

    public void sort(int colNumber, int startRow) {
        if (sortComparators.get(colNumber) != null) {
            sort(colNumber, startRow, (tableA, tableB) -> {
                String stringA = ((Label) tableA.getChildren().get(0)).getText().toString();
                String stringB = ((Label) tableB.getChildren().get(0)).getText().toString();
                return sortComparators.get(colNumber).compare(stringA, stringB);
            });
        } else {
            sort(colNumber, startRow, Comparators.getDefaultComparator());
        }
    }

    public void sort(int colNumber, Comparator<? super Table> comparator) {
        sort(colNumber, defaultStartRow, comparator);
    }

    public void sort(int colNumber, int startRow, Comparator<? super Table> comparator) {
        if (colNumber >= super.getColumnCount()) return;
        HashMap<Integer, SnapshotArray<Table>> table = getColsData(startRow, defaultEndRow);
        SnapshotArray<Table> col = new SnapshotArray<>(table.get(colNumber));
        SnapshotArray<Table> sorted = new SnapshotArray<>(table.get(colNumber));
        sorted.sort(comparator);
        if (sortedCol == colNumber) {
            sorted.reverse();
            sortedCol = -1;
        } else {
            sortedCol = colNumber;
        }

        HashMap<Integer, SnapshotArray<Table>> sortedTable = getColsData(startRow, defaultEndRow);
        for (int i = 0; i < col.size; i++) {
            int index = sorted.indexOf(col.get(i), false);
            for (int k = 0; k < table.size(); k++) {
                sortedTable.get(k).set(index, table.get(k).get(i));
            }
        }
        Table[] staticStartItems = getStaticStartItems(startRow);
        Table[] staticEndItems = getStaticEndItems(defaultEndRow);
        refillChart(staticStartItems, sortedTable, staticEndItems);
    }

    private void refillChart(Table[] staticStartItems, HashMap<Integer, SnapshotArray<Table>> sortedTable, Table[] staticEndItems) {
        super.clearChildren();
        super.currentCol = 0;
        super.currentRowCount = 0;
        super.itemRow = 0;
        for (Table t : staticStartItems) {
            addItem(t);
        }
        for (int i = 0; i < sortedTable.get(0).size; i++) {
            for (int j = 0; j < sortedTable.size(); j++) {
                this.addItem(sortedTable.get(j).get(i));
            }
        }
        for (Table t : staticEndItems) {
            addItem(t);
        }
    }

    private Table[] getStaticStartItems(int startRow) {
        Table[] tables = new Table[startRow * super.getColumnCount()];
        SnapshotArray<Actor> children = super.getChildren();
        for (int i = 0; i < tables.length; i++) {
            tables[i] = (Table) children.get(i);
        }
        return tables;
    }

    private Table[] getStaticEndItems(int endRow) {
        Table[] tables;
        if (endRow < 0) endRow = -endRow;
        tables = new Table[endRow * super.getColumnCount()];

        SnapshotArray<Actor> children = super.getChildren();
        for (int i = 0, j = (super.getRows() - endRow) * super.getColumnCount(); i < tables.length; i++, j++) {
            tables[i] = (Table) children.get(j);
        }
        return tables;
    }

    private HashMap<Integer, SnapshotArray<Table>> getColsData(int startRow, int endRow) {
        HashMap<Integer, SnapshotArray<Table>> table = new HashMap<>();
        SnapshotArray<Actor> children = super.getChildren();
        children.ordered = false;

        if (endRow < 0) endRow = -endRow;
        endRow = endRow * super.getColumnCount();

        for (int i = 0; i < super.getColumnCount(); i++) {
            table.put(i, new SnapshotArray<>());
        }
        for (int i = startRow * super.getColumnCount(); i < children.size - endRow; i++) {
            Table t = (Table) children.items[i];
            table.get(i % super.getColumnCount()).add(t);
        }
        return table;
    }

    @SafeVarargs
    public final void addSortComparators(Comparator<String>... comparators) {
        for (int i = 0; i < comparators.length; i++) {
            if (comparators[i] == null) {
                for (int j = i; j >= 0; j--) {
                    if (comparators[j] != null) {
                        addSortComparator(i, comparators[j]);
                        break;
                    }
                }
            } else {
                addSortComparator(i, comparators[i]);
            }
        }
    }

    public void addSortComparator(int forCol, Comparator<String> comparator) {
        if (sortComparators.containsKey(forCol)) {
            System.err.println("LabelChart: Comparator for column '" + forCol + "' already set!");
            return;
        }
        sortComparators.put(forCol, comparator);
    }

    public void removeSortComparator(int forCol) {
        if (!sortComparators.containsKey(forCol)) {
            System.err.println("LabelChart: No comparator for column '" + forCol + "' found!");
            return;
        }
        sortComparators.remove(forCol);
    }

    public void setDefaultStartRow(int defaultStartRow) {
        this.defaultStartRow = defaultStartRow;
    }

    public int getDefaultStartRow() {
        return defaultStartRow;
    }

    public void setDefaultEndRow(int defaultEndRow) {
        this.defaultEndRow = defaultEndRow;
    }

    public int getDefaultEndRow() {
        return defaultEndRow;
    }

    public static class Comparators {

        private static Comparator<? super Table> getDefaultComparator() {
            return (tA, tB) -> {
                Label lA = (Label) tA.getChildren().get(0);
                Label lB = (Label) tB.getChildren().get(0);
                return lA.getText().toString().compareToIgnoreCase(lB.getText().toString());
            };
        }

        public static Comparator<String> timeComparator() {
            return timeComparator(true);
        }

        /* Format: hh:mm */
        public static Comparator<String> timeComparator(boolean ab) {
            return (stringA, stringB) -> {
                int timeA = Time.parseTime(stringA);
                int timeB = Time.parseTime(stringB);
                return ab ? timeA - timeB : timeB - timeA;
            };
        }

        public static Comparator<String> intComparator() {
            return intComparator(true);
        }


        public static Comparator<String> intComparator(boolean ab) {
            return (stringA, stringB) -> {
                int compare = Integer.compare(Integer.parseInt(stringA), Integer.parseInt(stringB));
                return ab ? compare : compare * -1;
            };
        }

        public static Comparator<String> dateComparator() {
            return (stringA, stringB) -> {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
                try {
                    return dateFormat.parse(stringA).compareTo(dateFormat.parse(stringB));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            };
        }

        public static Comparator<String> stringComparator() {
            return String::compareToIgnoreCase;
        }
    }
}
