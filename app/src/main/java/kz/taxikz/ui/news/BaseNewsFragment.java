package kz.taxikz.ui.news;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.news.NewsController;
import kz.taxikz.ui.BaseListFragment;

public class BaseNewsFragment extends BaseListFragment {

    @Inject
    protected NewsController newsController;

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}
