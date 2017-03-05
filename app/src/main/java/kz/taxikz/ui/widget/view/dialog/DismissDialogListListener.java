package kz.taxikz.ui.widget.view.dialog;

import android.support.v7.app.AlertDialog;
import android.view.View;

import java.lang.ref.WeakReference;

import kz.taxikz.ui.common.MonkeySafeClickListener;


public class DismissDialogListListener extends MonkeySafeClickListener {

    private WeakReference<AlertDialog> dialog;

    public void onSafeClick(View v) {
        if (this.dialog != null) {
            AlertDialog dialog = this.dialog.get();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = new WeakReference(dialog);
    }
}
