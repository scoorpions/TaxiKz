package kz.taxikz.di;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.squareup.otto.Bus;
import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;

public abstract class BaseDaggerFragment extends Fragment {
    
    @Inject
    protected Bus mBus;

    protected abstract void onInject();

    @Override
    public void onAttach(Activity activity) {
        TaxiKzApp.get().component().inject(this);
        onInject();
        super.onAttach(activity);
    }
}
