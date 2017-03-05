package kz.taxikz.ui.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;

public final class UiUtils {
    public static Dialog getProgressDialog(final Context context) {
        return getProgressDialog(context, R.string.please_wait);
    }

    public static Dialog getProgressDialog(final Context context, @StringRes final int text) {
        final View inflate = LayoutInflater.from(context).inflate(R.layout.layout_wait, null);
        ((TextView) ButterKnife.findById(inflate, R.id.message)).setText(text);
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppIndeterminateDialog)).setCancelable(false).setView(inflate).create();
    }

    public static Snackbar getSnackBar(final View view, final String s) {
        return getSnackBar(view, s, 3000, null, null);
    }

    public static Snackbar getSnackBar(final View view, final String s, final int duration, final View.OnClickListener clickListener, final String s2) {
        final Snackbar setActionTextColor = Snackbar.make(view, s, Snackbar.LENGTH_LONG).setDuration(duration).setActionTextColor(-1);
        if (clickListener != null && !TextUtils.isEmpty(s2)) {
            setActionTextColor.setAction(s2, clickListener);
        }
        setActionTextColor.getView().setBackgroundColor(Color.DKGRAY);
        final TextView textView = (TextView) setActionTextColor.getView().findViewById(R.id.snackbar_text);
        ((TextView) setActionTextColor.getView().findViewById(R.id.snackbar_text)).setTypeface(null, 1);
        textView.setTextColor(-1);
        return setActionTextColor;
    }

    public static Snackbar getSnackBar(final View view, final String s, final View.OnClickListener clickListener) {
        return getSnackBar(view, s, 3000, null, TaxiKzApp.get().getString(R.string.retry));
    }

    public static void hideSoftKeyboard(final Activity activity) {
        final View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public static void setImageViewSizeFromResource(ImageView imageView, int widthResId, int heightResId) {
        Context context = imageView.getContext();
        if (widthResId == heightResId) {
            int iconWidthHeight = context.getResources().getDimensionPixelSize(widthResId);
            imageView.getLayoutParams().width = iconWidthHeight;
            imageView.getLayoutParams().height = iconWidthHeight;
        } else {
            int iconWidth = context.getResources().getDimensionPixelSize(widthResId);
            int iconHeight = context.getResources().getDimensionPixelSize(heightResId);
            imageView.getLayoutParams().width = iconWidth;
            imageView.getLayoutParams().height = iconHeight;
        }
        imageView.requestLayout();
    }

    public static void showAppInGooglePlay(Activity activity) {
        String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void showSoftKeyboardForEditText(EditText editText) {
        if (editText != null && editText.requestFocus()) {
            ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 1);
        }
    }

}
