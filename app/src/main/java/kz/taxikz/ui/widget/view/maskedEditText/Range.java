package kz.taxikz.ui.widget.view.maskedEditText;

public class Range {
    private int end;
    private int start;

    Range() {
        this.start = -1;
        this.end = -1;
    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return this.end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
