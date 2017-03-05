package kz.taxikz.filter;

import java.util.Collections;
import java.util.List;

import kz.taxikz.data.api.pojo.News;

public class NewsSort {

    public static List<News> getSortedNews(List<News> newses){
        Collections.sort(newses, (o1, o2) -> o1.getId() - o2.getId());
//        Collections.sort(newses, (o1, o2) ->
//                o1.getId() - o2.getId());
        return newses;
    }
}
