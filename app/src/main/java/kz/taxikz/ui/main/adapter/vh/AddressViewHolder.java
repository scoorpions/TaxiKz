package kz.taxikz.ui.main.adapter.vh;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.util.UiUtils;
import kz.taxikz.ui.widget.item.NewAddressLocal;

public class AddressViewHolder extends BaseViewHolder {
    @BindView(R.id.address_details_textView)
    TextView mAddressDetailsTextView;
    @BindView(R.id.address_layout)
    LinearLayout mAddressLayout;
    @BindView(R.id.address_textView)
    TextView mAddressTextView;
    @BindView(R.id.divider)
    View mDividerView;
    @BindView(R.id.icon_bottom_line_view)
    View mIconBottomLineView;
    @BindView(R.id.icon_imageView)
    ImageView mIconImageView;
    @BindView(R.id.icon_top_line_view)
    View mIconTopLineView;
    @BindView(R.id.remove_imageView)
    ImageView mRemoveImageView;

    private NewAddressLocal mItemAddress;
    private Callbacks mItemCallbacks;
    private TYPE mItemType;

    public interface Callbacks {
        void onClick();

        void onRemoveClick();
    }

    public enum TYPE {
        START,
        STAY,
        END
    }

    public AddressViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(NewAddressLocal address, TYPE type, boolean isActive, boolean routeComplete, Callbacks callbacks) {
        this.mItemAddress = address;
        this.mItemType = type;
        this.mItemCallbacks = callbacks;
        this.mAddressTextView.setVisibility(View.VISIBLE);
        this.mAddressDetailsTextView.setVisibility(View.GONE);
        this.mRemoveImageView.setOnClickListener(v -> {
            if(mItemCallbacks != null){
                mItemCallbacks.onRemoveClick();
            }
        });
        if (type == TYPE.START) {
            this.mIconImageView.setImageResource(R.mipmap.start_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.avh_start_end_icon_size, R.dimen.avh_start_end_icon_size);
            this.mIconTopLineView.setVisibility(View.INVISIBLE);
            this.mRemoveImageView.setVisibility(View.GONE);
            if (isActive) {
                this.mDividerView.setVisibility(View.INVISIBLE);
                this.mIconBottomLineView.setVisibility(View.INVISIBLE);
            } else {
                this.mDividerView.setVisibility(View.VISIBLE);
                this.mIconBottomLineView.setVisibility(View.VISIBLE);
            }
            if (address.isEmpty()) {
                this.mAddressTextView.setTextColor(ContextCompat.getColor(this.mAddressTextView.getContext(), R.color.secondaryText));
                this.mAddressTextView.setText(this.mAddressTextView.getContext().getString(R.string.order_address_start_address_placeholder));
            }
        } else if (type == TYPE.END) {
            this.mIconImageView.setImageResource(R.mipmap.end_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.avh_start_end_icon_size, R.dimen.avh_start_end_icon_size);
            this.mIconBottomLineView.setVisibility(View.INVISIBLE);
            this.mDividerView.setVisibility(View.INVISIBLE);
            this.mRemoveImageView.setVisibility(View.GONE);
            if (isActive) {
                this.mIconTopLineView.setVisibility(View.INVISIBLE);
            } else {
                this.mIconTopLineView.setVisibility(View.VISIBLE);
            }
            if (address.isEmpty()) {
                this.mAddressTextView.setTextColor(ContextCompat.getColor(this.mAddressTextView.getContext(), R.color.secondaryText));
                this.mAddressTextView.setText(this.mAddressTextView.getContext().getString(R.string.order_address_end_address_placeholder));
            }
        } else {
            this.mIconImageView.setImageResource(R.mipmap.medium_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.avh_stay_icon_size, R.dimen.avh_stay_icon_size);
            if (isActive) {
                this.mRemoveImageView.setVisibility(View.INVISIBLE);
                this.mDividerView.setVisibility(View.INVISIBLE);
                this.mIconTopLineView.setVisibility(View.INVISIBLE);
                this.mIconBottomLineView.setVisibility(View.INVISIBLE);
            } else {
                this.mRemoveImageView.setVisibility(View.VISIBLE);
                this.mDividerView.setVisibility(View.VISIBLE);
                this.mIconTopLineView.setVisibility(View.VISIBLE);
                this.mIconBottomLineView.setVisibility(View.VISIBLE);
            }
            if (address.isEmpty()) {
                this.mAddressTextView.setTextColor(ContextCompat.getColor(this.mAddressTextView.getContext(), R.color.secondaryText));
                this.mAddressTextView.setText(this.mAddressTextView.getContext().getString(R.string.order_address_stay_address_placeholder));
            }
        }
        if (address.getUserAddress().isEmpty()) {
            this.mAddressTextView.setTextColor(ContextCompat.getColor(this.mAddressTextView.getContext(), R.color.secondaryText));
            this.mIconImageView.setColorFilter(ContextCompat.getColor(this.mIconImageView.getContext(), R.color.iconsLight));
        } else {
            this.mAddressTextView.setTextColor(ContextCompat.getColor(this.mAddressTextView.getContext(), R.color.primaryText));
            this.mIconImageView.setColorFilter(ContextCompat.getColor(this.mIconImageView.getContext(), R.color.icons));
            String addressStr = address.getUserAddress();
            if (addressStr.charAt(addressStr.length() - 1) == ',') {
                addressStr = addressStr.substring(0, addressStr.length() - 1);
            }
            this.mAddressTextView.setText(addressStr);
            if (!address.getUserHouse().isEmpty()) {
                this.mAddressDetailsTextView.setVisibility(View.VISIBLE);
                if (address.getUserPorch().isEmpty()) {
                    this.mAddressDetailsTextView.setText(String.format(this.mAddressDetailsTextView.getContext().getString(R.string.order_address_details_template), address.getUserHouse()));
                } else {
                    this.mAddressDetailsTextView.setText(String.format(this.mAddressDetailsTextView.getContext().getString(R.string.order_address_details_with_porch_template), address.getUserHouse(), address.getUserPorch()));
                }
            }
        }
        if (routeComplete) {
            this.mIconTopLineView.setBackgroundColor(ContextCompat.getColor(this.mIconImageView.getContext(), R.color.primaryText));
            this.mIconBottomLineView.setBackgroundColor(ContextCompat.getColor(this.mIconImageView.getContext(), R.color.primaryText));
        } else {
            this.mIconTopLineView.setBackgroundColor(ContextCompat.getColor(this.mIconImageView.getContext(), R.color.secondaryText));
            this.mIconBottomLineView.setBackgroundColor(ContextCompat.getColor(this.mIconImageView.getContext(), R.color.secondaryText));
        }
        if (callbacks != null) {
            this.mAddressLayout.setOnClickListener(v -> {
                if(mItemCallbacks != null){
                    mItemCallbacks.onClick();
                }
            });
        }
    }

    public void setActive() {
        bind(this.mItemAddress, this.mItemType, true, false, this.mItemCallbacks);
    }
}
