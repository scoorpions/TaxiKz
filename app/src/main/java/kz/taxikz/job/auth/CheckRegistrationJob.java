package kz.taxikz.job.auth;

import kz.taxikz.controllers.auth.AccountController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CredentialsData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CheckRegistrationJob extends BaseAuthJob {
    private AccountController mAccountController;
    private String mKey;
    private String mRejectedPhone;

    public CheckRegistrationJob(AccountController mAccountController, String key, String rejectedPhone) {
        this.mAccountController = mAccountController;
        this.mKey = key;
        this.mRejectedPhone = rejectedPhone;
    }

    @Override
    protected int getRetryLimit() {
        return 1;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.mAuthService.checkRegistration(this.mKey, this.mRejectedPhone).enqueue(new Callback<BaseApiData<CredentialsData>>() {
            @Override
            public void onResponse(Call<BaseApiData<CredentialsData>> call, Response<BaseApiData<CredentialsData>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    CheckRegistrationJob.this.mAccountController.handleCheckRegistrationSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<CredentialsData>> call, Throwable t) {
                Timber.e("onFailure() %s", t.getLocalizedMessage());
                CheckRegistrationJob.this.mAccountController.handleCheckRegistrationError(t, "activate error");
            }
        });
    }
}
