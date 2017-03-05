package kz.taxikz.ui.splash;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;

import javax.inject.Inject;

import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.version.ParamsController;
import kz.taxikz.controllers.version.ParamsEvents.FailedEvent;
import kz.taxikz.controllers.version.ParamsEvents.SuccessEvent;
import kz.taxikz.controllers.version.ParamsEvents.UpdateEvent;
import kz.taxikz.di.BaseDaggerActivity;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.auth.LoginActivity;
import kz.taxikz.ui.main.MainActivity;
import kz.taxikz.ui.util.UiUtils;
import kz.taxikz.utils.NetworkStatusUtil;

public class SplashScreensActivity extends BaseDaggerActivity {

    private static final int ANIMATION_DURATION = 1000;
    @BindView(R.id.loading_view)
    AVLoadingIndicatorView mLoadingView;
    @BindView(R.id.logo_layout)
    LinearLayout mLogoLayout;

    @Inject
    ParamsController mParamsController;
    private AlertDialog mNetworkErrorDialog;
    private AlertDialog mUpdateVersionDialog;
    private ObjectAnimator mAlphaAnimator;
    private AlertDialog mDataErrorDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCustomContentView(R.layout.activity_splash_screen);
        this.mLogoLayout.setAlpha(0.0f);
    }

    private void initAnimation() {
        this.mAlphaAnimator = ObjectAnimator.ofFloat(this.mLogoLayout, View.ALPHA, 0.0f, 1.0f);
        this.mAlphaAnimator.setDuration(ANIMATION_DURATION);
        this.mAlphaAnimator.setInterpolator(new AccelerateInterpolator(2.0f));
        this.mAlphaAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                SplashScreensActivity.this.mLoadingView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                SplashScreensActivity.this.mLoadingView.setVisibility(View.VISIBLE);
                SplashScreensActivity.this.getAppParams();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void clearAnimation() {
        this.mLoadingView.setVisibility(View.INVISIBLE);
        this.mAlphaAnimator.cancel();
        this.mAlphaAnimator.removeAllListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((this.mUpdateVersionDialog == null && this.mNetworkErrorDialog == null
                && this.mDataErrorDialog == null) || ((this.mUpdateVersionDialog != null
                && !this.mUpdateVersionDialog.isShowing()) || ((this.mNetworkErrorDialog != null
                && !this.mNetworkErrorDialog.isShowing()) || (this.mDataErrorDialog != null
                && !this.mDataErrorDialog.isShowing())))) {
            initAnimation();
            this.mAlphaAnimator.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        clearAnimation();
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    private void getAppParams() {
        if (NetworkStatusUtil.getConnectivityStatus(this)) {
            this.mLoadingView.setVisibility(View.VISIBLE);
            this.mParamsController.fetchVersion();
            return;
        }
        this.mLoadingView.setVisibility(View.INVISIBLE);
        showNetworkErrorDialog();
    }

    @Subscribe
    public void onVersionSuccessListener(SuccessEvent successEvent) {
        this.mLoadingView.setVisibility(View.INVISIBLE);
        if (isAuthValid()) {
            MainActivity.startActivity(this);
            finish();
            return;
        }
        LoginActivity.startActivity(this);
        finish();
    }

    @Subscribe
    public void onVersionUpdateListener(UpdateEvent updateEvent) {
        this.mLoadingView.setVisibility(View.INVISIBLE);
        if (this.mUpdateVersionDialog == null) {
            Builder builder = new Builder(this);
            builder.setTitle(getString(R.string.dialog_params_need_update_title));
            builder.setMessage(getString(R.string.dialog_params_need_update_text));
            builder.setPositiveButton(R.string.dialog_params_need_update_ok_button, (dialog, which) -> {
                dialog.dismiss();
                UiUtils.showAppInGooglePlay(SplashScreensActivity.this);
                finish();
            });
            builder.setNegativeButton(R.string.dialog_params_need_update_cancel_button, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setCancelable(false);
            this.mUpdateVersionDialog = builder.create();
        }
        new Handler().post(() -> this.mUpdateVersionDialog.show());
    }

    @Subscribe
    public void onVersionFailedListener(FailedEvent failedEvent) {
        this.mLoadingView.setVisibility(View.INVISIBLE);
        if (this.mDataErrorDialog == null) {
            Builder builder = new Builder(this);
            builder.setTitle(getString(R.string.dialog_params_loading_error_title));
            builder.setMessage(getString(R.string.dialog_params_loading_error_text));
            builder.setPositiveButton(R.string.dialog_params_loading_ok_button, (dialog, which) -> {
                dialog.dismiss();
                getAppParams();
            });
            builder.setCancelable(false);
            this.mDataErrorDialog = builder.create();
        }
        new Handler().post(() -> this.mDataErrorDialog.show());
    }

    private boolean isAuthValid() {
        return !(TextUtils.isEmpty(TaxiKzApp.storage.getCliendId())
                || TextUtils.isEmpty(TaxiKzApp.storage.getPhone())
                || TextUtils.isEmpty(TaxiKzApp.storage.getPassword()));
    }

    private void showNetworkErrorDialog() {
        if (this.mNetworkErrorDialog == null) {
            Builder builder = new Builder(this);
            builder.setTitle(getString(R.string.error));
            builder.setMessage(getString(R.string.need_internet_connection));
            builder.setPositiveButton(R.string.retry, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setNegativeButton(R.string.exit, (dialog, which) -> mNetworkErrorDialog.show());
            builder.setCancelable(false);
            this.mNetworkErrorDialog = builder.create();
        }
        new Handler().post(() -> this.mNetworkErrorDialog.show());
    }
}
