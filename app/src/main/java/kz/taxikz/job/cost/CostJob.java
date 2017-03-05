package kz.taxikz.job.cost;

import java.util.ArrayList;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.cost.CostController;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CostData;
import kz.taxikz.ui.widget.item.NewAddressLocal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CostJob extends BaseCostJob {
    private CostController mCostController;
    private List<String> mFormatAddresses;
    private List<Integer> mOrderDetails;

    public CostJob(CostController costController, List<NewAddressLocal> addressList, List<Integer> orderDetails) {
        this.mCostController = costController;
        this.mFormatAddresses = formatAddresses(addressList);
        this.mOrderDetails = orderDetails;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mCostController.handleFetchPriceFailed(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mCostController.handleFetchPriceFailed(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.costService.cost(this.mFormatAddresses,
                this.mOrderDetails,
                TaxiKzApp.storage.getCityId()).enqueue(new Callback<BaseApiData<CostData>>() {
            @Override
            public void onResponse(Call<BaseApiData<CostData>> call, Response<BaseApiData<CostData>> response) {
                if (response != null && response.isSuccessful()) {
                    CostJob.this.mCostController.handleFetchPriceSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<CostData>> call, Throwable t) {
                Timber.e("onError() %s", t.getLocalizedMessage());
            }
        });
    }

    private List<String> formatAddresses(List<NewAddressLocal> addressList) {
        List<String> formattedList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addressList.size(); i++) {
            NewAddressLocal address = addressList.get(i);
            sb.append(address.getAutocompleteAddressData().getId());
            if (address.getAutocompleteAddressData().getType().equals(AddressData.TYPE_STREET)) {
                sb.append(ApiConfig.ADDRESS_SEPARATOR);
                sb.append(address.getUserHouse());
            }
            formattedList.add(sb.toString());
            sb.setLength(0);
        }
        return formattedList;
    }
}
