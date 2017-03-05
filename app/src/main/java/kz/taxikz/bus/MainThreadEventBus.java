package kz.taxikz.bus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class MainThreadEventBus extends Bus {
    private final Handler mainThread;

    public MainThreadEventBus() {
        mainThread = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(final Object o) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(o);
            return;
        }

        mainThread.post(() -> post(o));
    }
}
