package kz.taxikz.controllers.order;

import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.order.OrderEvents.CreateOrderFailed;
import kz.taxikz.controllers.order.OrderEvents.CreateOrderSuccess;
import kz.taxikz.controllers.order.OrderEvents.GetOrdersSuccess;
import kz.taxikz.controllers.order.OrderEvents.OrderCancelFailed;
import kz.taxikz.controllers.order.OrderEvents.OrderCancelSuccess;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CreateOrderDataItem;
import kz.taxikz.data.api.pojo.CreateOrderParam;
import kz.taxikz.data.api.pojo.Order;
import kz.taxikz.job.order.OrderCancelJob;
import kz.taxikz.job.order.OrderCreateJob;
import kz.taxikz.job.order.OrdersGetJob;
import kz.taxikz.utils.Preconditions;

public class OrderController extends BaseController {
    public static final String TAG = "OrderController";
    private final Bus mBus;
    private CreateOrderParam mCreateOrderParam;
    private final JobManager mJobManager;
    private String mOrderId;

    protected void onInject() {
        TaxiKzApp.get().component().inject((BaseController) this);
    }

    public OrderController(Bus bus, JobManager jobManager) {
        this.mJobManager = jobManager;
        this.mBus = bus;
    }

    public void getOrders() {
        this.mJobManager.addJobInBackground(new OrdersGetJob(this));
    }

    public void handleGetOrdersSuccess(BaseApiData<List<Order>> reply) {
        Preconditions.checkNotNull(reply);
        if (reply.getCode() == ApiConfig.CODE_OK) {
            List<Order> ordersList = (List) reply.getData();
            if (ordersList != null) {
                if (ordersList.size() > 0) {
                    int bonusSum = 0;
                    for (Order order : ordersList) {
                        if (order.getBonusSum() > 0) {
                            bonusSum += order.getBonusSum();
                        }
                    }
                    TaxiKzApp.storage.setOrderBonus(bonusSum);
                } else {
                    TaxiKzApp.storage.setOrderBonus(0);
                }
            }
            this.mBus.post(new GetOrdersSuccess((List) reply.getData()));
        }
    }

    public void handleGetOrdersError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            getOrders();
        }
    }

    public void createOrder(CreateOrderParam createOrderParam) {
        Preconditions.checkNotNull(createOrderParam);
        this.mCreateOrderParam = createOrderParam;
        this.mJobManager.addJobInBackground(new OrderCreateJob(this, createOrderParam));
    }

    public void handleCreateOrderSuccess(BaseApiData<List<CreateOrderDataItem>> reply) {
        Log.e(TAG, "handleCreateOrderSuccess: " + reply.getCode());
        Preconditions.checkNotNull(reply);
        if (reply.getCode() == ApiConfig.CODE_OK) {
            TaxiKzApp.storage.setOrderId(((CreateOrderDataItem) ((List) reply.getData()).get(0)).getData().getOrderId());
            this.mBus.post(new CreateOrderSuccess());
            getOrders();
            return;
        }
        this.mBus.post(new CreateOrderFailed());
    }

    public void handleCreateOrderError(Throwable throwable) {
        Log.e(TAG, "handleCreateOrderError: " + throwable.toString());
        if (throwable instanceof SocketTimeoutException) {
            createOrder(this.mCreateOrderParam);
        } else {
            this.mBus.post(new CreateOrderFailed());
        }
    }

    public void cancelOrder(String orderId) {
        Preconditions.checkNotNull(orderId);
        this.mOrderId = orderId;
        this.mJobManager.addJobInBackground(new OrderCancelJob(this, orderId));
    }

    public void handleCancelOrderSuccess(BaseApiData<String> reply) {
        Preconditions.checkNotNull(reply);
        if (reply.getCode() == ApiConfig.CODE_OK) {
            this.mBus.post(new OrderCancelSuccess());
        } else {
            this.mBus.post(new OrderCancelFailed());
        }
    }

    public void handleCancelOrderFailure(Throwable t) {
        this.mBus.post(new OrderCancelFailed());
    }
}
