package kz.taxikz.ui.main.adapter.vh;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.widget.item.Note;

public class OrderDetailsViewHolder extends BaseViewHolder {
    @BindView(R.id.detail_checkbox)
    AppCompatCheckBox mDetailCheckbox;
    @BindView(R.id.main_layout)
    FrameLayout mMainLayout;

    public interface Callbacks {
        void onClick();
    }

    public OrderDetailsViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Note note, Callbacks callbacks) {
        this.mMainLayout.setOnClickListener(v -> this.mDetailCheckbox.performClick());
        this.mDetailCheckbox.setOnClickListener(v -> {
            setCheckboxColor();
            if (callbacks != null) {
                callbacks.onClick();
            }
        });
        this.mDetailCheckbox.setText(note.getName());
        this.mDetailCheckbox.setChecked(note.isSelected());
        setCheckboxColor();
    }

    private void setCheckboxColor() {
        if (this.mDetailCheckbox.isChecked()) {
            this.mDetailCheckbox.setTextColor(ContextCompat.getColor(this.mDetailCheckbox.getContext(), R.color.primaryText));
        } else {
            this.mDetailCheckbox.setTextColor(ContextCompat.getColor(this.mDetailCheckbox.getContext(), R.color.secondaryText));
        }
    }
}
