package kz.taxikz.ui.orders.adapters.vh;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineScalePartyIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.Order;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.orders.adapters.OrderAddressesAdapter;
import kz.taxikz.utils.Utils;

public class OrderViewHolder extends BaseViewHolder {
    private ArrayList<String> mAddresses;
    @BindView(R.id.addresses_recyclerView)
    RecyclerView mAddressesRecyclerView;
    private Callbacks mCallbacks;
    @BindView(R.id.cancel_imageView)
    ImageView mCancelOrderImageView;
    @BindView(R.id.driver_info_car_icon_imageView)
    ImageView mDriverInfoCarIconImageView;
    @BindView(R.id.driver_info_car_textView)
    TextView mDriverInfoCarTextView;
    @BindView(R.id.driver_info_layout)
    LinearLayout mDriverInfoLayout;
    @BindView(R.id.driver_info_phone_icon_imageView)
    ImageView mDriverInfoPhoneIconImageView;
    @BindView(R.id.driver_info_phone_textView)
    TextView mDriverInfoPhoneTextView;
    @BindView(R.id.last_divider_view)
    View mLastDividerView;
    @BindView(R.id.loading_view)
    AVLoadingIndicatorView mLoadingView;
    @BindView(R.id.no_price_layout)
    LinearLayout mNoPriceLayout;
    private OrderAddressesAdapter mOrderAddressesAdapter;
    @BindView(R.id.order_status_textView)
    TextView mOrderStatusTextView;
    @BindView(R.id.order_timer_textView)
    TextView mOrderTimerTextView;
    @BindView(R.id.order_title_layout)
    RelativeLayout mOrderTitleLayout;
    @BindView(R.id.text_price_textView)
    TextView mPriceTextView;
    @BindView(R.id.price_with_bonus_bonus_textView)
    TextView mPriceWithBonusBonusTextView;
    @BindView(R.id.price_with_bonus_final_price_textView)
    TextView mPriceWithBonusFinalPriceTextView;
    @BindView(R.id.price_with_bonus_layout)
    LinearLayout mPriceWithBonusLayout;
    @BindView(R.id.price_with_bonus_price_textView)
    TextView mPriceWithBonusPriceTextView;
    private CountDownTimer mWaitDriverTimer;

    /* renamed from: kz.taxikz.ui.orders.adapters.vh.OrderViewHolder.1 */
    class C05321 extends CountDownTimer {
        C05321(long x0, long x1) {
            super(x0, x1);
        }

