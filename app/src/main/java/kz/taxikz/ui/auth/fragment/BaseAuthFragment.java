package kz.taxikz.ui.auth.fragment;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.auth.AccountController;
import kz.taxikz.data.prefs.BooleanPreference;
import kz.taxikz.ui.BaseFragment;
import kz.taxikz.ui.auth.IsPhoneValid;

public class BaseAuthFragment extends BaseFragment {

    @Inject
    AccountController mAccountController;

    @Inject
    @IsPhoneValid
    BooleanPreference mIsPhoneValid;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}
