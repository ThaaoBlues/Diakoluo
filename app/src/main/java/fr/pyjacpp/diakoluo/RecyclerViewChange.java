package fr.pyjacpp.diakoluo;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewChange {
    public static final short None = 0;              // 00000000
    public static final short ItemChanged = 1;       // 00000001
    public static final short ItemInserted = 2;      // 00000010
    public static final short ItemMoved = 4;         // 00000100
    public static final short ItemRangeChanged = 8;  // 00001000
    public static final short ItemRangeInserted = 16;// 00010000
    public static final short ItemRangeRemoved = 32; // 00100000
    public static final short ItemRemoved = 64;      // 01000000
    public static final short DataSetChanged = 128;  // 10000000

    private int changes;
    private Integer position = null;
    private Integer positionStart = null;
    private Integer positionEnd = null;

    public RecyclerViewChange(int changes) {
        this.changes = changes;
    }

    public boolean changeIsIn(int change) {
        return (changes & change) == change;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Integer positionStart) {
        this.positionStart = positionStart;
    }

    public Integer getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Integer positionEnd) {
        this.positionEnd = positionEnd;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public void apply(RecyclerView.Adapter recyclerViewAdapter) {
        if (changeIsIn(ItemChanged)) {
            recyclerViewAdapter.notifyItemChanged(position);
        } if (changeIsIn(ItemInserted)) {
            recyclerViewAdapter.notifyItemInserted(position);
        } if (changeIsIn(ItemMoved)) {
            recyclerViewAdapter.notifyItemMoved(positionStart, positionEnd);
        } if (changeIsIn(ItemRangeChanged)) {
            recyclerViewAdapter.notifyItemRangeChanged(positionStart, positionEnd - positionStart);
        } if (changeIsIn(ItemRangeInserted)) {
            recyclerViewAdapter.notifyItemRangeInserted(positionStart, positionEnd - positionStart);
        } if (changeIsIn(ItemRangeRemoved)) {
            recyclerViewAdapter.notifyItemRangeRemoved(positionStart, positionEnd - positionStart);
        } if (changeIsIn(ItemRemoved)) {
            recyclerViewAdapter.notifyItemRemoved(positionStart);
        } if (changeIsIn(DataSetChanged)) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
