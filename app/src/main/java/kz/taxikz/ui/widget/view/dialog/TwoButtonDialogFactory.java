package kz.taxikz.ui.widget.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import kz.taxikz.taxi4.R;

public class TwoButtonDialogFactory {
    private final Context context;

    public TwoButtonDialogFactory(Context context) {
        this.context = context;
    }

    public AlertDialog createDialog(TwoButtonDialogContent content) {
        TextView title = createTitle(content.title);
        return new Builder(context).setCustomTitle(title).setView(createContent(content.message, content.firstBtnTxtId, content.secondBtnTxtId, content.firstBtnClickListener, content.secondBtnClickListener, content.firstBtnColor)).create();
    }

    public AlertDialog createDialog(TwoButtonDialogContent content, String message) {
        TextView title = createTitle(content.title);
        return new Builder(context).setCustomTitle(title).setView(createContent(message, content.firstBtnTxtId, content.secondBtnTxtId, content.firstBtnClickListener, content.secondBtnClickListener, content.firstBtnColor)).create();
    }
    @NonNull
    private View createContent(int messageId, int firstBtnTextId, int secondBtnTextId, OnClickListener firstBtnListener, OnClickListener secondBtnListener, Integer firstBtnColor) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.dialog_twobutton, null);
        ((TextView) view.findViewById(R.id.message)).setText(messageId);
        TextView firstBtn = (TextView) view.findViewById(R.id.first_btn);
        TextView secondBtn = (TextView) view.findViewById(R.id.second_btn);
        firstBtn.setText(firstBtnTextId);
        secondBtn.setText(secondBtnTextId);
        firstBtn.setOnClickListener(firstBtnListener);
        secondBtn.setOnClickListener(secondBtnListener);
        if (firstBtnColor != null) {
            firstBtn.setTextColor(firstBtnColor);
        }
        return view;
    }

    @NonNull
    private View createContent(String messageId, int firstBtnTextId, int secondBtnTextId, OnClickListener firstBtnListener, OnClickListener secondBtnListener, Integer firstBtnColor) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.dialog_twobutton, null);
        ((TextView) view.findViewById(R.id.message)).setText(messageId);
        TextView firstBtn = (TextView) view.findViewById(R.id.first_btn);
        TextView secondBtn = (TextView) view.findViewById(R.id.second_btn);
        firstBtn.setText(firstBtnTextId);
        secondBtn.setText(secondBtnTextId);
        firstBtn.setOnClickListener(firstBtnListener);
        secondBtn.setOnClickListener(secondBtnListener);
        if (firstBtnColor != null) {
            firstBtn.setTextColor(firstBtnColor);
        }
        return view;
    }

    @NonNull
    private TextView createTitle(int titleTextId) {
        TextView title = (TextView) LayoutInflater.from(this.context).inflate(R.layout.dialog_title, null);
        title.setText(titleTextId);
        return title;
    }
}
