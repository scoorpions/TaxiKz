package kz.taxikz.job.order;

import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OrdersGetJob extends BaseOrderJob {
    private OrderController mOrderController;

    public OrdersGetJob(OrderController mOrderController) {
        this.mOrderController = mOrderController;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mOrderController.handleGetOrdersError(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mOrderController.handleGetOrdersError(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.orderApiService.getCurrentOrders(TaxiKzApp.storage.getCliendId()).enqueue(new Callback<BaseApiData<List<Order>>>() {
            @Override
            public void onResponse(Call<BaseApiData<List<Order>>> call, Response<BaseApiData<List<Order>>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    OrdersGetJob.this.mOrderController.handleGetOrdersSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<List<Order>>> call, Throwable t) {
                OrdersGetJob.this.mOrderController.handleGetOrdersError(t);
                Timber.e("onFailure() %s", t);
            }
        });
    }
}