        public void onTick(long millisUntilFinished) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
            TextView textView = OrderViewHolder.this.mOrderTimerTextView;
            String string = OrderViewHolder.this.mOrderStatusTextView.getContext().getString(R.string.orders_card_wait_driver_title_timer);
            Object[] objArr = new Object[2];
            objArr[0] = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
            objArr[1] = seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds);
            textView.setText(String.format(string, objArr));
        }

        public void onFinish() {
            OrderViewHolder.this.stateDriverDelayed();
        }
    }

    public interface Callbacks {
        void onCancelClick(Order order);
    }

    public OrderViewHolder(View itemView, Callbacks callbacks) {
        super(itemView);
        this.mCallbacks = callbacks;
        this.mWaitDriverTimer = null;
    }

    public void bind(Order order) {
        Log.e("!", "bind: " + order.getState());
        Context context = this.mCancelOrderImageView.getContext();
        this.mCancelOrderImageView.setOnClickListener(v -> {
            if (this.mCallbacks != null) {
                this.mCallbacks.onCancelClick(order);
            }
        });
        this.mAddresses = new ArrayList<>();
        this.mAddresses.add(order.getSource());
        if (order.getStops() != null) {
            this.mAddresses.addAll(order.getStops());
        }
        if (order.getDestination() != null) {
            this.mAddresses.add(order.getDestination());
        }
        this.mOrderAddressesAdapter = new OrderAddressesAdapter(this.mAddresses);
        this.mAddressesRecyclerView.setLayoutManager(new LinearLayoutManager(this.mAddressesRecyclerView.getContext()));
        this.mAddressesRecyclerView.setAdapter(this.mOrderAddressesAdapter);
        this.mDriverInfoPhoneTextView.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.DIAL");
            intent.setData(Uri.parse("tel:" + this.mDriverInfoPhoneTextView.getText().toString()));
            this.mDriverInfoPhoneTextView.getContext().startActivity(intent);
        });
        setOrderPrice(order.getSumm(), order.getBonusSum());
        if (Utils.containsInArray(ApiConfig.ORDER_SEARCH_STATES, order.getState())) {
            stateSearchCar();
        } else if (Utils.containsInArray(ApiConfig.ORDER_CLIENT_CONFIRMATION_STATES, order.getState())) {
            stateSearchCar();
            stateWaitClientConfirmation();
        } else if (Utils.containsInArray(ApiConfig.ORDER_WAIT_DRIVER_STATES, order.getState())) {
            setDriverInfo(context, order);
            stateWaitCar(order.getDriverSourceTime());
        } else if (Utils.containsInArray(ApiConfig.ORDER_DRIVER_ON_PLACE_STATES, order.getState())) {
            setDriverInfo(context, order);
            stateOnPlace();
        } else if (Utils.containsInArray(ApiConfig.ORDER_IN_WORK_STATES, order.getState())) {
            setDriverInfo(context, order);
            stateInWork();
        }
    }

    private void setOrderPrice(int price, int bonuses) {
        if (price == 0 && bonuses == 0) {
            this.mPriceTextView.setVisibility(View.GONE);
            this.mPriceWithBonusLayout.setVisibility(View.GONE);
            this.mNoPriceLayout.setVisibility(View.VISIBLE);
            return;
        }
        this.mNoPriceLayout.setVisibility(View.GONE);
        this.mPriceWithBonusLayout.setVisibility(View.GONE);
        if (bonuses == 0) {
            this.mPriceTextView.setVisibility(View.VISIBLE);
            this.mPriceTextView.setText(String.format(this.mPriceTextView.getContext().getString(R.string.newprice), new Object[]{Integer.valueOf(price)}));
            return;
        }
        this.mPriceTextView.setVisibility(View.GONE);
        this.mPriceWithBonusLayout.setVisibility(View.VISIBLE);
        this.mPriceWithBonusPriceTextView.setText(String.valueOf(price + bonuses));
        this.mPriceWithBonusBonusTextView.setText(String.valueOf(bonuses));
        this.mPriceWithBonusFinalPriceTextView.setText(String.format(this.mPriceTextView.getContext().getString(R.string.orders_card_tenge_price), new Object[]{Integer.valueOf(price)}));
    }

    private void setDriverInfo(Context context, Order order) {
        String carColor = order.getCarColor();
        String carMark = order.getCarMark();
        String carNumber = order.getCarNum();
        String driverPhone = order.getDriverPhone();
        if (carColor == null || carColor.isEmpty()) {
            carColor = context.getResources().getString(R.string.orders_card_unknown_value);
        } else {
            carColor = carColor.substring(0, 1).toUpperCase() + carColor.substring(1);
        }
        if (carMark == null || carMark.isEmpty()) {
            carMark = context.getResources().getString(R.string.orders_card_unknown_value);
        }
        if (carNumber == null || carNumber.isEmpty()) {
            carNumber = context.getResources().getString(R.string.orders_card_unknown_value);
        }
        if (driverPhone == null || driverPhone.isEmpty()) {
            driverPhone = context.getResources().getString(R.string.orders_card_unknown_value);
        }
        this.mDriverInfoCarTextView.setText(String.format(this.mDriverInfoCarTextView.getContext().getString(R.string.car_data), new Object[]{carColor, carMark, carNumber}));
        this.mDriverInfoPhoneTextView.setText(driverPhone);
    }

    private void stateSearchCar() {
        this.mLoadingView.setVisibility(View.VISIBLE);
        int color = ContextCompat.getColor(this.mLoadingView.getContext(), R.color.colorRed);
        this.mLoadingView.setIndicatorColor(color);
        this.mOrderStatusTextView.setTextColor(color);
        this.mOrderStatusTextView.setText(R.string.orders_card_search_car_title);
        this.mCancelOrderImageView.setVisibility(View.VISIBLE);
        this.mOrderTimerTextView.setVisibility(View.INVISIBLE);
        this.mDriverInfoLayout.setVisibility(View.GONE);
        this.mLastDividerView.setVisibility(View.GONE);
    }

    private void stateWaitClientConfirmation() {
        int color = ContextCompat.getColor(this.mLoadingView.getContext(), R.color.colorAccent);
        this.mLoadingView.setIndicatorColor(color);
        this.mOrderStatusTextView.setTextColor(color);
        this.mOrderStatusTextView.setText(R.string.orders_card_wait_client_confirmation_title);
    }

    private void stateWaitCar(String driverWillBeInPlace) {
        this.mLoadingView.setVisibility(View.VISIBLE);
        int color = ContextCompat.getColor(this.mLoadingView.getContext(), R.color.colorAccent);
        this.mLoadingView.setIndicatorColor(color);
        this.mOrderStatusTextView.setTextColor(color);
        this.mOrderTitleLayout.setBackgroundColor(ContextCompat.getColor(this.mLoadingView.getContext(), android.R.color.white));
        this.mOrderStatusTextView.setText(R.string.orders_card_wait_driver_title);
        this.mCancelOrderImageView.setVisibility(View.INVISIBLE);
        this.mOrderTimerTextView.setVisibility(View.VISIBLE);
        this.mDriverInfoLayout.setVisibility(View.VISIBLE);
        this.mLastDividerView.setVisibility(View.VISIBLE);
        setupTimer(driverWillBeInPlace);
        setDriverInfoColor(ContextCompat.getColor(this.mLoadingView.getContext(), R.color.icons), ContextCompat.getColor(this.mLoadingView.getContext(), R.color.iconsLight));
    }

    private void stateOnPlace() {
        this.mLoadingView.setVisibility(View.VISIBLE);
        int color = ContextCompat.getColor(this.mLoadingView.getContext(), R.color.colorGreen);
        this.mLoadingView.setIndicatorColor(color);
        this.mOrderStatusTextView.setTextColor(color);
        this.mOrderTitleLayout.setBackgroundColor(ContextCompat.getColor(this.mLoadingView.getContext(), android.R.color.white));
        this.mDriverInfoLayout.setVisibility(View.VISIBLE);
        this.mLastDividerView.setVisibility(View.VISIBLE);
        this.mOrderStatusTextView.setText(R.string.orders_card_driver_on_place_title);
        this.mCancelOrderImageView.setVisibility(View.INVISIBLE);
        this.mOrderTimerTextView.setVisibility(View.INVISIBLE);
        setDriverInfoColor(color, color);
    }

    private void stateInWork() {
        this.mLoadingView.setVisibility(View.GONE);
        int color = ContextCompat.getColor(this.mLoadingView.getContext(), R.color.icons);
        this.mOrderStatusTextView.setTextColor(color);
        this.mOrderTitleLayout.setBackgroundColor(ContextCompat.getColor(this.mLoadingView.getContext(), R.color.colorGreen));
        this.mOrderStatusTextView.setText(R.string.orders_card_in_work_title);
        this.mCancelOrderImageView.setVisibility(View.INVISIBLE);
        this.mOrderTimerTextView.setVisibility(View.INVISIBLE);
        setDriverInfoColor(color, ContextCompat.getColor(this.mLoadingView.getContext(), R.color.iconsLight));
    }

    private void setDriverInfoColor(int colorIcon, int colorText) {
        this.mDriverInfoCarIconImageView.setColorFilter(colorIcon);
        this.mDriverInfoCarTextView.setTextColor(colorText);
        this.mDriverInfoPhoneIconImageView.setColorFilter(colorIcon);
        this.mDriverInfoPhoneTextView.setTextColor(colorText);
        this.mDriverInfoPhoneTextView.setLinkTextColor(colorText);
    }

    private void setupTimer(String driverWillBeInPlace) {
        if (driverWillBeInPlace != null) {
            long remainMilliseconds = 0;
            try {
                remainMilliseconds = new SimpleDateFormat("yyyy-M-dd kk:mm:ss").parse(driverWillBeInPlace).getTime() - new Date().getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (remainMilliseconds <= 0) {
                stateDriverDelayed();
            } else {
                this.mWaitDriverTimer = new CountDownTimer(remainMilliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                        TextView textView = OrderViewHolder.this.mOrderTimerTextView;
                        String string = OrderViewHolder.this.mOrderStatusTextView.getContext().getString(R.string.orders_card_wait_driver_title_timer);
                        Object[] objArr = new Object[2];
                        objArr[0] = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
                        objArr[1] = seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds);
                        textView.setText(String.format(string, objArr));
                    }

                    @Override
                    public void onFinish() {
                        stateDriverDelayed();
                    }
                }.start();  //new C05321(remainMilliseconds, 1000).start();
            }
        }
    }

    private void stateDriverDelayed() {
        int color = ContextCompat.getColor(this.mOrderStatusTextView.getContext(), R.color.colorRed);
        this.mOrderStatusTextView.setText(R.string.orders_card_wait_driver_delayed_title);
        this.mOrderStatusTextView.setTextColor(color);
        this.mLoadingView.setIndicatorColor(color);
        this.mOrderTimerTextView.setVisibility(View.INVISIBLE);
    }

    private void startBlinkAnimation(View view) {
        Animation animation = new AlphaAnimation(LineScalePartyIndicator.SCALE, 0.0f);
        animation.setDuration(400);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setRepeatMode(2);
        view.startAnimation(animation);
    }

    public void stopTimer() {
        if (this.mWaitDriverTimer != null) {
            this.mWaitDriverTimer.cancel();
            this.mWaitDriverTimer = null;
        }
    }
}
