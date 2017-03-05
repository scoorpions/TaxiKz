package kz.taxikz.ui.orders.fragment;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squareup.otto.Subscribe;
import com.wang.avi.indicators.LineScalePartyIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kz.taxikz.AppConfig.FabricEvents;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.controllers.order.OrderEvents.GetOrdersFailed;
import kz.taxikz.controllers.order.OrderEvents.GetOrdersSuccess;
import kz.taxikz.controllers.order.OrderEvents.OrderCancelFailed;
import kz.taxikz.controllers.order.OrderEvents.OrderCancelSuccess;
import kz.taxikz.data.api.pojo.Order;
import kz.taxikz.google.OrderGcmListenerService;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseFragment;
import kz.taxikz.ui.orders.adapters.OrdersAdapter;
import kz.taxikz.ui.widget.design.RecyclerViewEmptySupport;
import kz.taxikz.ui.widget.view.dialog.Dialogs;
import kz.taxikz.utils.NetworkStatusUtil;

public class OrdersFragment extends BaseFragment {

    @BindView(R.id.mainLayout)
    LinearLayout mMainLayout;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.no_orders_layout)
    LinearLayout mNoOrdersLayout;
    @BindView(R.id.orders_recyclerView)
    RecyclerViewEmptySupport mOrdersRecyclerView;

    private OrderController mOrderController;
    private ArrayList<Order> mOrderList;
    private OrdersAdapter mOrdersAdapter;

    private Snackbar mSnackbar;
    private BroadcastReceiver mStateChangedBroadcastReceiver;
    private Callbacks mCallbacks;


    public interface Callbacks {
        void lockScreen();

        void unlockScreen();
    }

    public static OrdersFragment newInstance(OrderController orderController) {
        OrdersFragment ordersFragment = new OrdersFragment();
        ordersFragment.setOrderController(orderController);
        return ordersFragment;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mStateChangedBroadcastReceiver, new IntentFilter(OrderGcmListenerService.COPA_RESULT));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mStateChangedBroadcastReceiver);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupServiceReceiver();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            onRefresh();
            mSwipeRefreshLayout.setRefreshing(false);
        });
//        this.mSwipeRefreshLayout.setColorSchemeColors(-7829368, -3355444, -7829368, -12303292);
        mOrderList = new ArrayList<>();
        mOrdersAdapter = new OrdersAdapter(this.mOrderList, order -> Dialogs.showCancelOrderDialog(getActivity(), (dialog, which) -> {
            mOrderList.remove(order);
            cancelOrder(order);
            Answers.getInstance().logCustom(new CustomEvent(FabricEvents.CANCEL_ORDER_OK));
        }));
        mOrdersRecyclerView.setAdapter(mOrdersAdapter);
        mOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mOrdersRecyclerView.setEmptyView(this.mNoOrdersLayout);
    }

    public void setupServiceReceiver() {
        mStateChangedBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ((NotificationManager) OrdersFragment.this.getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
                 onRefresh();
            }
        };
    }

    private void dismissLoadingDialog() {
        if (mCallbacks != null) {
            if (mNoOrdersLayout.getVisibility() != View.GONE) {
                mNoOrdersLayout.animate().alpha(LineScalePartyIndicator.SCALE).setDuration(250).start();
            }
            mCallbacks.unlockScreen();
        }
    }

    public void showSnackbar(int textRes) {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(mMainLayout, textRes, 0).setAction(R.string.ok, v -> mSnackbar.dismiss()).setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            ((TextView) mSnackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
        }
        mSnackbar.setText(textRes);
        mSnackbar.show();
    }

    public void onRefresh() {
        if (NetworkStatusUtil.getConnectivityStatus(getContext())) {
            if (this.mCallbacks != null) {
                if (this.mNoOrdersLayout.getVisibility() != View.GONE) {
                    this.mNoOrdersLayout.animate().alpha(0.0f).setDuration(250).start();
                }
                this.mCallbacks.lockScreen();
            }
            this.mOrderController.getOrders();
        }
    }

    @Subscribe
    public void onOrderCancelSuccessListener(OrderCancelSuccess event) {
        dismissLoadingDialog();
        mOrdersAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onOrderCancelFailedListener(OrderCancelFailed event) {
        dismissLoadingDialog();
        onRefresh();
    }

    @Subscribe
    public void onOrderSuccessListener(GetOrdersSuccess success) {
        List<Order> ordersList = success.getOrdersList();
        mOrderList.clear();
        if (ordersList == null || ordersList.size() == 0) {
            TaxiKzApp.storage.setOrderBonus(0);
        } else {
            mOrderList.addAll(ordersList);
        }
        if (mOrderList.isEmpty()) {
            ((NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
        }
        mOrdersAdapter.notifyDataSetChanged();
        dismissLoadingDialog();
    }

    @Subscribe
    public void onOrderFailedListener(GetOrdersFailed failed) {
        dismissLoadingDialog();
        showSnackbar(R.string.orders_refresh_failed_snackbar_error);
    }

    public void setOrderController(OrderController orderController) {
        mOrderController = orderController;
    }

    private void cancelOrder(Order order) {
        if (order.getBonusSum() > 0) {
            TaxiKzApp.storage.setOrderBonus(0);
        }
        if (mCallbacks != null) {
            if (mNoOrdersLayout.getVisibility() != View.GONE) {
                mNoOrdersLayout.animate().alpha(0.0f).setDuration(250).start();
            }
            mCallbacks.lockScreen();
        }
        mOrderController.cancelOrder(String.valueOf(order.getId()));
    }
}
