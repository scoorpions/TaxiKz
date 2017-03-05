package kz.taxikz.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kz.taxikz.controllers.news.NewsEvents.GetNewsFailed;
import kz.taxikz.controllers.news.NewsEvents.GetNewsSuccess;
import kz.taxikz.data.api.pojo.News;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.news.adapter.NewsAdapter;
import kz.taxikz.utils.SimpleDividerItemDecoration;

public class NewsFragment extends BaseNewsFragment {

    private Adapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<News> newsList;

    public NewsFragment() {
        newsList = new ArrayList<>();
    }

    public static Fragment newInstance() {
        return new NewsFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        getNews();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getNews() {
        newsController.getNews();
    }

    private void initViews() {
        mAdapter = new NewsAdapter(newsList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onNewsFetchedFailedEvent(GetNewsFailed event) {
        mNetworkProgressView.setOnRetryClickListener(view -> {
            getNews();
            mNetworkProgressView.retry();
        });
        mNetworkProgressView.onError(getString(R.string.network_error_repeat));
    }

    @Subscribe
    public void onNewsFetchedSuccessEvent(GetNewsSuccess event) {
        mNetworkProgressView.onSuccess();
        newsList.clear();
        newsList.addAll(event.getNews());
        mAdapter.notifyDataSetChanged();
    }

}
