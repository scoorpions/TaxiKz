package kz.taxikz.ui.main.fragments;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineScalePartyIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.taxikz.AppConfig.FabricEvents;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.data.api.pojo.CreateOrderParam;
import kz.taxikz.helper.DataHelper;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseFragment;
import kz.taxikz.ui.main.adapter.AddressesAdapter;
import kz.taxikz.ui.main.adapter.vh.AddressViewHolder;
import kz.taxikz.ui.widget.item.NewAddressLocal;
import kz.taxikz.ui.widget.item.NewAddressLocal.TYPE;
import kz.taxikz.ui.widget.item.Note;
import kz.taxikz.utils.AnimUtil;
import timber.log.BuildConfig;

import static kz.taxikz.ui.main.fragments.OrderFragment.VIEW_STATE.CALC_PRICE;

public class OrderFragment extends BaseFragment {
    private static final int MAX_ADDRESS_COUNT = 5;
    private static final int MIN_ADDRESS_COUNT = 2;

    @BindView(R.id.addresses_recyclerView)
    RecyclerView addressesRecyclerView;
    @BindView(R.id.add_address_fab)
    FloatingActionButton mAddAddressButton;
    @BindView(R.id.add_address_layout)
    FrameLayout mAddAddressLayout;
    @BindView(R.id.additional_data_divider)
    View mAdditionalDataDivider;
    private List<NewAddressLocal> mAddresses;
    private AddressesAdapter mAddressesAdapter;
    @BindView(R.id.addresses_card_view)
    CardView mAddressesCardView;
    @BindView(R.id.bonus_balance_textView)
    TextView mBonusBalanceTextView;
    @BindView(R.id.bonus_button_layout)
    LinearLayout mBonusButton;
    private Callbacks mCallbacks;
    @BindView(R.id.clear_comment_imageView)
    ImageView mClearCommentButton;
    @BindView(R.id.comment_editText)
    EditText mCommentEditText;
    @BindView(R.id.comment_icon)
    ImageView mCommentIcon;
    @BindView(R.id.comment_layout)
    LinearLayout mCommentLayout;
    @BindView(R.id.create_order_button)
    FrameLayout mCreateOrderButton;
    private int mCurrentEditAddressIndex;
    @BindView(R.id.details_button_layout)
    LinearLayout mDetailsButton;
    private Handler mHandler;
    private AnimatorSet mHideBonusAnimatorSet;
    private AnimatorSet mHidePriceAnimatorSet;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;
    private int mOrderBonusMoney;
    @BindView(R.id.order_bonus_textView)
    TextView mOrderBonusTextView;
    private List<Note> mOrderDetails;
    private OrderDetailsDialogFragment mOrderDetailsFragment;
    @BindView(R.id.order_details_subtext_textView)
    TextView mOrderDetailsSubtextTextView;
    private int mOrderPrice;
    @BindView(R.id.order_price_textView)
    TextView mOrderPriceTextView;
    private PriceDetailsDialogFragment mPriceDetailsFragment;
    @BindView(R.id.price_divider)
    View mPriceDividerView;
    @BindView(R.id.price_layout)
    LinearLayout mPriceLayout;
    @BindView(R.id.price_loading_view)
    AVLoadingIndicatorView mPriceLoadingView;
    @BindView(R.id.root_price_layout)
    FrameLayout mRootPriceLayout;
    private AnimatorSet mShowBonusAnimatorSet;
    private AnimatorSet mShowPriceAnimatorSet;
    private AnimatorSet mShowPriceNumbersAnimatorSet;
    private SpendBonusDialogFragment mSpendBonusFragment;
    private int mTempOrderBonusMoney;
    @BindView(R.id.touch_interceptor_layout)
    FrameLayout mTouchInterceptorLayout;
    private VIEW_STATE mViewState;

    private class ShowPriceAnimListener implements AnimatorListener {
        final float viewWeight;

        ShowPriceAnimListener(float v) {
            this.viewWeight = v;
        }

