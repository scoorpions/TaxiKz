package kz.taxikz.ui.address.fragment;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.address.AutoCompleteController;
import kz.taxikz.ui.BaseFragment;

public class BaseAddressFragment extends BaseFragment {
    @Inject
    AutoCompleteController autoCompleteController;

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}
