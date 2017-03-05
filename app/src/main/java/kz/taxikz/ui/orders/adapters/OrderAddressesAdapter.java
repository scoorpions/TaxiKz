package kz.taxikz.ui.orders.adapters;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.taxi4.R;
import kz.taxikz.ui.orders.adapters.vh.OrderAddressViewHolder;
import kz.taxikz.ui.orders.adapters.vh.OrderAddressViewHolder.TYPE;

public class OrderAddressesAdapter extends Adapter<OrderAddressViewHolder> {
    private List<String> mItems;

    public OrderAddressesAdapter(List<String> items) {
        this.mItems = items;
    }

    public OrderAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_orders_order_address_viewholder, parent, false));
    }

    public void onBindViewHolder(OrderAddressViewHolder holder, int position) {
        TYPE holderType;
        String addressStr = (String) this.mItems.get(position);
        if (position == 0) {
            if (this.mItems.size() == 1) {
                holderType = TYPE.ONE_ADDRESS;
            } else {
                holderType = TYPE.START;
            }
        } else if (position == this.mItems.size() - 1) {
            holderType = TYPE.END;
        } else {
            holderType = TYPE.STAY;
        }
        holder.bind(addressStr, holderType);
    }

    public int getItemViewType(int position) {
        return 1;
    }

    public int getItemCount() {
        return this.mItems.size();
    }
}