        public void onAnimationStart(Animator animator) {
            OrderFragment.this.mRootPriceLayout.setVisibility(View.VISIBLE);
            OrderFragment.this.mPriceDividerView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mRootPriceLayout.getLayoutParams();
            layoutParams.weight = this.viewWeight;
            OrderFragment.this.mRootPriceLayout.setLayoutParams(layoutParams);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mRootPriceLayout.getLayoutParams();
            layoutParams.weight = this.viewWeight;
            OrderFragment.this.mRootPriceLayout.setLayoutParams(layoutParams);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    private class HideBonusAnimListener implements AnimatorListener {
        final float viewWeight;

        HideBonusAnimListener(float v) {
            this.viewWeight = v;
        }

        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (OrderFragment.this.mBonusButton != null && OrderFragment.this.mAdditionalDataDivider != null) {
                OrderFragment.this.mBonusButton.setVisibility(View.GONE);
                OrderFragment.this.mAdditionalDataDivider.setVisibility(View.GONE);
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mBonusButton.getLayoutParams();
                layoutParams.weight = this.viewWeight;
                OrderFragment.this.mBonusButton.setLayoutParams(layoutParams);
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            if (OrderFragment.this.mBonusButton != null && OrderFragment.this.mAdditionalDataDivider != null) {
                OrderFragment.this.mBonusButton.setVisibility(View.GONE);
                OrderFragment.this.mAdditionalDataDivider.setVisibility(View.GONE);
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mBonusButton.getLayoutParams();
                layoutParams.weight = this.viewWeight;
                OrderFragment.this.mBonusButton.setLayoutParams(layoutParams);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    private class HidePriceAnimListener implements AnimatorListener {
        final float viewWeight;

        HidePriceAnimListener(float f) {
            this.viewWeight = f;
        }

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (OrderFragment.this.mRootPriceLayout != null && OrderFragment.this.mPriceDividerView != null) {
                OrderFragment.this.mRootPriceLayout.setVisibility(View.GONE);
                OrderFragment.this.mPriceDividerView.setVisibility(View.GONE);
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mRootPriceLayout.getLayoutParams();
                layoutParams.weight = this.viewWeight;
                OrderFragment.this.mRootPriceLayout.setLayoutParams(layoutParams);
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            if (OrderFragment.this.mRootPriceLayout != null && OrderFragment.this.mPriceDividerView != null) {
                OrderFragment.this.mRootPriceLayout.setVisibility(View.GONE);
                OrderFragment.this.mPriceDividerView.setVisibility(View.GONE);
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mRootPriceLayout.getLayoutParams();
                layoutParams.weight = this.viewWeight;
                OrderFragment.this.mRootPriceLayout.setLayoutParams(layoutParams);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    private class ShowBonusAnimListener implements AnimatorListener {
        ShowBonusAnimListener() {
        }

        @Override
        public void onAnimationStart(Animator animator) {
            OrderFragment.this.mPriceLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            OrderFragment.this.mPriceLayout.setScaleX(LineScalePartyIndicator.SCALE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            OrderFragment.this.mPriceLayout.setScaleX(LineScalePartyIndicator.SCALE);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    private class ShowBonusWeightAnimListener implements AnimatorUpdateListener {
        ShowBonusWeightAnimListener() {
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (OrderFragment.this.mBonusButton != null) {
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mBonusButton.getLayoutParams();
                layoutParams.weight = (Float) valueAnimator.getAnimatedValue();
                OrderFragment.this.mBonusButton.setLayoutParams(layoutParams);
            }
        }
    }

    private class ShowBonusInfoAnimListener implements AnimatorListener {
        final  float viewWeight;

        ShowBonusInfoAnimListener(float f) {
            this.viewWeight = f;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            OrderFragment.this.mBonusButton.setVisibility(View.VISIBLE);
            OrderFragment.this.mAdditionalDataDivider.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mBonusButton.getLayoutParams();
            layoutParams.weight = this.viewWeight;
            OrderFragment.this.mBonusButton.setLayoutParams(layoutParams);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mBonusButton.getLayoutParams();
            layoutParams.weight = this.viewWeight;
            OrderFragment.this.mBonusButton.setLayoutParams(layoutParams);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    public interface Callbacks {
        void lockScreen();

        void onCreateOrder(CreateOrderParam createOrderParam);

        void onEditAddress(NewAddressLocal newAddressLocal);

        void onGetPrice(List<NewAddressLocal> list, List<Integer> list2);

        void onOrderCreatedSuccess();

        void onUpdateClientName(String clientName);

        void unlockScreen();
    }

    enum VIEW_STATE {
        NO_ADDRESS,
        CALC_PRICE,
        SHOW_PRICE,
        CREATE_ORDER
    }


    private class CallbackImpl extends Callback {

        class RunnableImpl implements Runnable {
            final  ViewHolder mViewHolder;

            RunnableImpl(ViewHolder viewHolder) {
                this.mViewHolder = viewHolder;
            }

            @Override
            public void run() {
                boolean b;
                if (OrderFragment.this.isAddressesReady()) {
                    OrderFragment.this.mAddressesAdapter.setRouteComplete(true);
                } else {
                    OrderFragment.this.mAddressesAdapter.setRouteComplete(false);
                }
                AddressesAdapter addressesAdapter = OrderFragment.this.mAddressesAdapter;
                b = this.mViewHolder.getAdapterPosition() != -1;
                addressesAdapter.setActiveItems(-1, false, b);
                if (this.mViewHolder.getAdapterPosition() != -1 && OrderFragment.this.mAddresses.size() < OrderFragment.MAX_ADDRESS_COUNT) {
                    OrderFragment.this.showAddAddressButton();
                }
                if (OrderFragment.this.isAddressesReady()) {
                    OrderFragment.this.mAddressesAdapter.setRouteComplete(true);
                    OrderFragment.this.requestOrderPrice();
                }
            }
        }

        CallbackImpl() {
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            return Callback.makeMovementFlags(3, 48);
        }

        @Override
        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (viewHolder != null && actionState != 0) {
                ((AddressViewHolder) viewHolder).setActive();
                OrderFragment.this.mAddressesAdapter.setActiveItems(viewHolder.getAdapterPosition(), true, true);
                OrderFragment.this.hideAddAddressButton();
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            Collections.swap(OrderFragment.this.mAddresses, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            OrderFragment.this.mAddressesAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            OrderFragment.this.mAddressesAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            OrderFragment.this.mAddressesAdapter.notifyItemChanged(target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            OrderFragment.this.mAddressesAdapter.setActiveItems(-1, false, false);
            OrderFragment.this.removeAddress(position);
            Answers.getInstance().logCustom(new CustomEvent(FabricEvents.REMOVE_ADDRESS_SWIPE));
        }

        @Override
        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            OrderFragment.this.mHandler.postDelayed(new RunnableImpl(viewHolder), 100);
        }
    }

    public OrderFragment() {
        this.mAddresses = new ArrayList<>();
        this.mCurrentEditAddressIndex = -1;
    }

    public static Fragment newInstance() {
        return new OrderFragment();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler = new Handler();
        mTouchInterceptorLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == 0 && OrderFragment.this.mCommentEditText.isFocused()) {
                Rect outRect = new Rect();
                OrderFragment.this.mCommentEditText.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    OrderFragment.this.mCommentEditText.clearFocus();
                    ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return false;
        });
        mAddressesAdapter = new AddressesAdapter(mAddresses, new AddressesAdapter.Callbacks() {

            @Override
            public void itemClicked(int position) {
                if (mCallbacks != null) {
                    mCurrentEditAddressIndex = position;
                    NewAddressLocal address = mAddresses.get(mCurrentEditAddressIndex);
                    if (position == 0) {
                        address.setAddressType(TYPE.FROM);
                    } else if (position == mAddresses.size() - 1) {
                        address.setAddressType(TYPE.TO);
                    } else {
                        address.setAddressType(TYPE.STAY);
                    }
                    mCallbacks.onEditAddress(address);
                }
            }

            @Override
            public void itemRemoveClicked(int position) {
                removeAddress(position);
                if (isAddressesReady()) {
                    requestOrderPrice();
                }
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.REMOVE_ADDRESS_BUTTON));
            }
        });
        addressesRecyclerView.setAdapter(this.mAddressesAdapter);
        addressesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        addressesRecyclerView.setNestedScrollingEnabled(false);
        new ItemTouchHelper(new CallbackImpl()).attachToRecyclerView(this.addressesRecyclerView);
        mAddAddressButton.setOnClickListener(v -> {
            addAddress();
            if (this.mAddresses.size() >= MAX_ADDRESS_COUNT) {
                hideAddAddressButton();
            }
        });
        mCommentLayout.setOnClickListener(v -> {
            mCommentEditText.requestFocus();
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this.mCommentEditText, MIN_ADDRESS_COUNT);
        });
        mCommentEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    mCommentIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.iconsLight));
                    if (mClearCommentButton.isEnabled()) {
                        mClearCommentButton.setEnabled(false);
                        mClearCommentButton.setClickable(false);
                        AnimUtil.scaleDown(mClearCommentButton, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
                        return;
                    }
                    return;
                }
                OrderFragment.this.mCommentIcon.setColorFilter(ContextCompat.getColor(OrderFragment.this.getContext(), R.color.icons));
                if (!mClearCommentButton.isEnabled()) {
                    mClearCommentButton.setEnabled(true);
                    mClearCommentButton.setClickable(true);
                    AnimUtil.scaleUp(mClearCommentButton, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
                }
            }

        });
        mClearCommentButton.setOnClickListener(v -> this.mCommentEditText.getText().clear());
        mCommentEditText.setText(BuildConfig.VERSION_NAME);
        mOrderDetails = getOrderDetails();
        mOrderDetailsFragment = new OrderDetailsDialogFragment();
        mOrderDetailsFragment.setOrderNotes(mOrderDetails);
        mOrderDetailsFragment.setCallbacks(() -> {
            if (isAddressesReady()) {
                requestOrderPrice();
            }
        });
        mOrderDetailsSubtextTextView.setText(orderDetailsToString(mOrderDetails));
        mDetailsButton.setOnClickListener(v -> {
            if (!mOrderDetailsFragment.isAdded()) {
                mOrderDetailsFragment.show(getActivity().getSupportFragmentManager(), mOrderDetailsFragment.getTag());
            }
        });
        mSpendBonusFragment = new SpendBonusDialogFragment();
        mBonusButton.setOnClickListener(v ->  showDialogSpendBonus());
        mRootPriceLayout.setOnClickListener(v -> {
            if (mViewState == VIEW_STATE.SHOW_PRICE && mOrderPrice > 0) {
                showPriceDetailsDialog();
            }
        });
        mCreateOrderButton.setOnClickListener(v -> {
            if (isAddressesReady()) {
                if (mViewState == VIEW_STATE.SHOW_PRICE) {
                    createOrder();
                } else if (mViewState == CALC_PRICE) {
                    showSnackbar(mMainLayout, R.string.order_create_no_price_error);
                    Answers.getInstance().logCustom(new CustomEvent(FabricEvents.CREATE_ORDER_NO_PRICE));
                }
            } else if (isOneAddress()) {
                showOneAddressOrderDialog();
            } else {
                showSnackbar(mMainLayout, R.string.order_create_no_address_error);
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.CREATE_ORDER_NO_ADDRESSES));
            }
        });
        clearAddresses();
        clearOrderDetails();
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCommentEditText.clearFocus();
        if (mViewState == CALC_PRICE) {
            requestOrderPrice();
        } else if (mViewState == VIEW_STATE.CREATE_ORDER) {
            clearAddresses();
            clearOrderDetails();
            updateUI();
        } else {
            updateUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void updateAddress(NewAddressLocal address) {
        if (mCurrentEditAddressIndex != -1 && mCurrentEditAddressIndex < mAddresses.size()) {
            mAddresses.set(mCurrentEditAddressIndex, address);
        }
        if (isAddressesReady()) {
            mAddressesAdapter.setRouteComplete(true);
        } else {
            mAddressesAdapter.setRouteComplete(false);
        }
        mAddressesAdapter.notifyDataSetChanged();
        if (isAddressesReady()) {
            requestOrderPrice();
        }
    }

    public void setPrice(int price) {
        mOrderPrice = price;
        mOrderBonusMoney = 0;
        if (isAddressesReady()) {
            mViewState = VIEW_STATE.SHOW_PRICE;
        } else {
            mViewState = VIEW_STATE.NO_ADDRESS;
        }
        updateUI();
    }

    public void orderPlaced() {
        mViewState = VIEW_STATE.NO_ADDRESS;
        updateUI();
        Builder builder = new Builder(getActivity());
        builder.setTitle(getString(R.string.order_created_success_title));
        builder.setMessage(getString(R.string.make_order_dialog_content_success));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            if (mCallbacks != null) {
                mCallbacks.onOrderCreatedSuccess();
            }
        });
        builder.create().show();
    }

    public void orderPlacedError() {
        mViewState = VIEW_STATE.NO_ADDRESS;
        updateUI();
        showSnackbar(mMainLayout, R.string.order_create_failed_error);
    }

    private List<Note> getOrderDetails() {
        return DataHelper.getNotes();
    }

    private void clearAddresses() {
        mAddresses.clear();
        for (int i = 0; i < MIN_ADDRESS_COUNT; i++) {
            mAddresses.add(new NewAddressLocal());
        }
        mAddressesAdapter.notifyDataSetChanged();
        mViewState = VIEW_STATE.NO_ADDRESS;
    }

    private void clearOrderDetails() {
        ListIterator<Note> i = mOrderDetails.listIterator();
        while (i.hasNext()) {
            Note detail = i.next();
            if (detail.isSelected()) {
                detail.toggle();
            }
            i.set(detail);
        }
    }

    private boolean isAddressesReady() {
        if (mAddresses.size() < MIN_ADDRESS_COUNT || mAddresses.size() > MAX_ADDRESS_COUNT) {
            return false;
        }
        for (int i = 0; i < mAddresses.size(); i++) {
            if (mAddresses.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isOneAddress() {
        return !(mAddresses.size() != MIN_ADDRESS_COUNT || mAddresses.get(0).isEmpty());
    }

    private void requestOrderPrice() {
        mViewState = CALC_PRICE;
        if (mCallbacks != null) {
            mCallbacks.onGetPrice(mAddresses, getOrderDetailsIds(mOrderDetails));
        }
        updateUI();
    }

    private void createOrder() {
        if (mCallbacks != null) {
            mViewState = VIEW_STATE.CREATE_ORDER;
            updateUI();
            mCallbacks.onCreateOrder(getOrderParams());
        }
    }

    private void addAddress() {
        if (mAddresses.size() != MAX_ADDRESS_COUNT) {
            mAddresses.add(mAddresses.size() - 1, new NewAddressLocal());
            mAddressesAdapter.notifyDataSetChanged();
            mViewState = VIEW_STATE.NO_ADDRESS;
            updateUI();
            Answers.getInstance().logCustom(new CustomEvent(FabricEvents.ADD_ADDRESS));
        }
    }

    private void removeAddress(int position) {
        if (mAddresses.size() == MIN_ADDRESS_COUNT) {
            mAddresses.remove(position);
            mAddresses.add(position, new NewAddressLocal());
            mAddressesAdapter.notifyDataSetChanged();
            mViewState = VIEW_STATE.NO_ADDRESS;
            updateUI();
            return;
        }
        mAddresses.remove(position);
        mAddressesAdapter.notifyDataSetChanged();
        if (mAddresses.size() < MAX_ADDRESS_COUNT) {
            showAddAddressButton();
        }
        if (isAddressesReady()) {
            mViewState = CALC_PRICE;
        } else {
            mViewState = VIEW_STATE.NO_ADDRESS;
        }
        updateUI();
    }

    private void hideAddAddressButton() {
        if (mAddAddressButton.isEnabled()) {
            mAddAddressButton.setEnabled(false);
            AnimUtil.scaleDown(mAddAddressLayout, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
        }
    }

    private void showAddAddressButton() {
        if (!mAddAddressButton.isEnabled()) {
            mAddAddressButton.setEnabled(true);
            AnimUtil.scaleUp(mAddAddressLayout, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
        }
    }

    private CreateOrderParam getOrderParams() {
        CreateOrderParam orderParam = new CreateOrderParam();
        orderParam.setPhone(TaxiKzApp.storage.getPhone());
        orderParam.setAutodialPhone(TaxiKzApp.storage.getPhone());
        orderParam.setClientId(TaxiKzApp.storage.getCliendId());
        StringBuilder sb = new StringBuilder();
        List<String> formattedAddressList = new ArrayList<>();
        for (NewAddressLocal address : this.mAddresses) {
            if (!address.isEmpty()) {
                sb.append(address.getAutocompleteAddressData().getId());
                if (address.getAutocompleteAddressData().getType().equals(AddressData.TYPE_STREET)) {
                    sb.append(ApiConfig.ADDRESS_SEPARATOR);
                    sb.append(address.getUserHouse());
                }
                formattedAddressList.add(sb.toString());
                sb.setLength(0);
            }
        }
        orderParam.setAddresses(formattedAddressList);
        orderParam.setCityId(TaxiKzApp.storage.getCityId());
        orderParam.setPorch(this.mAddresses.get(0).getUserPorch());
        orderParam.setComment(this.mCommentEditText.getText().toString());
        orderParam.setCustomer(TaxiKzApp.storage.getName());
        orderParam.setOrderParams(getOrderDetailsIds(this.mOrderDetails));
        orderParam.setTotalCost(this.mOrderPrice);
        orderParam.setBonusSum(this.mOrderBonusMoney);
        return orderParam;
    }

    private List<Integer> getOrderDetailsIds(List<Note> orderDetails) {
        List<Integer> orderDetailsIdList = new ArrayList<>();
        for (Note note : orderDetails) {
            if (note.isSelected()) {
                orderDetailsIdList.add(note.getId());
            }
        }
        return orderDetailsIdList;
    }

    private void showSnackbar(ViewGroup layout, int textResId) {
        Snackbar snackbar = Snackbar.make(mMainLayout, textResId, 0);
        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
        snackbar.show();
    }

    private void updateUI() {
        switch (mViewState) {
            case NO_ADDRESS:
                updateBonusInfo();
                hideBonusInfo();
                hidePriceInfo();
                hideLockLayout();
                return;
            case CALC_PRICE:
                showBonusInfo();
                updateBonusInfo();
                showPriceInfo(true);
                return;
            case SHOW_PRICE:
                showPriceInfo(false);
                showBonusInfo();
                updatePriceInfo();
                updateBonusInfo();
                return;
            case CREATE_ORDER:
                showLockLayout();
                return;
            default:
        }
    }

    private void showLockLayout() {
        if (this.mCallbacks != null) {
            this.mCallbacks.lockScreen();
        }
    }

    private void hideLockLayout() {
        if (this.mCallbacks != null) {
            this.mCallbacks.unlockScreen();
        }
    }

    private void updateBonusInfo() {
        if (TaxiKzApp.storage.getOrderBonus() > 0) {
            this.mBonusBalanceTextView.setText(getString(R.string.order_bonus_not_available));
            return;
        }
        int bonusCount = TaxiKzApp.storage.getBonus();
        mBonusBalanceTextView.setText(String.format(getString(R.string.order_bonus_subtitle), bonusCount));
    }

    private void updatePriceInfo() {
        animateUpdatePriceInfo();
        if (mOrderPrice == 0) {
            mOrderPriceTextView.setTextSize(0, getContext().getResources().getDimension(R.dimen.avh_no_price_text_size));
            mOrderPriceTextView.setText(getString(R.string.order_no_price));
            mOrderBonusTextView.setVisibility(View.GONE);
            return;
        }
        mOrderPriceTextView.setTextSize(0, getContext().getResources().getDimension(R.dimen.avh_price_text_size));
        mOrderPriceTextView.setText(String.format(getString(R.string.order_price_template), mOrderPrice - this.mOrderBonusMoney));
        if (mOrderBonusMoney > 0) {
            mOrderBonusTextView.setVisibility(View.VISIBLE);
            mOrderBonusTextView.setText(String.format(getString(R.string.order_price_title_with_price), this.mOrderBonusMoney));
        }
        mOrderBonusTextView.setVisibility(View.GONE);
    }

    private void showBonusInfo() {
        if (this.mBonusButton.getVisibility() != View.VISIBLE) {
            animateShowBonusInfo();
        }
    }

    private void showPriceInfo(boolean loadingView) {
        if (this.mRootPriceLayout.getVisibility() != View.VISIBLE) {
            animateShowPriceInfo();
        }
        if (loadingView) {
            this.mPriceLoadingView.setVisibility(View.VISIBLE);
            this.mPriceLayout.setVisibility(View.INVISIBLE);
            return;
        }
        this.mPriceLoadingView.setVisibility(View.GONE);
        this.mPriceLayout.setVisibility(View.VISIBLE);
    }

    private void hidePriceInfo() {
        if (this.mRootPriceLayout.getVisibility() == View.VISIBLE) {
            animateHidePriceInfo();
        }
    }

    private void hideBonusInfo() {
        if (this.mBonusButton.getVisibility() == View.VISIBLE) {
            animateHideBonusInfo();
        }
    }

    private void animateUpdatePriceInfo() {
        if (this.mShowPriceNumbersAnimatorSet != null && this.mShowPriceNumbersAnimatorSet.isRunning()) {
            this.mShowPriceNumbersAnimatorSet.cancel();
        }
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mPriceLayout, View.ALPHA, 0.0f, LineScalePartyIndicator.SCALE);
        alphaAnimator.setDuration(250);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(this.mPriceLayout, "scaleX", 0.0f, LineScalePartyIndicator.SCALE);
        scaleAnimator.setDuration(250);
        this.mShowBonusAnimatorSet = new AnimatorSet();
        this.mShowBonusAnimatorSet.play(alphaAnimator).with(scaleAnimator);
        this.mShowBonusAnimatorSet.addListener(new ShowBonusAnimListener());
        this.mShowBonusAnimatorSet.start();
    }

    private void animateShowBonusInfo() {
        if (this.mShowBonusAnimatorSet != null && this.mShowBonusAnimatorSet.isRunning()) {
            this.mShowBonusAnimatorSet.cancel();
        }
        float[] fArr = new float[MIN_ADDRESS_COUNT];
        fArr[0] = (float) this.mBonusButton.getMeasuredWidth();
        fArr[1] = 0.0f;
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(this.mBonusButton, "translationX", fArr);
        translationXAnimator.setDuration(250);
        float viewWeight = ((LayoutParams) this.mBonusButton.getLayoutParams()).weight;
        float[] fArr2 = new float[MIN_ADDRESS_COUNT];
        fArr2[0] = 0.0f;
        fArr2[1] = viewWeight;
        ValueAnimator weightAnimator = ValueAnimator.ofFloat(fArr2);
        weightAnimator.addUpdateListener(new ShowBonusWeightAnimListener());
        weightAnimator.setDuration(250);
        ObjectAnimator dividerAlpha = ObjectAnimator.ofFloat(this.mAdditionalDataDivider, View.ALPHA, 0.0f, LineScalePartyIndicator.SCALE);
        dividerAlpha.setDuration(250);
        this.mShowBonusAnimatorSet = new AnimatorSet();
        this.mShowBonusAnimatorSet.play(translationXAnimator).with(weightAnimator).with(dividerAlpha);
        this.mShowBonusAnimatorSet.addListener(new ShowBonusInfoAnimListener(viewWeight));
        this.mShowBonusAnimatorSet.start();
    }

    private void animateShowPriceInfo() {
        if (this.mShowPriceAnimatorSet != null && this.mShowPriceAnimatorSet.isRunning()) {
            this.mShowPriceAnimatorSet.cancel();
        }
        float[] fArr = new float[MIN_ADDRESS_COUNT];
        fArr[0] = (float) (this.mRootPriceLayout.getMeasuredWidth() * -1);
        fArr[1] = 0.0f;
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(this.mRootPriceLayout, "translationX", fArr);
        translationXAnimator.setDuration(250);
        float viewWeight = ((LayoutParams) this.mRootPriceLayout.getLayoutParams()).weight;
        float[] fArr2 = new float[MIN_ADDRESS_COUNT];
        fArr2[0] = 0.0f;
        fArr2[1] = viewWeight;
        ValueAnimator weightAnimator = ValueAnimator.ofFloat(fArr2);
        weightAnimator.addUpdateListener(valueAnimator -> {
            if (OrderFragment.this.mRootPriceLayout != null) {
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mRootPriceLayout.getLayoutParams();
                layoutParams.weight = (Float) valueAnimator.getAnimatedValue();
                OrderFragment.this.mRootPriceLayout.setLayoutParams(layoutParams);
            }
        });
        weightAnimator.setDuration(250);
        ObjectAnimator dividerAlpha = ObjectAnimator.ofFloat(this.mPriceDividerView, View.ALPHA, 0.0f, LineScalePartyIndicator.SCALE);
        dividerAlpha.setDuration(250);
        this.mShowPriceAnimatorSet = new AnimatorSet();
        this.mShowPriceAnimatorSet.play(translationXAnimator).with(weightAnimator).with(dividerAlpha);
        this.mShowPriceAnimatorSet.addListener(new ShowPriceAnimListener(viewWeight));
        this.mShowPriceAnimatorSet.start();
    }

    private void animateHideBonusInfo() {
        if (this.mHideBonusAnimatorSet != null && this.mHideBonusAnimatorSet.isRunning()) {
            this.mHideBonusAnimatorSet.cancel();
        }
        float[] fArr = new float[MIN_ADDRESS_COUNT];
        fArr[0] = 0.0f;
        fArr[1] = (float) this.mBonusButton.getMeasuredWidth();
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(this.mBonusButton, "translationX", fArr);
        translationXAnimator.setDuration(250);
        float viewWeight = ((LayoutParams) this.mBonusButton.getLayoutParams()).weight;
        float[] fArr2 = new float[MIN_ADDRESS_COUNT];
        fArr2[0] = viewWeight;
        fArr2[1] = 0.0f;
        ValueAnimator weightAnimator = ValueAnimator.ofFloat(fArr2);
        weightAnimator.addUpdateListener(valueAnimator -> {
            if (OrderFragment.this.mBonusButton != null) {
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mBonusButton.getLayoutParams();
                layoutParams.weight = (Float) valueAnimator.getAnimatedValue();
                OrderFragment.this.mBonusButton.setLayoutParams(layoutParams);
            }
        });
        weightAnimator.setDuration(250);
        ObjectAnimator dividerAlpha = ObjectAnimator.ofFloat(this.mAdditionalDataDivider, View.ALPHA, LineScalePartyIndicator.SCALE, 0.0f);
        dividerAlpha.setDuration(250);
        this.mHideBonusAnimatorSet = new AnimatorSet();
        this.mHideBonusAnimatorSet.play(translationXAnimator).with(weightAnimator).with(dividerAlpha);
        this.mHideBonusAnimatorSet.addListener(new HideBonusAnimListener(viewWeight));
        this.mHideBonusAnimatorSet.start();
    }

    private void animateHidePriceInfo() {
        if (this.mHidePriceAnimatorSet != null && this.mHidePriceAnimatorSet.isRunning()) {
            this.mHidePriceAnimatorSet.cancel();
        }
        float[] fArr = new float[MIN_ADDRESS_COUNT];
        fArr[0] = 0.0f;
        fArr[1] = (float) (this.mRootPriceLayout.getMeasuredWidth() * -1);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(this.mRootPriceLayout, "translationX", fArr);
        translationXAnimator.setDuration(250);
        float viewWeight = ((LayoutParams) this.mRootPriceLayout.getLayoutParams()).weight;
        float[] fArr2 = new float[MIN_ADDRESS_COUNT];
        fArr2[0] = viewWeight;
        fArr2[1] = 0.0f;
        ValueAnimator weightAnimator = ValueAnimator.ofFloat(fArr2);
        weightAnimator.addUpdateListener(valueAnimator -> {
            if (OrderFragment.this.mRootPriceLayout != null) {
                LayoutParams layoutParams = (LayoutParams) OrderFragment.this.mRootPriceLayout.getLayoutParams();
                layoutParams.weight = (Float) valueAnimator.getAnimatedValue();
                OrderFragment.this.mRootPriceLayout.setLayoutParams(layoutParams);
            }
        });
        weightAnimator.setDuration(250);
        ObjectAnimator dividerAlpha = ObjectAnimator.ofFloat(this.mPriceDividerView, View.ALPHA, LineScalePartyIndicator.SCALE, 0.0f);
        dividerAlpha.setDuration(250);
        this.mHidePriceAnimatorSet = new AnimatorSet();
        this.mHidePriceAnimatorSet.play(translationXAnimator).with(weightAnimator).with(dividerAlpha);
        this.mHidePriceAnimatorSet.addListener(new HidePriceAnimListener(viewWeight));
        this.mHidePriceAnimatorSet.start();
    }

    private void showPriceDetailsDialog() {
        if (this.mPriceDetailsFragment == null) {
            this.mPriceDetailsFragment = new PriceDetailsDialogFragment();
        }
        if (!this.mPriceDetailsFragment.isAdded()) {
            this.mPriceDetailsFragment.setData(this.mOrderPrice, this.mOrderBonusMoney, this.mOrderPrice - this.mOrderBonusMoney);
            this.mPriceDetailsFragment.show(getActivity().getSupportFragmentManager(), this.mPriceDetailsFragment.getTag());
        }
    }

    private void showOneAddressOrderDialog() {
        Builder builder = new Builder(getActivity());
        builder.setTitle(getString(R.string.order_one_address_dialog_title));
        builder.setMessage(getString(R.string.order_one_address_dialog_text));
        builder.setPositiveButton(R.string.ok,(dialog, which) -> {
            dialog.dismiss();
            createOrder();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showDialogSpendBonus() {
        if (mViewState == VIEW_STATE.SHOW_PRICE && !mSpendBonusFragment.isAdded()) {
            mSpendBonusFragment.setCallbacks(new kz.taxikz.ui.main.fragments.SpendBonusDialogFragment.Callbacks() {
                public void selectedBonuses(int bonusCount) {
                    mTempOrderBonusMoney = bonusCount;
                }

                public void onDismiss() {
                    mOrderBonusMoney = mTempOrderBonusMoney;
                    updateUI();
                }
            });
            this.mSpendBonusFragment.setData(TaxiKzApp.storage.getBonus(), mOrderPrice > TaxiKzApp.storage.getBonus() ? TaxiKzApp.storage.getBonus() : this.mOrderPrice);
            this.mSpendBonusFragment.show(getActivity().getSupportFragmentManager(), this.mSpendBonusFragment.getTag());
        }
    }

    private String orderDetailsToString(List<Note> list) {
        StringBuilder sb = new StringBuilder();
        for (Note note : list) {
            sb.append(note.getName());
            sb.append(", ");
        }
        String finalString = sb.toString();
        if (finalString.length() > MIN_ADDRESS_COUNT) {
            return finalString.substring(0, finalString.length() - 2);
        }
        return finalString;
    }
}
