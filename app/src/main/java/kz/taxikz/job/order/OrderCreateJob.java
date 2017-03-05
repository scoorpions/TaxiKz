package kz.taxikz.job.order;

import java.util.List;

import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CreateOrderDataItem;
import kz.taxikz.data.api.pojo.CreateOrderParam;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OrderCreateJob extends BaseOrderJob {

    private CreateOrderParam mCreateOrderParam;
    private OrderController mOrderController;

    public OrderCreateJob(OrderController orderController, CreateOrderParam createOrderParam) {
        this.mOrderController = orderController;
        this.mCreateOrderParam = createOrderParam;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mOrderController.handleCreateOrderError(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mOrderController.handleCreateOrderError(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.orderApiService.newOrder(this.mCreateOrderParam.getPhone(),
                this.mCreateOrderParam.getAutodialPhone(),
                this.mCreateOrderParam.getClientId(),
                this.mCreateOrderParam.getCustomer(),
                this.mCreateOrderParam.getAddresses(),
                this.mCreateOrderParam.getCityId(),
                this.mCreateOrderParam.getPorch(),
                this.mCreateOrderParam.getComment(),
                this.mCreateOrderParam.getOrderParams(),
                this.mCreateOrderParam.getTotalCost(),
                this.mCreateOrderParam.getBonusSum()).enqueue(new Callback<BaseApiData<List<CreateOrderDataItem>>>() {
            @Override
            public void onResponse(Call<BaseApiData<List<CreateOrderDataItem>>> call, Response<BaseApiData<List<CreateOrderDataItem>>> response) {
                if (response != null && response.isSuccessful()) {
                    OrderCreateJob.this.mOrderController.handleCreateOrderSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<List<CreateOrderDataItem>>> call, Throwable t) {
                OrderCreateJob.this.mOrderController.handleCreateOrderError(t);
            }
        });
    }
}
