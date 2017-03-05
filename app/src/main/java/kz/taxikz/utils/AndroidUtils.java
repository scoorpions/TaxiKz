/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.Display
 *  android.view.KeyCharacterMap
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.Window
 *  android.view.WindowManager
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.Spinner
 */
package kz.taxikz.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;
import java.util.Locale;

import kz.taxikz.ui.common.click.DoubleClickPresenter;
import kz.taxikz.taxi4.R;


public class AndroidUtils {

    private static final double LARGE_PHONE_DIAGONAL = 4.2;
    private static final int PHONE = 1;
    private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
    private static final int TABLET_LARGE = 2;
    private static final int TABLET_XLARGE = 3;
    private static Integer deviceType;
    private static final DoubleClickPresenter sClickPreventer;
    private static int sNavBarBottomHeight;
    private static String sNavBarOverride;
    private static int sNavBarRightWidth;

    static {
        sClickPreventer = new DoubleClickPresenter(300);
        try {
            Method method = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class);
            method.setAccessible(true);
            sNavBarOverride = (String)method.invoke(null, "qemu.hw.mainkeys");
        }
        catch (Throwable ignored) {}
    }


    /*
     * Enabled aggressive block sorting
     */
    private static int getDeviceType(Context context) {
        if (deviceType != null) return deviceType;
        if ((context.getResources().getConfiguration().screenLayout & 15) == 4) {
            deviceType = TABLET_XLARGE;
            return deviceType;
        }
        if ((context.getResources().getConfiguration().screenLayout & 15) == 3) {
            deviceType = TABLET_LARGE;
            return deviceType;
        }
        deviceType = PHONE;
        return deviceType;
    }

    private static double getDiagonalInInches(Context context) {
        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        double d = displayMetrics.density * 160.0f;
        return Math.sqrt(Math.pow((double)displayMetrics.widthPixels / d, 2.0) + Math.pow((double)displayMetrics.heightPixels / d, 2.0));
    }

    public static String getLanguage() {
        return AndroidUtils.getLocale().getLanguage().toLowerCase();
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    /*
     * Enabled aggressive block sorting
     */
//    @Deprecated
//    public static int getNavBarHeight(Context context) {
//        int n = 0;
//        boolean bl = ViewConfiguration.get(context).hasPermanentMenuKey();
//        boolean bl2 = KeyCharacterMap.deviceHasKey(4);
//        int n2 = n;
//        if (bl) return n2;
//        n2 = n;
//        if (bl2) return n2;
//        Resources resources = context.getResources();
//        int n3 = context.getResources().getConfiguration().orientation;
//        if (AndroidUtils.isTablet(context)) {
//            String string2 = n3 == PHONE ? "navigation_bar_height" : "navigation_bar_height_landscape";
//            n3 = resources.getIdentifier(string2, "dimen", "android");
//        } else {
//            String string3 = n3 == PHONE ? "navigation_bar_height" : "navigation_bar_width";
//            n3 = resources.getIdentifier(string3, "dimen", "android");
//        }
//        n2 = n;
//        if (n3 <= 0) return n2;
//        return context.getResources().getDimensionPixelSize(n3);
//    }

    public static int getSideNavBarWidth() {
        return sNavBarRightWidth;
    }

    public static int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        return AndroidUtils.getStatusBarHeight(context.getResources());
    }

    public static int getStatusBarHeight(Resources resources) {
        int n = 0;
        int n2 = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (n2 > 0) {
            n = resources.getDimensionPixelSize(n2);
        }
        return n;
    }

    public static int getToolbarHeight(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }
        return context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
    }

    public static int getViewYLocationOnScreen(View view) {
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        return arrn[1];
    }

    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @TargetApi(value=14)
    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int n = resources.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
        if (n != 0) {
            boolean bl = resources.getBoolean(n);
            return !"1".equals(sNavBarOverride) && ("0".equals(sNavBarOverride) || bl);
        }
        return !ViewConfiguration.get(context).hasPermanentMenuKey();
    }

    public static void hideKeyboard(Activity activity) {
        View view;
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view2 = view = activity.getCurrentFocus();
        if (view == null) {
            view2 = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view2.getWindowToken(), 0);
    }

//    public static boolean isGoogleServicesAvailable(Context context) {
//        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0;
//    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == TABLET_LARGE;
    }

    public static boolean isLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= TABLET_XLARGE;
    }

    public static boolean isLargePhone(Context context) {
        return AndroidUtils.isPhone(context) && AndroidUtils.getDiagonalInInches(context) > LARGE_PHONE_DIAGONAL;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isMetricUnits() {
        String string2 = AndroidUtils.getLocale().getCountry();
        return !"US".equals(string2) && !"LR".equals(string2) && !"MM".equals(string2);
    }

    public static boolean isPhone(Context context) {
        return AndroidUtils.getDeviceType(context) == 1;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static boolean isTablet(Context context) {
        return AndroidUtils.getDeviceType(context) != PHONE && AndroidUtils.isLarge(context);
    }

    public static boolean preventDoubleClick() {
        return sClickPreventer.preventDoubleClick();
    }

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            return;
        }
        view.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setUpViewBelowStatusBar(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        marginLayoutParams.setMargins(marginLayoutParams.leftMargin, AndroidUtils.getStatusBarHeight(view.getContext()), marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
    }

    public static boolean smallDensity(Context context) {
        int n = context.getResources().getDisplayMetrics().densityDpi;
        return n == 120 || n == 160 || n == 240;
    }

    public static int convertDpToPx(Context context, int n) {
        return (int)TypedValue.applyDimension(1, (float)n, context.getResources().getDisplayMetrics());
    }

    // Не даем диалогу закрыть по нажатию кнопки назад
    public static Dialog.OnKeyListener onKeyListener = new Dialog.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // Не даем закрыться диалогу
            }
            return true;
        }
    };
}

