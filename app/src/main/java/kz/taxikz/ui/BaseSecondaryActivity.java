package kz.taxikz.ui;

import android.view.MenuItem;

import kz.taxikz.taxi4.R;

public abstract class BaseSecondaryActivity extends BaseMainActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        overridePendingTransition(R.anim.comming_in, R.anim.comming_out);
        finish();
        return true;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        getSupportActionBar().setLogo(null);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
