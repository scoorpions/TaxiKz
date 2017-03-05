package kz.taxikz.ui.auth.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import kz.taxikz.controllers.auth.AuthEvents.CheckRegistrationUserFailed;
import kz.taxikz.controllers.auth.AuthEvents.CheckRegistrationUserSuccess;
import kz.taxikz.receiver.CallListener;
import kz.taxikz.receiver.CallListener.OnCallCaughtListener;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.auth.fragment.RegistrationFragment.OnSwitchFragment;
import kz.taxikz.ui.main.MainActivity;
import timber.log.Timber;

public class CountDownFragment extends BaseAuthFragment implements OnCallCaughtListener {
    private static final int COUNTDOWN_MSEC = 20000;
    private CallListener mBroadcastReceiver;
    private ProgressDialog mCheckNumberProgressDialog;
    @BindView(R.id.code)
    RobotoTextView mCountDown;
    private CountDownTimer mCountDownTimer;
    private OnSwitchFragment onSwitchFragment;

    class CallWaitCountDown extends CountDownTimer {
        private SimpleDateFormat sdf;


        public CallWaitCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.sdf = new SimpleDateFormat("ss");
        }

        @Override
        public void onFinish() {
            mCheckNumberProgressDialog.dismiss();
            onSwitchFragment.onSwitchFragment(LogInFragment.newInstance());
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCountDown.setText(this.sdf.format(new Date(millisUntilFinished)));
        }
    }

    public CountDownFragment() {
        mCountDownTimer = new CallWaitCountDown(COUNTDOWN_MSEC, 1000);
        mBroadcastReceiver = new CallListener();
    }

    public static Fragment newInstance() {
        return new CountDownFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_count_down, null);
        mCheckNumberProgressDialog = new ProgressDialog(getActivity());
        mCheckNumberProgressDialog.setMessage(getContext().getString(R.string.progress_dialog_check_number));
        mCheckNumberProgressDialog.setCancelable(false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCountDownTimer.cancel();
        mBroadcastReceiver.setOnCallCaughtListener(null);
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mCheckNumberProgressDialog.isShowing()) {
            mCountDownTimer.start();
        }
        getActivity().registerReceiver(mBroadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
        mBroadcastReceiver.setOnCallCaughtListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onSwitchFragment = (OnSwitchFragment) getActivity();
    }

    @Subscribe
    public void onCheckRegistrationUserSuccessListener(CheckRegistrationUserSuccess checkRegistrationUserSuccess) {
        mCheckNumberProgressDialog.dismiss();
        mIsPhoneValid.set(true);
        MainActivity.startActivity(getActivity(), true);
    }

    @Subscribe
    public void onCheckRegistrationUserFailedListener(CheckRegistrationUserFailed checkRegistrationUserFailed) {
        this.mCheckNumberProgressDialog.dismiss();
        showCheckNumberErrorDialog();
        onSwitchFragment.onSwitchFragment(LogInFragment.newInstance());
    }

    @Override
    public void onCallCaught(String phoneNumber) {
        Timber.d("onCallCaught()");
        mAccountController.checkRegistration(phoneNumber);
        mCountDownTimer.cancel();
        mCheckNumberProgressDialog.show();
    }

    private void showCheckNumberErrorDialog() {
        Builder builder = new Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_check_number_error_title));
        builder.setMessage(getString(R.string.dialog_check_number_error_text));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.create().show();
    }
}
