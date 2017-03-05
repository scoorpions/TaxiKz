package kz.taxikz.ui.widget.item;

public class Note {
    private int mId;
    private boolean mIsSelected;
    private String mName;
    private int mOptionId;

    public String getName() {
        return this.mName;
    }

    public int getId() {
        return this.mId;
    }

    public int getOptionId() {
        return this.mOptionId;
    }

    public boolean isSelected() {
        return this.mIsSelected;
    }

    public void toggle() {
        if (this.mIsSelected) {
            this.mIsSelected = false;
        } else {
            this.mIsSelected = true;
        }
    }

    public Note(int id, int optionId, String name) {
        this.mId = id;
        this.mOptionId = optionId;
        this.mName = name;
    }

    public Note(Note note) {
        this.mId = note.getId();
        this.mName = note.getName();
        this.mIsSelected = note.isSelected();
        this.mOptionId = note.getOptionId();
    }
}
