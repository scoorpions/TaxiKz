package kz.taxikz.ui.widget.view.dialog;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.lang.ref.WeakReference;

import kz.taxikz.ui.common.MonkeySafeClickListener;
import kz.taxikz.ui.splash.SplashScreensActivity;

public class DismissDialogListener extends MonkeySafeClickListener {

    private WeakReference<AlertDialog> dialog;
    private FragmentActivity activity;

    public DismissDialogListener(FragmentActivity activity) {
        this.activity = activity;
    }

    public DismissDialogListener() {}

    public void onSafeClick(View v) {
        if (dialog != null) {
            AlertDialog dialog = this.dialog.get();
            if (dialog != null) {
                dialog.dismiss();
                if(activity != null && activity instanceof SplashScreensActivity){
                    activity.finish();
                }
            }
        }
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = new WeakReference(dialog);
    }
}
