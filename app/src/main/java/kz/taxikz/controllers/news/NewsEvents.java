package kz.taxikz.controllers.news;

import java.util.List;

import kz.taxikz.data.api.pojo.News;

public class NewsEvents {

    public static class GetNewsFailed {
    }

    public static class GetNewsSuccess {
        private final List<News> mNews;

        public GetNewsSuccess(List<News> newsList) {
            this.mNews = newsList;
        }

        public List<News> getNews() {
            return this.mNews;
        }
    }
}
