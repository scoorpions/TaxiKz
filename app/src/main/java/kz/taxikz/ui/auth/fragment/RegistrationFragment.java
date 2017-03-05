package kz.taxikz.ui.auth.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.otto.Subscribe;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.auth.AuthEvents;
import kz.taxikz.helper.UiHelper;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.util.UiUtils;
import kz.taxikz.ui.widget.view.dialog.Dialogs;
import kz.taxikz.ui.widget.view.dialog.DismissDialogListener;
import kz.taxikz.ui.widget.view.maskedEditText.MaskedEditText;
import timber.log.Timber;

public class RegistrationFragment extends BaseAuthFragment {

    @BindView(R.id.edit_text_name)
    EditText mName;
    @BindView(R.id.edit_text_phone)
    MaskedEditText mPhone;
    @BindView(R.id.textInputLayoutName)
    TextInputLayout mTextInputLayoutName;
    @BindView(R.id.textInputLayoutPhone)
    TextInputLayout mTextInputLayoutPhone;
    @BindView(R.id.mainContainer)
    ViewGroup mViewGroupMainContainer;

    private OnSwitchFragment onSwitchFragment;
    private Dialog mProgressDialog;
    private RxPermissions rxPermissions;

    public RegistrationFragment() {
    }

    private void initView() {
        mName.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                mTextInputLayoutName.setError(null);
            }
        });
        mPhone.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                mTextInputLayoutPhone.setError(null);
            }
        });
        mProgressDialog = UiUtils.getProgressDialog(getContext());

    }

    private boolean isValidCredentials() {
        if (UiHelper.isEmptyField(mName, mTextInputLayoutName) || UiHelper.isEmptyField(mPhone, mTextInputLayoutPhone)) {
            return false;
        }
        if (UiHelper.getPhoneFromEditText(mPhone).length() < 10) {
            this.mTextInputLayoutPhone.setError(this.getString(R.string.invalid_number));
            return false;
        }
        return UiHelper.isValidPhone(mPhone, mTextInputLayoutPhone);
    }

    public static Fragment newInstance() {
        return new RegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_num_valid, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        rxPermissions = RxPermissions.getInstance(activity);
        rxPermissions.setLogging(true);
        onSwitchFragment = (OnSwitchFragment) activity;
    }

    @Override
    public void onDestroyView() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mProgressDialog.hide();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.initView();
    }

    @OnClick(R.id.buttonLogIn)
    public void submit() {
        if (isValidCredentials()) {
            askPermission();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onRegisterUserSuccessEventListener(final AuthEvents.RegisterUserSuccess registerUserSuccess) {
        mProgressDialog.hide();
        onSwitchFragment.onSwitchFragment(CountDownFragment.newInstance());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onRegisterUserFailedEventListener(final AuthEvents.RegisterUserFailed registerUserFailed) {
        mProgressDialog.hide();
        UiUtils.getSnackBar(mViewGroupMainContainer, getString(R.string.registration_error));
    }

    public interface OnSwitchFragment {
        void onSwitchFragment(Fragment fragment);
    }

    private class CallListener extends DismissDialogListener {
        private CallListener() {
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            TaxiKzApp.storage.setPhone(UiHelper.getPhoneFromEditText(mPhone));
            TaxiKzApp.storage.setName(mName.getText().toString());
            UiUtils.hideSoftKeyboard(getActivity());
                if (mProgressDialog != null)
                    mProgressDialog.show();
                mAccountController.registerUser();
        }
    }

    private void askPermission() {
        RxPermissions.getInstance(getActivity())
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        Timber.d("granted()");
                        Dialogs.showCallConfirmDialog(getActivity(), mPhone.getText().toString(), new CallListener());
                       // All requested permissions are granted
                    } else {
                        Timber.d("denied()");
                        // At least one permission is denied
                    }
                });
    }
}
