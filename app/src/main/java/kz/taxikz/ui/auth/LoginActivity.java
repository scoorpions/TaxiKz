package kz.taxikz.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import butterknife.BindString;
import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.helper.UiHelper;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.auth.fragment.RegistrationFragment;

public class LoginActivity extends BaseAuthActivity implements RegistrationFragment.OnSwitchFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindString(R.string.sign_in_title)
    String mLogin;

    public static void startActivity(final Context context) {
        context.startActivity(new Intent(context, (Class)LoginActivity.class));
    }

    public static void startActivity(final Activity activity, final boolean clearTask) {
        final Intent intent = new Intent(activity, (Class) LoginActivity.class);
        if (clearTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }

    private void switchFragment(final Fragment fragment) {
        final FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        beginTransaction.replace(R.id.container, fragment);
        beginTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setCustomContentView(R.layout.activity_log_in);
        switchFragment(RegistrationFragment.newInstance());
        setSupportActionBar(mToolbar);
        UiHelper.setTitle(this, R.string.sign_in_title);
        mAnalyticsTrackers.screenVisited(mLogin);
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    public void onSwitchFragment(Fragment fragment) {
        switchFragment(fragment);
    }

}
