package kz.taxikz.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServiceUtil {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Check the device to make sure it has the Google Play ClientApiService APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(@NonNull Activity activity) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                activity.finish();
            }
            return false;
        }
        return true;
    }
}
