package kz.taxikz.controllers.auth;

import android.text.TextUtils;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.analytics.AnalyticsTrackers;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.auth.AuthEvents.CheckRegistrationUserFailed;
import kz.taxikz.controllers.auth.AuthEvents.CheckRegistrationUserSuccess;
import kz.taxikz.controllers.auth.AuthEvents.RegisterUserFailed;
import kz.taxikz.controllers.auth.AuthEvents.RegisterUserSuccess;
import kz.taxikz.controllers.auth.AuthEvents.SmsCodeFailed;
import kz.taxikz.controllers.auth.AuthEvents.SmsCodeSuccess;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CredentialsData;
import kz.taxikz.data.api.pojo.RegistrationData;
import kz.taxikz.job.auth.CheckRegistrationJob;
import kz.taxikz.job.auth.RegistrationBySmsJob;
import kz.taxikz.job.auth.RegistrationJob;
import kz.taxikz.job.auth.SmsCodeJob;
import kz.taxikz.utils.Preconditions;
import timber.log.BuildConfig;
import timber.log.Timber;

public class AccountController extends BaseController {
    private AnalyticsTrackers mAnalyticsTrackers;
    private final Bus mBus;
    private final JobManager mJobManager;
    private String mRejectedPhoneNumber;

    public AccountController(Bus mBus, JobManager mJobManager, AnalyticsTrackers mAnalyticsTrackers) {
        this.mBus = mBus;
        this.mJobManager = mJobManager;
        this.mAnalyticsTrackers = mAnalyticsTrackers;
    }

    public void registerUser() {
        String name = TaxiKzApp.storage.getName();
        String phone = TaxiKzApp.storage.getPhone();
        Preconditions.checkNotNull(phone);
        this.mJobManager.addJobInBackground(new RegistrationJob(this, name, phone));
    }

    public void checkRegistration(String rejectedPhoneNumber) {
        this.mRejectedPhoneNumber = rejectedPhoneNumber;
        this.mJobManager.addJobInBackground(new CheckRegistrationJob(this, TaxiKzApp.storage.getRegistrationKey(), this.mRejectedPhoneNumber));
    }

    public void registerUserBySms() {
        this.mJobManager.addJobInBackground(new RegistrationBySmsJob(this, TaxiKzApp.storage.getPhone(), TaxiKzApp.storage.getName(), TaxiKzApp.storage.getPassword()));
    }

    public void handleRegisterUserError(Throwable throwable, String s) {
        if (throwable instanceof SocketTimeoutException) {
            registerUser();
        }
        this.mBus.post(new RegisterUserFailed());
        if (!TextUtils.isEmpty(s)) {
            this.mAnalyticsTrackers.numValid(null, s, false);
        }
    }

    public void handleRegisterUserSuccess(BaseApiData<RegistrationData> reply) {
        if (reply.getCode() == ApiConfig.CODE_OK) {
            RegistrationData registrationData = reply.getData();
            TaxiKzApp.storage.setName(registrationData.getName());
            TaxiKzApp.storage.setPhone(registrationData.getPhone());
            TaxiKzApp.storage.setRegistrationKey(registrationData.getKey());
            this.mBus.post(new RegisterUserSuccess());
            return;
        }
        this.mBus.post(new RegisterUserFailed());
    }

    public void handleCheckRegistrationSuccess(BaseApiData<CredentialsData> reply) {
        if (reply.getCode() == ApiConfig.CODE_OK) {
            String clientId = reply.getData().getClientId();
            String password = reply.getData().getPassword();
            TaxiKzApp.storage.setClientId(clientId);
            TaxiKzApp.storage.setPassword(password);
            this.mBus.post(new CheckRegistrationUserSuccess());
            return;
        }
        this.mBus.post(new CheckRegistrationUserFailed());
    }

    public void handleCheckRegistrationError(Throwable throwable, String s) {
        if (throwable instanceof SocketTimeoutException) {
            checkRegistration(this.mRejectedPhoneNumber);
        }
        if (!TextUtils.isEmpty(s)) {
            this.mAnalyticsTrackers.activate(null, s, false);
        }
    }

    public void smsCode() {
        this.mJobManager.addJobInBackground(new SmsCodeJob(this, TaxiKzApp.storage.getPhone(), TaxiKzApp.storage.getRegistrationKey()));
    }

    public void handleSmsCodeSuccess(BaseApiData<List<String>> reply) {
        Timber.d("handleSmsCodeSuccess code");
        if (reply.getCode() == ApiConfig.CODE_OK) {
            this.mBus.post(new SmsCodeSuccess());
        } else {
            this.mBus.post(new SmsCodeFailed(new Exception()));
        }
    }

    public void handleSmsCodeError(Throwable throwable, String s) {
        if (!TextUtils.isEmpty(s)) {
            this.mAnalyticsTrackers.smsCode("sms", s, false);
        }
        if (throwable instanceof SocketTimeoutException) {
            smsCode();
        } else {
            this.mBus.post(new SmsCodeFailed(new Exception()));
        }
    }

    public void handleSmsRegistrationSuccess(BaseApiData<CredentialsData> reply) {
        if (reply.getCode() == ApiConfig.CODE_OK) {
            String clientId = reply.getData().getClientId();
            String password = reply.getData().getPassword();
            TaxiKzApp.storage.setClientId(clientId);
            TaxiKzApp.storage.setPassword(password);
            this.mBus.post(new CheckRegistrationUserSuccess());
            return;
        }
        this.mBus.post(new CheckRegistrationUserFailed());
    }

    public void handleSmsRegistrationError(Throwable t, String error) {
        this.mBus.post(new CheckRegistrationUserFailed());
    }

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    public void logOut() {
        TaxiKzApp.storage.setPhone(BuildConfig.VERSION_NAME);
        TaxiKzApp.storage.setClientId(BuildConfig.VERSION_NAME);
        TaxiKzApp.storage.setPassword(BuildConfig.VERSION_NAME);
        TaxiKzApp.storage.setName(BuildConfig.VERSION_NAME);
    }
}
