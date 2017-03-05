package kz.taxikz.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.pojo.News;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseSecondaryActivity;

public class NewsDetailActivity extends BaseSecondaryActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static final String EXTRA_NEWS = "news";

    public static void startActivity(final Activity activity, News news) {
        final Intent intent = new Intent(activity, (Class)NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS, news);
        activity.startActivity(intent);
    }

    private void switchFragment(final Fragment fragment) {
        final FragmentTransaction beginTransaction = this.getSupportFragmentManager().beginTransaction();
        beginTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        beginTransaction.replace(R.id.container, fragment);
        beginTransaction.commit();
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCustomContentView(R.layout.activity_log_in);
        this.setSupportActionBar(mToolbar);
        initActionBar();
        initView();


    }

    private void initView() {
        Fragment fragment = NewsDetailFragment.newInstance();
        if (getIntent().getSerializableExtra(EXTRA_NEWS) != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_NEWS, getIntent().getSerializableExtra(EXTRA_NEWS));
            fragment.setArguments(bundle);
        }
        switchFragment(fragment);
    }
}
