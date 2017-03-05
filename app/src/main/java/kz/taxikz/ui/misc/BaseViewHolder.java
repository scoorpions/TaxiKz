package kz.taxikz.ui.misc;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import butterknife.ButterKnife;

public class BaseViewHolder extends ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
