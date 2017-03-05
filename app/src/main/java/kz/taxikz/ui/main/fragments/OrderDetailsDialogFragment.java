package kz.taxikz.ui.main.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.main.adapter.OrderDetailsAdapter;
import kz.taxikz.ui.util.UiUtils;
import kz.taxikz.ui.widget.item.Note;

public class OrderDetailsDialogFragment extends BottomSheetDialogFragment {
    private BottomSheetCallback mBottomSheetBehaviorCallback;
    private Callbacks mCallbacks;
    @BindView(R.id.client_icon_imageView)
    ImageView mClientIconImageView;
    @BindView(R.id.client_name_editText)
    EditText mClientNameEditText;
    @BindView(R.id.client_name_layout)
    LinearLayout mClientNameLayout;
    private List<Note> mOrderDetails;
    private OrderDetailsAdapter mOrderDetailsAdapter;
    @BindView(R.id.details_recyclerView)
    RecyclerView mOrderDetailsRecyclerView;
    private List<Note> mTempOrderDetails;
    @BindView(R.id.touch_interceptor_layout)
    FrameLayout mTouchInterceptorLayout;

    public interface Callbacks {
        void dataChanged();
    }

    public OrderDetailsDialogFragment() {
        this.mBottomSheetBehaviorCallback = new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == 5) {
                    OrderDetailsDialogFragment.this.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };
    }

    public void setOrderNotes(List<Note> list) {
        this.mOrderDetails = list;
        this.mTempOrderDetails = new ArrayList<>();
        for (Note note : this.mOrderDetails) {
            this.mTempOrderDetails.add(new Note(note));
        }
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        this.mClientNameEditText.clearFocus();
        this.mOrderDetailsRecyclerView.requestFocus();
        if (this.mOrderDetails != null && this.mTempOrderDetails != null && this.mOrderDetails.size() == this.mTempOrderDetails.size()) {
            boolean haveChanges = false;
            for (int i = 0; i < this.mOrderDetails.size(); i++) {
                if (this.mOrderDetails.get(i).isSelected() != this.mTempOrderDetails.get(i).isSelected()) {
                    this.mOrderDetails.set(i, new Note(this.mTempOrderDetails.get(i)));
                    haveChanges = true;
                }
            }
            if (haveChanges && this.mCallbacks != null) {
                this.mCallbacks.dataChanged();
            }
            UiUtils.hideSoftKeyboard(getActivity());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mClientNameEditText.setText(TaxiKzApp.storage.getName());
        this.mClientNameEditText.setSelection(this.mClientNameEditText.getText().toString().length());
        this.mClientNameEditText.clearFocus();
        this.mOrderDetailsRecyclerView.requestFocus();
        setClientNameColor();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_dialog_order_details, null);
        ButterKnife.bind(this, contentView);
        this.mOrderDetailsAdapter = new OrderDetailsAdapter(this.mTempOrderDetails);
        this.mOrderDetailsRecyclerView.setAdapter(this.mOrderDetailsAdapter);
        this.mOrderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mOrderDetailsRecyclerView.setNestedScrollingEnabled(false);
        this.mClientNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TaxiKzApp.storage.setName(s.toString());
                OrderDetailsDialogFragment.this.setClientNameColor();
            }
        });
        this.mClientNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == 6) {
                OrderDetailsDialogFragment.this.mClientNameEditText.setCursorVisible(false);
            }
            return false;
        });
        this.mClientNameEditText.setOnClickListener(v -> this.mClientNameEditText.setCursorVisible(true));
        this.mClientNameLayout.setOnClickListener(v -> {
            this.mClientNameEditText.setCursorVisible(true);
            UiUtils.showSoftKeyboardForEditText(this.mClientNameEditText);
        });
        this.mTouchInterceptorLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == 0 && OrderDetailsDialogFragment.this.mClientNameEditText.isFocused()) {
                Rect outRect = new Rect();
                OrderDetailsDialogFragment.this.mClientNameEditText.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    OrderDetailsDialogFragment.this.mClientNameEditText.clearFocus();
                    ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    OrderDetailsDialogFragment.this.mOrderDetailsRecyclerView.requestFocus();
                }
            }
            return false;
        });
        dialog.setContentView(contentView);
        Behavior behavior = ((LayoutParams) ((View) contentView.getParent()).getLayoutParams()).getBehavior();
        if (behavior != null && (behavior instanceof BottomSheetBehavior)) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(this.mBottomSheetBehaviorCallback);
        }
    }

    private void setClientNameColor() {
        int currentColor = 0;
        int color;
        if (this.mClientNameEditText.getText().toString().isEmpty()) {
            color = ContextCompat.getColor(this.mClientNameEditText.getContext(), R.color.secondaryText);
            if (this.mClientIconImageView.getTag() != null) {
                currentColor = (Integer) this.mClientIconImageView.getTag();
            }
            if (color != currentColor) {
                this.mClientIconImageView.setTag(color);
                this.mClientIconImageView.setColorFilter(color);
                this.mClientNameEditText.setTextColor(color);
                return;
            }
            return;
        }
        color = ContextCompat.getColor(this.mClientNameEditText.getContext(), R.color.primaryText);
        if (this.mClientIconImageView.getTag() != null) {
            currentColor = (Integer) this.mClientIconImageView.getTag();
        }
        if (color != currentColor) {
            this.mClientIconImageView.setTag(color);
            this.mClientIconImageView.setColorFilter(color);
            this.mClientNameEditText.setTextColor(color);
        }
    }
}
