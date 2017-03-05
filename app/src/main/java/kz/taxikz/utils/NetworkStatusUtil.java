package kz.taxikz.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkStatusUtil {

    private Context context;

    public NetworkStatusUtil(Context context) {
        this.context = context;
    }


    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

//    public static String getConnectivityStatusString(Context context) {
//        int conn = NetworkStatusUtil.getConnectivityStatus(context);
//        String status = null;
//        if (conn == NetworkStatusUtil.TYPE_WIFI) {
//            status = "Wifi enabled";
//        } else if (conn == NetworkStatusUtil.TYPE_MOBILE) {
//            status = "Mobile data enabled";
//        } else if (conn == NetworkStatusUtil.TYPE_NOT_CONNECTED) {
//            status = "Not connected to Internet";
//        }
//        return status;
//    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            return true;
        } else {
//            showNoInternetDialog();
            return false;
        }
    }
//    public void showNoInternetDialog() {
//            if(MManager ==null)
//                MManager = new MManager(context);
//                MManager.showDialog(context.getText(R.string.error).toString(),
//                        context.getText(R.string.need_internet_connection).toString(),
//                        context.getText(R.string.ok).toString());
//
//    }
}
