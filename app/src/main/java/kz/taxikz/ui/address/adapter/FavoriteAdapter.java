package kz.taxikz.ui.address.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.taxi4.R;
import kz.taxikz.ui.address.adapter.vh.FavoriteItemViewHolder;
import kz.taxikz.ui.widget.item.NewAddressLocal;

public class FavoriteAdapter extends Adapter<ViewHolder> {
    private Callbacks mCallbacks;
    private List<NewAddressLocal> mItems;

    public interface Callbacks {
        void onAddressClick(int i);
    }

    public FavoriteAdapter(List<NewAddressLocal> items, Callbacks callbacks) {
        this.mCallbacks = callbacks;
        this.mItems = items;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_address_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((FavoriteItemViewHolder) holder).bind(this.mItems.get(position), () ->  {
            if (this.mCallbacks != null) {
                this.mCallbacks.onAddressClick(position);
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
