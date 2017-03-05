package kz.taxikz.di;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kz.taxikz.TaxiKzApp;

public class BaseDaggerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TaxiKzApp.get().component().inject(this);
    }
}
