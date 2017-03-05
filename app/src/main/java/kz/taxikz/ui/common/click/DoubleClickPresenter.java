package kz.taxikz.ui.common.click;

public class DoubleClickPresenter {

    private long mLastClickTime;
    private final long mTimeDelta;

    public DoubleClickPresenter(final long mTimeDelta) {
        this.mTimeDelta = mTimeDelta;
    }

    public boolean preventDoubleClick() {
        if (System.currentTimeMillis() - this.mLastClickTime < this.mTimeDelta && System.currentTimeMillis() - this.mLastClickTime > 0L) {
            return true;
        }
        this.mLastClickTime = System.currentTimeMillis();
        return false;
    }
}

