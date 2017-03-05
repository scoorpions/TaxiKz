package kz.taxikz.ui.common;

import android.view.View;

import kz.taxikz.utils.AndroidUtils;
import timber.log.Timber;

public abstract class MonkeySafeClickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        if (AndroidUtils.preventDoubleClick()) {
            Timber.d(getClass().getName(), "Click prevented");
            return;
        }
        onSafeClick(view);
    }

    public abstract void onSafeClick(View view);
}

