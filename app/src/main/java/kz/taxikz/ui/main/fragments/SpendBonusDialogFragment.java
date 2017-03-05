package kz.taxikz.ui.main.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;

public class SpendBonusDialogFragment extends BottomSheetDialogFragment {
    private static final int SEEK_BAR_STEP = 5;
    @BindView(R.id.bonus_info_icon_imageView)
    ImageView mBonusInfoImageView;
    @BindView(R.id.bonus_seek_bar)
    SeekBar mBonusSeekBar;
    private int mBonuses;
    private BottomSheetCallback mBottomSheetBehaviorCallback;
    private Callbacks mCallbacks;
    private int mLimit;
    private int mSelectedBonuses;
    @BindView(R.id.selected_bonuses_textView)
    TextView mSelectedBonusesTextView;
    @BindView(R.id.total_bonuses_textView)
    TextView mTotalBonusesTextView;

    public interface Callbacks {
        void onDismiss();

        void selectedBonuses(int i);
    }

    public SpendBonusDialogFragment() {
        this.mBottomSheetBehaviorCallback = new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == SpendBonusDialogFragment.SEEK_BAR_STEP) {
                    SpendBonusDialogFragment.this.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.mCallbacks != null) {
            this.mCallbacks.onDismiss();
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_dialog_spend_bonus, null);
        ButterKnife.bind(this, contentView);
        this.mBonusInfoImageView.setOnClickListener(v -> showDialogBonusInfo());
        this.mSelectedBonuses = 0;
        this.mBonusSeekBar.setProgress(0);
        this.mBonusSeekBar.incrementProgressBy(SEEK_BAR_STEP);
        this.mBonusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SpendBonusDialogFragment.this.mSelectedBonuses = (progress / SpendBonusDialogFragment.SEEK_BAR_STEP) * SpendBonusDialogFragment.SEEK_BAR_STEP;
                SpendBonusDialogFragment.this.updateUI();
                if (SpendBonusDialogFragment.this.mCallbacks != null) {
                    SpendBonusDialogFragment.this.mCallbacks.selectedBonuses(SpendBonusDialogFragment.this.mSelectedBonuses);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        updateUI();
        dialog.setContentView(contentView);
        Behavior behavior = ((LayoutParams) ((View) contentView.getParent()).getLayoutParams()).getBehavior();
        if (behavior != null && (behavior instanceof BottomSheetBehavior)) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(this.mBottomSheetBehaviorCallback);
        }
    }

    private void updateUI() {
        this.mTotalBonusesTextView.setText(String.valueOf(this.mBonuses));
        this.mSelectedBonusesTextView.setText(String.valueOf(this.mSelectedBonuses));
        this.mBonusSeekBar.setProgress(this.mSelectedBonuses);
        this.mBonusSeekBar.setMax(this.mLimit);
    }

    public void setData(int bonuses, int limit) {
        this.mBonuses = bonuses;
        this.mLimit = limit;
        this.mSelectedBonuses = 0;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    private void showDialogBonusInfo() {
        Builder builder = new Builder(getActivity());
        if (TaxiKzApp.storage.getOrderBonus() == 0) {
            String dialogMessage = getString(R.string.bonus_description);
            if (TaxiKzApp.storage.getBonus() <= TaxiKzApp.storage.getSpendBonusLimit()) {
                StringBuilder sb = new StringBuilder();
                sb.append(dialogMessage);
                sb.append(String.format(getString(R.string.bonus_description_with_limit), TaxiKzApp.storage.getSpendBonusLimit()));
                dialogMessage = sb.toString();
            }
            builder.setTitle(getString(R.string.bonus_title));
            builder.setMessage(Html.fromHtml(dialogMessage));
            builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        } else {
            builder.setTitle(getString(R.string.bonus_title));
            builder.setMessage(getString(R.string.bonus_not_availabile_description));
            builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        }
        builder.create().show();
    }
}
