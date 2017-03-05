package kz.taxikz.di;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import kz.taxikz.TaxiKzApp;

public abstract class BaseDaggerDialogFragment extends DialogFragment {

    @Inject
    protected Bus eventBus;
    private Unbinder unbinder;

    protected abstract void onInject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TaxiKzApp.get().component().inject(this);
        onInject();
        this.eventBus.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.eventBus.unregister(this);
    }

    protected View inflateDialogView(int res, ViewGroup root) {
        View v = LayoutInflater.from(getActivity()).inflate(res, root);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
