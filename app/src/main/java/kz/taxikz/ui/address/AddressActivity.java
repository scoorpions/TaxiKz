package kz.taxikz.ui.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseSecondaryActivity;
import kz.taxikz.ui.address.fragment.AddressFragment;
import kz.taxikz.ui.util.UiUtils;
import kz.taxikz.ui.widget.item.NewAddressLocal;
import kz.taxikz.ui.widget.item.NewAddressLocal.TYPE;

public class AddressActivity extends BaseSecondaryActivity {

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        String title;
        super.onCreate(bundle);
        setCustomContentView(R.layout.activity_log_in);
        NewAddressLocal addressLocal = getIntent().getParcelableExtra(EXTRA_ADDRESS);
        switchFragment(AddressFragment.newInstance(addressLocal, newAddressLocal -> returnToMainActivity(addressLocal)));
        setSupportActionBar(this.mToolbar);
        initActionBar();
        TYPE addressType = addressLocal.getAddressType();
        if (addressType == TYPE.FROM) {
            title = getResources().getString(R.string.order_address_start_address_placeholder);
        } else if (addressType == TYPE.TO) {
            title = getResources().getString(R.string.order_address_end_address_placeholder);
        } else {
            title = getResources().getString(R.string.order_address_stay_address_placeholder);
        }
        getSupportActionBar().setTitle(title);
        this.mAnalyticsTrackers.screenVisited(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UiUtils.hideSoftKeyboard(this);
        overridePendingTransition(R.anim.comming_in, R.anim.comming_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UiUtils.hideSoftKeyboard(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        beginTransaction.replace(R.id.container, fragment);
        beginTransaction.commit();
    }

    private void returnToMainActivity(NewAddressLocal address) {
        UiUtils.hideSoftKeyboard(this);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, address);
        setResult(RESULT_OK, intent);
        finish();
    }
}
