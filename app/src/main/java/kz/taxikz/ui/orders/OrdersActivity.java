package kz.taxikz.ui.orders;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseSecondaryActivity;
import kz.taxikz.ui.orders.fragment.OrdersFragment;
import kz.taxikz.ui.orders.fragment.OrdersFragment.Callbacks;

public class OrdersActivity extends BaseSecondaryActivity {

    @BindView(R.id.lock_layout)
    FrameLayout mLockLayout;
    @BindString(R.string.order_state)
    String mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    public OrderController orderController;

    private ObjectAnimator mAlphaFadeInAnimator;
    private ObjectAnimator mAlphaFadeOutAnimator;


    class CallbacksImpl implements Callbacks {

        class FadeOutListener implements AnimatorListener {
            FadeOutListener() {
            }

            @Override
            public void onAnimationStart(Animator animator) {
                OrdersActivity.this.mLockLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                OrdersActivity.this.mLockLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                OrdersActivity.this.mLockLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }

        class LockColorUpdateListener implements AnimatorUpdateListener {
            final Window window;

            LockColorUpdateListener(Window window) {
                this.window = window;
            }

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (VERSION.SDK_INT >= 21) {
                    this.window.setStatusBarColor((Integer) animator.getAnimatedValue());
                }
            }
        }

        class LockColorAnimationListener implements AnimatorListener {
            final Window window;

            LockColorAnimationListener(Window window) {
                this.window = window;
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (VERSION.SDK_INT >= 21) {
                    this.window.setStatusBarColor(ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.transparentBackground));
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (VERSION.SDK_INT >= 21) {
                    this.window.setStatusBarColor(ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.transparentBackground));
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }

        class FadeInListener implements AnimatorListener {
            FadeInListener() {
            }

            @Override
            public void onAnimationStart(Animator animator) {
                OrdersActivity.this.mLockLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                OrdersActivity.this.mLockLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                OrdersActivity.this.mLockLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }

        class UnlockColorUpdateListener implements AnimatorUpdateListener {
            final Window window;

            UnlockColorUpdateListener(Window window) {
                this.window = window;
            }

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (VERSION.SDK_INT >= 21) {
                    this.window.setStatusBarColor((Integer) animator.getAnimatedValue());
                }
            }
        }

        class UnlockColorAnimationListener implements AnimatorListener {
            final  Window window;

            UnlockColorAnimationListener(Window window) {
                this.window = window;
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (VERSION.SDK_INT >= 21) {
                    this.window.setStatusBarColor(ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (VERSION.SDK_INT >= 21) {
                    this.window.setStatusBarColor(ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }

        CallbacksImpl() {
        }

        @Override
        public void lockScreen() {
            if (OrdersActivity.this.mAlphaFadeOutAnimator != null && OrdersActivity.this.mAlphaFadeOutAnimator.isRunning()) {
                OrdersActivity.this.mAlphaFadeOutAnimator.cancel();
            }
            OrdersActivity.this.mAlphaFadeOutAnimator = ObjectAnimator.ofFloat(OrdersActivity.this.mLockLayout, View.ALPHA, 0.0f, 1.0f);
            OrdersActivity.this.mAlphaFadeOutAnimator.setDuration(350);
            OrdersActivity.this.mAlphaFadeOutAnimator.addListener(new FadeOutListener());
            OrdersActivity.this.mAlphaFadeOutAnimator.start();
            if (VERSION.SDK_INT >= 21) {
                Window window = OrdersActivity.this.getWindow();
                window.addFlags(LinearLayoutManager.INVALID_OFFSET);
                int colorFrom = ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.colorPrimaryDark);
                int colorTo = ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.transparentBackground);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(350);
                colorAnimation.addUpdateListener(new LockColorUpdateListener(window));
                colorAnimation.addListener(new LockColorAnimationListener(window));
                colorAnimation.start();
            }
        }

        @Override
        public void unlockScreen() {
            if (OrdersActivity.this.mAlphaFadeInAnimator != null && OrdersActivity.this.mAlphaFadeInAnimator.isRunning()) {
                OrdersActivity.this.mAlphaFadeInAnimator.cancel();
            }
            OrdersActivity.this.mAlphaFadeInAnimator = ObjectAnimator.ofFloat(OrdersActivity.this.mLockLayout, View.ALPHA, 1.0f, 0.0f);
            OrdersActivity.this.mAlphaFadeInAnimator.setDuration(350);
            OrdersActivity.this.mAlphaFadeInAnimator.addListener(new FadeInListener());
            OrdersActivity.this.mAlphaFadeInAnimator.start();
            if (VERSION.SDK_INT >= 21) {
                Window window = OrdersActivity.this.getWindow();
                window.addFlags(LinearLayoutManager.INVALID_OFFSET);
                int colorFrom = ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.transparentBackground);
                int colorTo = ContextCompat.getColor(OrdersActivity.this.getApplicationContext(), R.color.colorPrimaryDark);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(350);
                colorAnimation.addUpdateListener(new UnlockColorUpdateListener(window));
                colorAnimation.addListener(new UnlockColorAnimationListener(window));
                colorAnimation.start();
            }
        }
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCustomContentView(R.layout.activity_orders);
        OrdersFragment ordersFragment = OrdersFragment.newInstance(this.orderController);
        ordersFragment.setCallbacks(new CallbacksImpl());
        switchFragment(ordersFragment);
        setSupportActionBar(this.mToolbar);
        initActionBar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(this.mTitle);
        }
        this.mAnalyticsTrackers.screenVisited(this.mTitle);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        beginTransaction.replace(R.id.container, fragment);
        beginTransaction.commitAllowingStateLoss();
    }
}
