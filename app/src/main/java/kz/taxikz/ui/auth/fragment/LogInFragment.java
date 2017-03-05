package kz.taxikz.ui.auth.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.auth.AuthEvents.CheckRegistrationUserFailed;
import kz.taxikz.controllers.auth.AuthEvents.CheckRegistrationUserSuccess;
import kz.taxikz.controllers.auth.AuthEvents.SmsCodeSuccess;
import kz.taxikz.helper.UiHelper;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.main.MainActivity;
import kz.taxikz.ui.util.UiUtils;

public class LogInFragment extends BaseAuthFragment {

    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout mTextInputLayoutPassword;
    @BindString(R.string.activation_sms_get_counter)
    String mStringPassCode;
    @BindView(R.id.edit_text_password)
    EditText mPassword;
    @BindView(R.id.btn_sms_get)
    RobotoTextView mSmsGet;

    private CountDownTimer mCountDownTimer;
    private final long countDownPeriod;
    private Dialog mProgressDialog;

    public LogInFragment() {
        this.countDownPeriod = 60000L;
        this.mCountDownTimer = new SmsCountDown(countDownPeriod, 1000L);
    }

    public static Fragment newInstance() {
        return new LogInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_log_in, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mCountDownTimer.cancel();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onActivateSuccessListener(CheckRegistrationUserSuccess checkRegistrationUserSuccess) {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        mIsPhoneValid.set(true);
        MainActivity.startActivity(getActivity(), true);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSmsCodeSuccessListener(SmsCodeSuccess smsCodeSuccess) {
        mProgressDialog.hide();
        this.mCountDownTimer.start();
        mSmsGet.setEnabled(false);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSmsCodeFailedListener(final CheckRegistrationUserFailed checkRegistrationUserFailed) {
        mProgressDialog.hide();
    }

    private void initView() {
        this.mProgressDialog = UiUtils.getProgressDialog(getContext());
    }

    private boolean isValidCredentials() {
        return !UiHelper.isEmptyField(mPassword, mTextInputLayoutPassword);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_continue)
    public void onContinueClicked() {
        if (isValidCredentials() && UiHelper.isValidPassword(this.mPassword, this.mTextInputLayoutPassword)) {
            TaxiKzApp.storage.setPassword(this.mPassword.getText().toString());
            mProgressDialog.show();
            mAccountController.registerUserBySms();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_sms_get)
    public void onGetPasswordClicked() {
        mProgressDialog.show();
        mAccountController.smsCode();
    }

    private class SmsCountDown extends CountDownTimer {
        private SimpleDateFormat sdf;

        public SmsCountDown(final long period, final long interval) {
            super(period, interval);
            this.sdf = new SimpleDateFormat("mm:ss");
        }

        public void onFinish() {
            mSmsGet.setText(R.string.activation_sms_code_btn);
            mSmsGet.setEnabled(true);
        }

        public void onTick(final long millisUntilFinished) {
            mSmsGet.setText(String.format(mStringPassCode, this.sdf.format(new Date(millisUntilFinished))));

        }
    }
}
