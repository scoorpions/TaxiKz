package kz.taxikz.ui;

import android.app.Activity;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public interface AppContainer {

    AppContainer DEFAULT = activity -> ButterKnife.findById(activity, android.R.id.content);

    ViewGroup get(final Activity activity);
}
