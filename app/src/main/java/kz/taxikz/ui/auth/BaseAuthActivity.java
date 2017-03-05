package kz.taxikz.ui.auth;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import kz.taxikz.di.BaseDaggerActivity;

public abstract class BaseAuthActivity extends BaseDaggerActivity {

    protected void initToolbar() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowHomeEnabled(false);
        }
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            default: {
                return super.onOptionsItemSelected(menuItem);
            }
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initToolbar();
    }
}
