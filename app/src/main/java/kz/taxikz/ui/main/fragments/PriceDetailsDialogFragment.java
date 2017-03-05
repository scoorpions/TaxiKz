package kz.taxikz.ui.main.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.taxikz.taxi4.R;

public class PriceDetailsDialogFragment extends DialogFragment {

    @BindView(R.id.base_price_textView)
    TextView mBasePriceTextView;
    @BindView(R.id.bonus_price_textView)
    TextView mBonusPriceTextView;
    @BindView(R.id.final_price_textView)
    TextView mFinalPriceTextView;

    private int mBasePrice;
    private int mBonusPrice;
    private int mFinalPrice;

    public void setData(int basePrice, int bonusPrice, int finalPrice) {
        this.mBasePrice = basePrice;
        this.mBonusPrice = bonusPrice;
        this.mFinalPrice = finalPrice;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, R.style.PriceDetailsDialog);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.PriceDetailsDialogAnimation;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_price_details, container, false);
        ButterKnife.bind(this, rootView);
        LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = 49;
        getDialog().getWindow().setAttributes(params);
        GestureDetector gestureDetector = new GestureDetector(getActivity(), new SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                    float diffY = e2.getY() - e1.getY();
                    if (Math.abs(diffY) > 100.0f && Math.abs(velocityY) > 100.0f && diffY <= 0.0f) {
                        PriceDetailsDialogFragment.this.dismiss();
                    }
                    return true;
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return false;
                }
            }
        });
        rootView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        this.mBasePriceTextView.setText(String.valueOf(this.mBasePrice));
        this.mBonusPriceTextView.setText(String.valueOf(this.mBonusPrice));
        this.mFinalPriceTextView.setText(String.format(getString(R.string.dialog_price_details_final_price), new Object[]{Integer.valueOf(this.mFinalPrice)}));
        return rootView;
    }
}
