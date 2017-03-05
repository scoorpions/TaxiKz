package kz.taxikz.ui.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import kz.taxikz.AppConfig.FabricEvents;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.client.ClientController;
import kz.taxikz.controllers.client.ClientEvents.GetClientSuccess;
import kz.taxikz.controllers.cost.CostController;
import kz.taxikz.controllers.cost.CostEvents.CostFailed;
import kz.taxikz.controllers.cost.CostEvents.CostSuccess;
import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.controllers.order.OrderEvents.CreateOrderFailed;
import kz.taxikz.controllers.order.OrderEvents.CreateOrderSuccess;
import kz.taxikz.controllers.order.OrderEvents.GetOrdersSuccess;
import kz.taxikz.data.api.pojo.CreateOrderParam;
import kz.taxikz.data.prefs.BooleanPreference;
import kz.taxikz.event.CityChangedEvent;
import kz.taxikz.google.RegistrationIntentService;
import kz.taxikz.helper.DataHelper;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseMainActivity;
import kz.taxikz.ui.about.AboutActivity;
import kz.taxikz.ui.address.AddressActivity;
import kz.taxikz.ui.auth.IsPhoneValid;
import kz.taxikz.ui.auth.LoginActivity;
import kz.taxikz.ui.main.fragments.OrderFragment;
import kz.taxikz.ui.main.fragments.OrderFragment.Callbacks;
import kz.taxikz.ui.misc.UiEvents.NavigationMenuClicked;
import kz.taxikz.ui.news.NewsActivity;
import kz.taxikz.ui.orders.OrdersActivity;
import kz.taxikz.ui.util.UiUtils;
import kz.taxikz.ui.widget.item.City;
import kz.taxikz.ui.widget.item.NewAddressLocal;
import kz.taxikz.utils.AnimUtil;
import kz.taxikz.utils.GooglePlayServiceUtil;
import kz.taxikz.utils.IntentHelper;
import kz.taxikz.utils.NetworkStatusUtil;
import timber.log.BuildConfig;

public class MainActivity extends BaseMainActivity {
    @Inject
    ClientController clientController;
    @Inject
    CostController costController;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private AlertDialog mCityDialog;
    private List<City> mCityList;
    private CityDialogListAdapter mCityListAdapter;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Inject
    @IsPhoneValid
    BooleanPreference mIsPhoneValid;
    @BindView(R.id.lock_layout)
    FrameLayout mLockLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private TextView mToolbarCityName;
    private TextView mToolbarOrderCount;
    private RelativeLayout mToolbarOrderLayout;
    private TextView mUserNameTextView;
    private TextView mUserPhoneTextView;
    @Inject
    OrderController orderController;

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    public static void startActivity(Activity activity) {
        startActivity(activity, false);
    }

    public static void startActivity(Activity activity, boolean clearTask) {
        Intent intent = new Intent(activity, MainActivity.class);
        if (clearTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomContentView(R.layout.activity_main);
        if (this.mIsPhoneValid.get()) {
            changeFragment(createOrderFragment());
            setupToolbar();
            setupSideMenu();
            setTitle(BuildConfig.VERSION_NAME);
            TaxiKzApp.storage.setOrderTime("20150417030000");
            if (GooglePlayServiceUtil.checkPlayServices(this)) {
                TaxiKzApp.storage.setNews(true);
                startService(new Intent(this, RegistrationIntentService.class));
                return;
            }
            return;
        }
        LoginActivity.startActivity(this, true);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.clientController.getClientInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TaxiKzApp.storage.getCurrentCity().isEmpty()) {
            showChoiceCityDialog();
        }
        orderController.getOrders();
    }

    private void showChoiceCityDialog() {
        if (mCityDialog == null) {
            mCityList = DataHelper.getCityList();
            mCityListAdapter = new CityDialogListAdapter(getApplicationContext(), mCityList,city -> {
                saveSelectedCity(city);
                changeFragment(createOrderFragment());
                eventBus.post(new CityChangedEvent());
                updateToolbarCity();
                mCityDialog.dismiss();
            });
            Builder builder = new Builder(this);
            builder.setTitle(R.string.dialog_list_title);
            builder.setAdapter(mCityListAdapter, (dialog, which) -> {});
            builder.setCancelable(false);
            mCityDialog = builder.create();
        }
        if (!mCityDialog.isShowing()) {
            mCityDialog.show();
        }
    }

