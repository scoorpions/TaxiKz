package kz.taxikz.job.auth;

import java.net.SocketTimeoutException;

import kz.taxikz.controllers.auth.AccountController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.RegistrationData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RegistrationJob extends BaseAuthJob {
    private AccountController mAccountController;
    private final String mName;
    private final String mPhone;

    public RegistrationJob(AccountController mAccountController, String name, String phone) {
        this.mAccountController = mAccountController;
        this.mName = name;
        this.mPhone = phone;
    }

    @Override
    protected int getRetryLimit() {
        return 1;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mAccountController.handleRegisterUserError(getThrowable(), "CheckNumValid failed");
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mAccountController.handleRegisterUserError(throwable, "CheckNumValid failed");
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.mAuthService.registration(this.mPhone, this.mName).enqueue(new Callback<BaseApiData<RegistrationData>>() {
            @Override
            public void onResponse(Call<BaseApiData<RegistrationData>> call, Response<BaseApiData<RegistrationData>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", ((BaseApiData) response.body()).toString());
                    RegistrationJob.this.mAccountController.handleRegisterUserSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<RegistrationData>> call, Throwable t) {
                Timber.e("onFailure() %s", t.getLocalizedMessage());
                if (t instanceof SocketTimeoutException) {
                    RegistrationJob.this.mAccountController.handleRegisterUserError(t, "CheckNumValid failed");
                }
            }
        });
    }
}
