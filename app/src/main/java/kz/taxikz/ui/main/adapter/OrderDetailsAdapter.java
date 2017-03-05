package kz.taxikz.ui.main.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.taxi4.R;
import kz.taxikz.ui.main.adapter.vh.OrderDetailsViewHolder;
import kz.taxikz.ui.widget.item.Note;

public class OrderDetailsAdapter extends Adapter<ViewHolder> {
    private List<Note> mItems;

    public OrderDetailsAdapter(List<Note> items) {
        this.mItems = items;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((OrderDetailsViewHolder) holder).bind(this.mItems.get(position), () -> {
            Note item = this.mItems.get(position);
            item.toggle();
            this.mItems.set(position, item);
            if (checkOptions()) {
                notifyDataSetChanged();
            }
        });
    }

    public int getItemViewType(int position) {
        return 1;
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    private boolean checkOptions() {
        boolean isDataChanged = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            Note note = this.mItems.get(i);
            if (note.isSelected()) {
                boolean found = false;
                for (int j = 0; j < this.mItems.size(); j++) {
                    Note secondNote = this.mItems.get(j);
                    if (i != j && note.getOptionId() == secondNote.getOptionId() && secondNote.isSelected()) {
                        found = true;
                        secondNote.toggle();
                        this.mItems.set(j, secondNote);
                        isDataChanged = true;
                    }
                }
                if (found) {
                    note.toggle();
                    this.mItems.set(i, note);
                    isDataChanged = true;
                }
            }
        }
        return isDataChanged;
    }
}
