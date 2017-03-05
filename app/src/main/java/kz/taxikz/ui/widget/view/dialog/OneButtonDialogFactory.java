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

public class OneButtonDialogFactory {

    private final Context mContext;

    public OneButtonDialogFactory(Context context) {
        this.mContext = context;
    }

    public AlertDialog createDialog(OneButtonDialogContent content) {
        TextView title = createTitle(content.title);
        return new Builder(this.mContext).setCustomTitle(title).setView(createContent(content.message, content.btn, content.clickListener)).create();
    }

    @NonNull
    private View createContent(int messageId, int btnTextId, OnClickListener listener) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_onebutton, null);
        ((TextView) view.findViewById(R.id.message)).setText(messageId);
        TextView btn = (TextView) view.findViewById(R.id.button);
        btn.setText(btnTextId);
        btn.setOnClickListener(listener);
        return view;
    }

    @NonNull
    private TextView createTitle(int titleTextId) {
        TextView title = (TextView) LayoutInflater.from(this.mContext).inflate(R.layout.dialog_title, null);
        title.setText(titleTextId);
        return title;
    }
}
