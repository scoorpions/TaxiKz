package kz.taxikz.ui.orders.adapters.vh;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.util.UiUtils;

public class OrderAddressViewHolder extends BaseViewHolder {
    @BindView(R.id.divider)
    View mDivider;
    @BindView(R.id.icon_bottom_line_view)
    View mIconBottomLineView;
    @BindView(R.id.icon_imageView)
    ImageView mIconImageView;
    @BindView(R.id.icon_top_line_view)
    View mIconTopLineView;
    @BindView(R.id.text_textView)
    TextView mTextTextView;

    public enum TYPE {
        ONE_ADDRESS,
        START,
        STAY,
        END
    }

    public OrderAddressViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(String address, TYPE type) {
        if (type == TYPE.ONE_ADDRESS) {
            this.mIconImageView.setImageResource(R.mipmap.start_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.of_address_item_start_end_icon_size, R.dimen.of_address_item_start_end_icon_size);
            this.mIconTopLineView.setVisibility(View.INVISIBLE);
            this.mIconBottomLineView.setVisibility(View.INVISIBLE);
            this.mDivider.setVisibility(View.INVISIBLE);
        } else if (type == TYPE.START) {
            this.mIconImageView.setImageResource(R.mipmap.start_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.of_address_item_start_end_icon_size, R.dimen.of_address_item_start_end_icon_size);
            this.mIconTopLineView.setVisibility(View.INVISIBLE);
            this.mIconBottomLineView.setVisibility(View.VISIBLE);
            this.mDivider.setVisibility(View.VISIBLE);
        } else if (type == TYPE.END) {
            this.mIconImageView.setImageResource(R.mipmap.end_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.of_address_item_start_end_icon_size, R.dimen.of_address_item_start_end_icon_size);
            this.mIconTopLineView.setVisibility(View.VISIBLE);
            this.mIconBottomLineView.setVisibility(View.INVISIBLE);
            this.mDivider.setVisibility(View.INVISIBLE);
        } else {
            this.mIconImageView.setImageResource(R.mipmap.medium_point);
            UiUtils.setImageViewSizeFromResource(this.mIconImageView, R.dimen.of_address_item_stay_icon_size, R.dimen.of_address_item_stay_icon_size);
            this.mIconTopLineView.setVisibility(View.VISIBLE);
            this.mIconBottomLineView.setVisibility(View.VISIBLE);
            this.mDivider.setVisibility(View.VISIBLE);
        }
        this.mTextTextView.setText(address);
    }
}
