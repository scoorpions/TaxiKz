package kz.taxikz.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public interface FragmentLifeCycleListener {
    void onDestroyView();

    void onPause();

    void onResume();

    void onViewCreated(View view, @Nullable Bundle bundle, Fragment fragment);
}
