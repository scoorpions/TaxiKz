package kz.taxikz.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.robototextview.widget.RobotoTextView;

import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.pojo.News;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.BaseFragment;

public class NewsDetailFragment extends BaseFragment {

    @BindView(R.id.news_title)
    public RobotoTextView textViewNewsTitle;

    @BindView(R.id.news_description)
    public RobotoTextView textViewNewsDescription;

    @BindView(R.id.news_date)
    public RobotoTextView textViewNewsDate;
    private News news;

    public static Fragment newInstance() {
        return new NewsDetailFragment();
    }
    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_news_detail, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatas();
        initView();
    }

    private void initView() {
        textViewNewsTitle.setText(news.getTitle());
        textViewNewsDescription.setText(news.getDescription());
        textViewNewsDate.setText(news.getDate());
    }

    private void initDatas() {
        if (getArguments().getSerializable(NewsDetailActivity.EXTRA_NEWS) != null) {
            news = (News) getArguments().getSerializable(
                    NewsDetailActivity.EXTRA_NEWS);
        }
    }
}
