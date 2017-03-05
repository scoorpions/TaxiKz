package kz.taxikz.ui.news;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import butterknife.BindString;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseSecondaryActivity;

public class NewsActivity extends BaseSecondaryActivity {

    @BindString(R.string.title_activity_news)
    String mNews;

    public static void startActivity(FragmentActivity fragmentActivity) {
        Intent intent = new Intent(fragmentActivity, NewsActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(fragmentActivity, R.anim.push_right_enter, R.anim.push_right_exit);
            fragmentActivity.startActivity(intent, options.toBundle());
        } else {
            fragmentActivity.startActivity(intent);
        }
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomContentView(R.layout.layout_wrapper_to_toolbar);
        initView();
        initActionBar();

        mAnalyticsTrackers.screenVisited(mNews);
    }

    private void initView() {
        Fragment fragment = NewsFragment.newInstance();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }
}
