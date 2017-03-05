package kz.taxikz.ui.orders.adapters;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.data.api.pojo.Order;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.orders.adapters.vh.OrderViewHolder;

public class OrdersAdapter extends Adapter<OrderViewHolder> {
    private Callbacks mCallbacks;
    private List<Order> mItems;

    public interface Callbacks {
        void onCancelOrder(Order order);
    }

    public OrdersAdapter(List<Order> items, Callbacks callbacks) {
        this.mItems = items;
        this.mCallbacks = callbacks;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_orders_order_viewholder, parent, false), new OrderViewHolder.Callbacks() {
            @Override
            public void onCancelClick(Order order) {
                if (OrdersAdapter.this.mCallbacks != null) {
                    OrdersAdapter.this.mCallbacks.onCancelOrder(order);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.bind(this.mItems.get(position));
    }

    @Override
    public void onViewDetachedFromWindow(OrderViewHolder holder) {
        holder.stopTimer();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }
}
