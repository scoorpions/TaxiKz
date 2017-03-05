package kz.taxikz.ui.address.adapter.vh;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.widget.item.NewAddressLocal;

public class FavoriteItemViewHolder extends BaseViewHolder {
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.subtitle_textView)
    TextView mSubtitleTextView;
    @BindView(R.id.title_textView)
    TextView mTitleTextView;

    public interface Callbacks {
        void onClick();
    }

    public FavoriteItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(NewAddressLocal address, Callbacks callbacks) {
        AddressData addressData = address.getAutocompleteAddressData();
        if (addressData != null) {
            this.mTitleTextView.setText(addressData.getName());
            if (address.getUserHouse().isEmpty()) {
                this.mSubtitleTextView.setVisibility(View.GONE);
            } else {
                this.mSubtitleTextView.setVisibility(View.VISIBLE);
                if (address.getUserPorch().isEmpty()) {
                    this.mSubtitleTextView.setText(String.format(this.mSubtitleTextView.getContext().getString(R.string.order_address_details_template), address.getUserHouse()));
                } else {
                    this.mSubtitleTextView.setText(String.format(this.mSubtitleTextView.getContext().getString(R.string.order_address_details_with_porch_template), address.getUserHouse(), address.getUserPorch()));
                }
            }
            this.mMainLayout.setOnClickListener(v -> {
                if (callbacks != null) {
                    callbacks.onClick();
                }
            });
        }
    }
}
