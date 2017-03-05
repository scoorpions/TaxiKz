package kz.taxikz.ui.about;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseSecondaryActivity;

public class AboutActivity extends BaseSecondaryActivity {
    @BindString(R.string.about)
    String about;
    @BindView(R.id.view_content)
    RobotoTextView aboutContent;
    @BindView(R.id.about_layout)
    RelativeLayout aboutLayout;
    @BindView(R.id.credits_layout)
    LinearLayout creditsLayout;
    @BindView(R.id.divider)
    View dividerView;
    @BindView(R.id.credits_icons_links_layout)
    LinearLayout iconsLinksLayout;
    @BindView(R.id.credits_cc_license_text_view)
    TextView licenseLinkTextView;
    @BindView(R.id.show_credits_button)
    Button showCreditsButton;
    private Unbinder unbinder;
    String versionName;
    @BindView(R.id.view_version)
    RobotoTextView versionRTV;

    public static void startActivity(Activity activity) {
        Intent aboutIntent = new Intent(activity, AboutActivity.class);
        if (VERSION.SDK_INT >= 16) {
            activity.startActivity(aboutIntent, ActivityOptions.makeCustomAnimation(activity, R.anim.push_right_enter, R.anim.push_right_exit).toBundle());
        } else {
            activity.startActivity(aboutIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomContentView(R.layout.activity_about);
        this.unbinder = ButterKnife.bind(this);
        PackageManager packageManager = getPackageManager();
        if (packageManager != null) {
            try {
                this.versionName = packageManager.getPackageInfo(getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                this.versionName = null;
            }
            this.versionRTV.setText(String.format(getString(R.string.version), new Object[]{this.versionName}));
        }
        setTitle(this.about);
        initActionBar();
        this.aboutContent.setText(Html.fromHtml(TaxiKzApp.storage.getAboutAppContent()));
        setupCredits();
        this.showCreditsButton.setOnClickListener(v -> showCredits());
        this.mAnalyticsTrackers.screenVisited(this.about);
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.comming_in, R.anim.comming_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        this.unbinder.unbind();
        super.onDestroy();
    }

    private void showCredits() {
        this.aboutLayout.setVisibility(View.GONE);
        this.showCreditsButton.setVisibility(View.GONE);
        this.dividerView.setVisibility(View.GONE);
        this.creditsLayout.setVisibility(View.VISIBLE);
    }

    private void setupCredits() {
        this.licenseLinkTextView.setText(Html.fromHtml(getResources().getString(R.string.credits_cc_license_link)));
        this.licenseLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        int linksMarginTB = (int) getResources().getDimension(R.dimen.cf_links_marginTB);
        String[] iconsLinksArray = getResources().getStringArray(R.array.credits_icons_links);
        for (String fromHtml : iconsLinksArray) {
            TextView nextLink = new TextView(this.iconsLinksLayout.getContext());
            nextLink.setText(Html.fromHtml(fromHtml));
            nextLink.setMovementMethod(LinkMovementMethod.getInstance());
            nextLink.setTextSize(0, getResources().getDimension(R.dimen.cf_links_text_size));
            LayoutParams params = new LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            params.setMargins(0, linksMarginTB, 0, linksMarginTB);
            params.gravity = Gravity.CENTER;
            nextLink.setLayoutParams(params);
            this.iconsLinksLayout.addView(nextLink);
        }
    }
}
