/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.os.Bundle
 *  android.os.Message
 */
package kz.taxikz.ui.widget.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class AlertDialogFragment extends DialogFragment {

    private AlertDialog alertDialog;

    public static AlertDialogFragment create(AlertDialog alertDialog) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setDialog(alertDialog);
        return alertDialogFragment;
    }

    private void setDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setRetainInstance(true);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return this.alertDialog;
    }

    @Override
    public void onDestroyView() {
        if (this.getDialog() != null && this.getRetainInstance()) {
            this.getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}

