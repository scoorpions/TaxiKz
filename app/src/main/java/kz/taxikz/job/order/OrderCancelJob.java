package kz.taxikz.job.order;

import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.data.api.pojo.BaseApiData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OrderCancelJob extends BaseOrderJob {
    private OrderController mOrderController;
    private String mOrderId;


    public OrderCancelJob(OrderController mOrderController, String orderId) {
        this.mOrderController = mOrderController;
        this.mOrderId = orderId;
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
        this.orderApiService.cancelOrder(this.mOrderId).enqueue(new Callback<BaseApiData<String>>() {
            @Override
            public void onResponse(Call<BaseApiData<String>> call, Response<BaseApiData<String>> response) {
                if (response != null && response.isSuccessful()) {
                    OrderCancelJob.this.mOrderController.handleCancelOrderSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<String>> call, Throwable t) {
                OrderCancelJob.this.mOrderController.handleCancelOrderFailure(t);
            }
        });
    }
}
