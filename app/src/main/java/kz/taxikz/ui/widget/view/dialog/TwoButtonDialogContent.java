package kz.taxikz.ui.widget.view.dialog;

import android.view.View.OnClickListener;

public class TwoButtonDialogContent {
    public final OnClickListener firstBtnClickListener;
    public Integer firstBtnColor;
    public final int firstBtnTxtId;
    public final int message;
    public final OnClickListener secondBtnClickListener;
    public final int secondBtnTxtId;
    public final int title;

    public TwoButtonDialogContent(int titleId, int messageId, int firstBtnTxtId, int secondBtnTxtId, OnClickListener firstBtnClickListener, OnClickListener secondBtnClickListener) {
        this.title = titleId;
        this.message = messageId;
        this.firstBtnTxtId = firstBtnTxtId;
        this.secondBtnTxtId = secondBtnTxtId;
        this.firstBtnClickListener = firstBtnClickListener;
        this.secondBtnClickListener = secondBtnClickListener;
    }

    public TwoButtonDialogContent(int titleId, int messageId, int firstBtnTxtId, int secondBtnTxtId, OnClickListener firstBtnClickListener, OnClickListener secondBtnClickListener, int firstBtnColor) {
        this(titleId, messageId, firstBtnTxtId, secondBtnTxtId, firstBtnClickListener, secondBtnClickListener);
        this.firstBtnColor = firstBtnColor;
    }
}
