package kz.taxikz.ui.main.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.taxi4.R;
import kz.taxikz.ui.main.adapter.vh.AddressViewHolder;
import kz.taxikz.ui.main.adapter.vh.AddressViewHolder.TYPE;
import kz.taxikz.ui.widget.item.NewAddressLocal;

public class AddressesAdapter extends Adapter<ViewHolder> {

    private boolean mActiveItems;
    private Callbacks mCallbacks;
    private List<NewAddressLocal> mItems;
    private boolean mRouteComplete;

    public interface Callbacks {
        void itemClicked(int position);

        void itemRemoveClicked(int position);
    }

    private class CallbacksImpl implements AddressViewHolder.Callbacks {
        final int position;

        CallbacksImpl(int position) {
            this.position = position;
        }

        @Override
        public void onClick() {
            mCallbacks.itemClicked(position);
        }

        @Override
        public void onRemoveClick() {
            mCallbacks.itemRemoveClicked(position);
        }
    }

    public AddressesAdapter(List<NewAddressLocal> items, Callbacks callbacks) {
        mItems = items;
        mCallbacks = callbacks;
        mActiveItems = false;
        mRouteComplete = false;
    }

    public void setActiveItems(int excludeIndex, boolean value, boolean notify) {
        mActiveItems = value;
        int i = 0;
        while (i < getItemCount()) {
            if (notify && (excludeIndex == -1 || excludeIndex != i)) {
                notifyItemChanged(i);
            }
            i++;
        }
    }

    public void setRouteComplete(boolean value) {
        mRouteComplete = value;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_address_vh, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TYPE holderType;
        AddressViewHolder addressViewHolder = (AddressViewHolder) holder;
        NewAddressLocal addressLocal = this.mItems.get(position);
        if (position == 0) {
            holderType = TYPE.START;
        } else if (position == this.mItems.size() - 1) {
            holderType = TYPE.END;
        } else {
            holderType = TYPE.STAY;
        }
        addressViewHolder.bind(addressLocal, holderType, mActiveItems, mRouteComplete,  new CallbacksImpl(position));
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
