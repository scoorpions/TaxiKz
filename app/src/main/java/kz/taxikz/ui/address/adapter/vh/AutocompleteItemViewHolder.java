package kz.taxikz.ui.address.adapter.vh;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import kz.taxikz.data.api.pojo.Address;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.widget.view.maskedEditText.MaskedEditText;

public class AutocompleteItemViewHolder extends BaseViewHolder {
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.subtitle_textView)
    TextView mSubtitleTextView;
    @BindView(R.id.title_textView)
    TextView mTitleTextView;

    public interface Callbacks {
        void onClick();
    }

    public AutocompleteItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(AddressData addressData, Callbacks callbacks) {
        Address address = addressData.getAddress();
        this.mTitleTextView.setText(addressData.getName());
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(address.getStreetName());
            sb.append(MaskedEditText.SPACE);
            sb.append(address.getHouseId());
            this.mSubtitleTextView.setVisibility(View.VISIBLE);
            this.mSubtitleTextView.setText(sb.toString());
        } else if (addressData.getType().equals(AddressData.TYPE_STREET)) {
            this.mSubtitleTextView.setVisibility(View.VISIBLE);
            this.mSubtitleTextView.setText(this.mSubtitleTextView.getContext().getString(R.string.address_fragment_autocomplete_street));
        } else if (addressData.getType().equals(AddressData.TYPE_POINT)) {
            this.mSubtitleTextView.setVisibility(View.VISIBLE);
            this.mSubtitleTextView.setText(this.mSubtitleTextView.getContext().getString(R.string.address_fragment_autocomplete_point));
        } else {
            this.mSubtitleTextView.setVisibility(View.GONE);
        }

        mMainLayout.setOnClickListener(v -> {
            if (callbacks != null) {
                callbacks.onClick();
            }
        });
    }

}
