package kz.taxikz.ui.widget.view.dialog;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;

import kz.taxikz.taxi4.R;

public class Dialogs {
    public static void showCancelOrderDialog(Activity activity, OnClickListener okListener) {
        Builder builder = new Builder(activity);
        builder.setMessage(activity.getString(R.string.delete_order_confirm_message)).setTitle(activity.getString(R.string.delete_order_confirm_title)).setCancelable(true).setPositiveButton(activity.getString(R.string.yes), okListener).setNegativeButton(activity.getString(R.string.no), okListener);
        builder.create().show();
    }

    public static void showCallConfirmDialog(FragmentActivity context, String phone, DismissDialogListener alarmListener) {
        DismissDialogListener dismissListener = new DismissDialogListener();
        AlertDialog alertDialog = new TwoButtonDialogFactory(context).createDialog(new TwoButtonDialogContent(R.string.dialog_105_title, R.string.dialog_105_message, R.string.dialog_105_cancel_btn, R.string.yes, dismissListener, alarmListener), String.format(context.getString(R.string.dialog_105_message), phone));
        dismissListener.setDialog(alertDialog);
        alarmListener.setDialog(alertDialog);
        show(AlertDialogFragment.create(alertDialog), context.getSupportFragmentManager(), "tag");
    }

    public static void showSplashRetryDialog(FragmentActivity activity, DismissDialogListener alarmListener) {
        DismissDialogListener dismissListener = new DismissDialogListener(activity);
        AlertDialog alertDialog = new TwoButtonDialogFactory(activity).createDialog(new TwoButtonDialogContent(R.string.error, R.string.need_internet_connection, R.string.exit, R.string.retry, dismissListener, alarmListener));
        dismissListener.setDialog(alertDialog);
        alarmListener.setDialog(alertDialog);
        show(AlertDialogFragment.create(alertDialog), activity.getSupportFragmentManager(), "tag");
    }

    public static void showQuitConfirmDialog(FragmentActivity activity, DismissDialogListener alarmListener) {
        DismissDialogListener dismissListener = new DismissDialogListener(activity);
        AlertDialog alertDialog = new TwoButtonDialogFactory(activity).createDialog(new TwoButtonDialogContent(R.string.exit_from_account, R.string.navigation_exit_alert_message, R.string.no, R.string.yes, dismissListener, alarmListener));
        dismissListener.setDialog(alertDialog);
        alarmListener.setDialog(alertDialog);
        show(AlertDialogFragment.create(alertDialog), activity.getSupportFragmentManager(), "tag");
    }

    private static void show(DialogFragment dialog, FragmentManager supportFragmentManager, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(dialog, tag);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.d("Dialgos", "Cant open dialog " + dialog.getClass().getSimpleName());
        }
    }
}