    private OrderFragment createOrderFragment() {
        OrderFragment fragment = (OrderFragment) OrderFragment.newInstance();
        fragment.setCallbacks(new Callbacks() {
            @Override
            public void onEditAddress(NewAddressLocal addressLocal) {
                startAddressActivity(addressLocal);
            }

            @Override
            public void onGetPrice(List<NewAddressLocal> addressList, List<Integer> orderDetails) {
                getOrderPrice(addressList, orderDetails);
            }

            @Override
            public void onCreateOrder(CreateOrderParam orderParam) {
                sendOrder(orderParam);
            }

            @Override
            public void onUpdateClientName(String clientName) {
                if (!TaxiKzApp.storage.getName().equals(clientName)) {
                    TaxiKzApp.storage.setName(clientName);
                    clientController.updateClientInfo(clientName);
                }
            }

            @Override
            public void onOrderCreatedSuccess() {
                showOrdersActivity();
            }

            @Override
            public void lockScreen() {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mLockLayout.setVisibility(View.VISIBLE);
                if (VERSION.SDK_INT >= 21) {
                    Window window = MainActivity.this.getWindow();
                    window.addFlags(LinearLayoutManager.INVALID_OFFSET);
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.transparentBackground));
                }
            }

            @Override
            public void unlockScreen() {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mLockLayout.setVisibility(View.GONE);
                if (VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.addFlags(LinearLayoutManager.INVALID_OFFSET);
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                }
            }
        });
        return fragment;
    }

    public void startAddressActivity(NewAddressLocal address) {
        if (NetworkStatusUtil.getConnectivityStatus(this)) {
            Intent intent = new Intent(this, AddressActivity.class);
            intent.putExtra(AddressActivity.EXTRA_ADDRESS, address);
            ActivityCompat.startActivityForResult(this, intent, 0, null);
        }
    }

    private void setupToolbar() {
        View toolbarChild = LayoutInflater.from(this).inflate(R.layout.toolbar_order, this.mToolbar, false);
        LayoutParams toolbarChildLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (TaxiKzApp.storage.getLastAppVersion() > 52) {
            FrameLayout newVersionLayout = (FrameLayout) toolbarChild.findViewById(R.id.new_version_layout);
            newVersionLayout.setVisibility(View.VISIBLE);
            newVersionLayout.setOnClickListener(v -> showAppUpdateDialog());
        }
        mToolbarOrderLayout = (RelativeLayout) toolbarChild.findViewById(R.id.current_orders_count_layout);
        mToolbarOrderLayout.setOnClickListener(v -> showOrdersActivity());
        mToolbarCityName = (TextView) toolbarChild.findViewById(R.id.city_textView);
        mToolbarOrderCount = (TextView) toolbarChild.findViewById(R.id.orders_count);
        mToolbarOrderCount.setEnabled(false);
        AnimUtil.scaleDown(this.mToolbarOrderCount, 1, true);
        updateToolbarCity();
        mToolbar.addView(toolbarChild, toolbarChildLayoutParams);
        setSupportActionBar(this.mToolbar);
    }

    private void updateToolbarCity() {
        this.mToolbarCityName.setText(TaxiKzApp.storage.getCurrentCity());
    }

    private void setupSideMenu() {
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int drawerWidthPx = metrics.widthPixels - ((int) (56.0f * (((float) metrics.densityDpi) / 160.0f)));
        int maxDrawerWidthPx = (int) (320.0f * (((float) metrics.densityDpi) / 160.0f));
        if (drawerWidthPx > maxDrawerWidthPx) {
            drawerWidthPx = maxDrawerWidthPx;
        }
        layoutParams.width = drawerWidthPx;
        mNavigationView.setLayoutParams(layoutParams);
        mUserPhoneTextView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_phone_textView);
        mUserNameTextView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_textView);
        mNavigationView.setNavigationItemSelectedListener(item -> {
            mDrawerLayout.closeDrawers();
            eventBus.post(new NavigationMenuClicked(item));
            return true;
        });
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, this.mToolbar, 0, 0);
        mActionBarDrawerToggle.setToolbarNavigationClickListener(v ->  onBackPressed());
        mDrawerLayout.addDrawerListener(this.mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    private void showOrdersActivity() {
        Intent intent = new Intent(this, OrdersActivity.class);
        if (VERSION.SDK_INT >= 16) {
            startActivity(intent);
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == -1) {
            NewAddressLocal address = data.getExtras().getParcelable(AddressActivity.EXTRA_ADDRESS);
            if (address != null) {
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != null && (currentFragment instanceof OrderFragment)) {
                    ((OrderFragment) currentFragment).updateAddress(address);
                }
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.eventBus.post(new CityChangedEvent());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMenuCategory(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_city:
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.SIDE_MENU_CITY_CLICK));
                showChoiceCityDialog();
                return;
            case R.id.nav_news:
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.SIDE_MENU_NEWS_CLICK));
                NewsActivity.startActivity(this);
                return;
            case R.id.nav_about:
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.SIDE_MENU_ABOUT_CLICK));
                AboutActivity.startActivity(this);
                return;
            case R.id.nav_exit:
                showAccountExitDialog();
                return;
            case R.id.nav_share:
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.SIDE_MENU_SHARE_CLICK));
                IntentHelper.share(this);
                return;
            case R.id.nav_send:
                Answers.getInstance().logCustom(new CustomEvent(FabricEvents.SIDE_MENU_REVIEW_CLICK));
                IntentHelper.feedback(this);
                return;
            default:
        }
    }

    private void sendOrder(CreateOrderParam orderParam) {
        this.orderController.createOrder(orderParam);
    }

    private void getOrderPrice(List<NewAddressLocal> addressList, List<Integer> orderDetails) {
        if (NetworkStatusUtil.getConnectivityStatus(getApplicationContext())) {
            this.costController.fetchPrice(addressList, orderDetails);
        } else {
            sendPriceToFragment(0);
        }
    }

    private void sendPriceToFragment(int price) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null && (currentFragment instanceof OrderFragment)) {
            ((OrderFragment) currentFragment).setPrice(price);
        }
    }

    private void showAccountExitDialog() {
        Builder builder = new Builder(this);
        builder.setTitle(getString(R.string.exit_from_account));
        builder.setMessage(getString(R.string.navigation_exit_alert_message));
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            TaxiKzApp.storage.setPhone(BuildConfig.VERSION_NAME);
            TaxiKzApp.storage.setClientId(BuildConfig.VERSION_NAME);
            TaxiKzApp.storage.setPassword(BuildConfig.VERSION_NAME);
            TaxiKzApp.storage.setName(BuildConfig.VERSION_NAME);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showAppUpdateDialog() {
        Builder builder = new Builder(this);
        builder.setTitle(getString(R.string.dialog_params_may_update_title));
        builder.setMessage(getString(R.string.dialog_params_may_update_text));
        builder.setPositiveButton(R.string.dialog_params_may_update_ok_button, (dialog, which) -> {
            dialog.dismiss();
            UiUtils.showAppInGooglePlay(this);
        });
        builder.setNegativeButton(R.string.dialog_params_may_update_cancel_button, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void saveSelectedCity(City city) {
        TaxiKzApp.storage.setUdsId(city.getUdsId());
        TaxiKzApp.storage.setCrewGroup_id(city.getCrewGroupId());
        TaxiKzApp.storage.setCityId(String.valueOf(city.getId()));
        TaxiKzApp.storage.setCurrentCity(city.getName());
    }

    @Subscribe
    public void onNavigationMenuClickedListener(NavigationMenuClicked event) {
        openMenuCategory(event.getMenuItem());
    }

    @TargetApi(16)
    @Subscribe
    public void onOrderSuccessListener(GetOrdersSuccess success) {
        int count = 0;
        if (success.getOrdersList() != null) {
            count = success.getOrdersList().size();
        } else {
            TaxiKzApp.storage.setOrderBonus(0);
        }
        if (count > 0) {
            if (!this.mToolbarOrderCount.isEnabled()) {
                this.mToolbarOrderCount.setEnabled(true);
                AnimUtil.scaleUp(this.mToolbarOrderCount, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
            }
            this.mToolbarOrderCount.setText(String.valueOf(count));
            AnimUtil.fadeInRightAnimation(this.mToolbarOrderLayout);
            this.mToolbarOrderCount.setText(String.valueOf(count));
            return;
        }
        if (this.mToolbarOrderCount.isEnabled()) {
            this.mToolbarOrderCount.setEnabled(false);
            AnimUtil.scaleDown(this.mToolbarOrderCount, Callback.DEFAULT_SWIPE_ANIMATION_DURATION, true);
        }
        this.mToolbarOrderCount.setText(String.valueOf(count));
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(0);
    }

    @Subscribe
    public void onCostSuccess(CostSuccess costSuccess) {
        sendPriceToFragment(costSuccess.getCostData().getSum());
    }

    @Subscribe
    public void onCostFailed(CostFailed costFailed) {
        sendPriceToFragment(0);
    }

    @Subscribe
    public void onOrderCreated(CreateOrderSuccess createOrderSuccess) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null && (currentFragment instanceof OrderFragment)) {
            ((OrderFragment) currentFragment).orderPlaced();
        }
        changeFragment(createOrderFragment());
        if (GooglePlayServiceUtil.checkPlayServices(this)) {
            TaxiKzApp.storage.setNews(false);
            startService(new Intent(this, RegistrationIntentService.class));
        }
    }

    @Subscribe
    public void onOrderCreateFailed(CreateOrderFailed createOrderFailed) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null && (currentFragment instanceof OrderFragment)) {
            ((OrderFragment) currentFragment).orderPlacedError();
        }
    }

    @Subscribe
    public void onCityChangedListener(CityChangedEvent cityChangedEvent) {
        this.mNavigationView.getMenu().findItem(R.id.nav_city).setTitle(Html.fromHtml(TaxiKzApp.storage.getCurrentCity()));
    }

    @Subscribe
    public void onClientInfoGetSuccessListener(GetClientSuccess getClientSuccess) {
        this.mUserPhoneTextView.setText(getString(R.string.navigation_phone_pre, TaxiKzApp.storage.getPhone()));
        this.mUserNameTextView.setText(TaxiKzApp.storage.getName());
    }
}
