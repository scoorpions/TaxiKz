package kz.taxikz.job.auth;

import android.util.Log;

import java.util.List;

import kz.taxikz.controllers.auth.AccountController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.ui.widget.view.maskedEditText.MaskedEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SmsCodeJob extends BaseAuthJob {
    private final AccountController mAccountController;
    private String mKey;
    private String mPhone;

    public SmsCodeJob(AccountController mAccountController, String phone, String key) {
        Log.e("!", "SmsCodeJob: " + phone + MaskedEditText.SPACE + key);
        this.mAccountController = mAccountController;
        this.mPhone = phone;
        this.mKey = key;
    }

    @Override
    protected int getRetryLimit() {
        return 1;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getTimeoutException());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.mAuthService.requestSms(this.mPhone, this.mKey).enqueue(new Callback<BaseApiData<List<String>>>() {
            @Override
            public void onResponse(Call<BaseApiData<List<String>>> call, Response<BaseApiData<List<String>>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    SmsCodeJob.this.mAccountController.handleSmsCodeSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<List<String>>> call, Throwable t) {
                Timber.e("onFailure() %s", t.getLocalizedMessage());
                mAccountController.handleSmsCodeError(t, "sms code error");
            }
        });
    }
}
