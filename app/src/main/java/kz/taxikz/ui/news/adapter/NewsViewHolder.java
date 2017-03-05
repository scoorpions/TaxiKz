package kz.taxikz.ui.news.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import kz.taxikz.data.api.pojo.News;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.misc.BaseViewHolder;
import kz.taxikz.ui.news.NewsDetailActivity;

public class NewsViewHolder extends BaseViewHolder{

    @BindView(R.id.textViewTitle)
    TextView mTextViewTitle;

    @BindView(R.id.textViewDescription)
    TextView mTextViewDescription;

    @BindView(R.id.item)
    View mainVew;

    private Activity mActivity;
    private News mNews;

    public NewsViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(News mNews, Activity mActivity) {
        this.mActivity = mActivity;
        this.mNews = mNews;
        mTextViewTitle.setText(mNews.getTitle());
        mTextViewDescription.setText(mNews.getDescription());
    }

    @OnClick(R.id.item)
    public void onItemClick(){
        NewsDetailActivity.startActivity(mActivity, mNews);
    }

}
