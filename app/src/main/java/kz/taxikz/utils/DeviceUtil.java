package kz.taxikz.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import kz.taxikz.ui.common.click.DoubleClickPresenter;

public class DeviceUtil {

    private static Context sContext;
    private static String sDeviceUID;

    private static final DoubleClickPresenter sClickPresenter;
    static {
        sClickPresenter = new DoubleClickPresenter(300);
    }
    public static int convertDpToPx(final Context context, final int n) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) n, context.getResources().getDisplayMetrics());
    }

    public static String getAndroidReleaseVersion() {
        return Build.VERSION.RELEASE;
    }
    public static boolean preventDoubleClick() {
        return sClickPresenter.preventDoubleClick();
    }

    public static String getDensityString(final DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            default: {
                return "unknown";
            }
            case DisplayMetrics.DENSITY_LOW: {
                return "ldpi";
            }
            case DisplayMetrics.DENSITY_MEDIUM: {
                return "mdpi";
            }
            case DisplayMetrics.DENSITY_HIGH: {
                return "hdpi";
            }
            case DisplayMetrics.DENSITY_XHIGH: {
                return "xhdpi";
            }
            case DisplayMetrics.DENSITY_XXHIGH: {
                return "xxhdpi";
            }
            case DisplayMetrics.DENSITY_XXXHIGH: {
                return "xxxhdpi";
            }
            case DisplayMetrics.DENSITY_TV: {
                return "tvdpi";
            }
        }
    }

    public static String getDeviceType() {
        return "Android " + Build.VERSION.RELEASE;
    }

    public static String getDeviceUID() {
        if (DeviceUtil.sDeviceUID == null) {
            if (Build.SERIAL != null) {
                DeviceUtil.sDeviceUID = Build.SERIAL;
            } else if (Settings.Secure.getString(DeviceUtil.sContext.getContentResolver(), "android_id") != null) {
                DeviceUtil.sDeviceUID = Settings.Secure.getString(DeviceUtil.sContext.getContentResolver(), "android_id");
            }
        }
        return DeviceUtil.sDeviceUID;
    }

    public static Point getDisplaySize(final Context context) {
//        Preconditions.checkNotNull(context);
        final Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static int getNavigationBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean isLandscape() {
        return DeviceUtil.sContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLollipopOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLowResTablet() {
        return DeviceUtil.sContext.getResources().getConfiguration().smallestScreenWidthDp >= 600 && DeviceUtil.sContext.getResources().getConfiguration().smallestScreenWidthDp <= 720;
    }

    public static boolean isPhone() {
        return DeviceUtil.sContext.getResources().getConfiguration().smallestScreenWidthDp < 600;
    }

    public static boolean isPortrait() {
        return DeviceUtil.sContext.getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isTablet() {
        return DeviceUtil.sContext.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public void setupApplicationContext(final Context sContext) {
        DeviceUtil.sContext = sContext;
    }
}
