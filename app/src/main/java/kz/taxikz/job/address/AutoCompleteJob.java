package kz.taxikz.job.address;

import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.address.AutoCompleteController;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.data.api.pojo.BaseApiData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AutoCompleteJob extends BaseAutoCompleteJob {
    private AutoCompleteController mAutoCompleteController;
    private String mInput;

    public AutoCompleteJob(AutoCompleteController autoCompleteController, String input) {
        this.mInput = input;
        this.mAutoCompleteController = autoCompleteController;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.d("onCancel()%s", getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.e("AutoCompleteJob onRun()");
        this.mAutoCompleteService.getAddresses(TaxiKzApp.storage.getCityId(), this.mInput).enqueue(new Callback<BaseApiData<List<AddressData>>>() {
            @Override
            public void onResponse(Call<BaseApiData<List<AddressData>>> call, Response<BaseApiData<List<AddressData>>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    AutoCompleteJob.this.mAutoCompleteController.handleAutoCompleteSuccess(response.body(), AutoCompleteJob.this.mInput);
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<List<AddressData>>> call, Throwable t) {
                Timber.e("onFailure() %s", t);
                AutoCompleteJob.this.mAutoCompleteController.handleAutoCompleteFailed(t);
            }
        });
    }
}
