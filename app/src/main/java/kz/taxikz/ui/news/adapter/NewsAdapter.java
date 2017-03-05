package kz.taxikz.ui.news.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kz.taxikz.data.api.pojo.News;
import kz.taxikz.filter.NewsSort;
import kz.taxikz.taxi4.R;

public class NewsAdapter extends Adapter<NewsViewHolder> {

    private final Activity mActivity;
    private final List<News> mNews;

    public NewsAdapter(List<News> newsList, Activity mActivity) {
        mNews = NewsSort.getSortedNews(newsList);
        this.mActivity = mActivity;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder h, int position) {
        News news = mNews.get(position);
        h.bind(news, mActivity);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }
}
