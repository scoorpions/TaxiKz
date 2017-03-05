package kz.taxikz.ui.widget.view.dialog;

import android.view.View.OnClickListener;

public class OneButtonDialogContent {

    public final int btn;
    public final OnClickListener clickListener;
    public final int message;
    public final int title;

    public OneButtonDialogContent(int titleId, int messageId, int btnId, OnClickListener clickListener) {
        this.title = titleId;
        this.message = messageId;
        this.btn = btnId;
        this.clickListener = clickListener;
    }
}
