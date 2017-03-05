package kz.taxikz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import kz.taxikz.utils.CallUtil;
import timber.log.BuildConfig;

public class CallListener extends BroadcastReceiver {
    private OnCallCaughtListener onCallCaughtListener;

    public interface OnCallCaughtListener {
        void onCallCaught(String str);
    }

    public void setOnCallCaughtListener(OnCallCaughtListener onCallCaughtListener) {
        this.onCallCaughtListener = onCallCaughtListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra("state").equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String incomingNumber = extras.getString("incoming_number");
                if (incomingNumber != null && !incomingNumber.isEmpty()) {
                    incomingNumber = incomingNumber.replace("+", BuildConfig.VERSION_NAME);
                    incomingNumber = incomingNumber.substring(1, incomingNumber.length());
                    if (CallUtil.disconnectCall() && this.onCallCaughtListener != null) {
                        this.onCallCaughtListener.onCallCaught(incomingNumber);
                    }
                }
            }
        }
    }
}
