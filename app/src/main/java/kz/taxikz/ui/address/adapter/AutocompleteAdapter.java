package kz.taxikz.ui.address.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.address.adapter.vh.AutocompleteItemViewHolder;

public class AutocompleteAdapter extends Adapter<ViewHolder> {
    private Callbacks mCallbacks;
    private List<AddressData> mItems;

    public interface Callbacks {
        void itemClick(int i);
    }

    public AutocompleteAdapter(List<AddressData> items, Callbacks callbacks) {
        this.mItems = items;
        this.mCallbacks = callbacks;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AutocompleteItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.autpcomplete_address_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((AutocompleteItemViewHolder) holder).bind(this.mItems.get(position), () -> {
            if (this.mCallbacks != null) {
                this.mCallbacks.itemClick(position);
            }
        });
    }

    public int getItemViewType(int position) {
        return 1;
    }

    public int getItemCount() {
        return this.mItems.size();
    }
}
