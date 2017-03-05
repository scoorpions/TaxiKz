package kz.taxikz.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kz.taxikz.data.api.pojo.News;
import kz.taxikz.helper.SerializerHelper;

public class LocalCache {

    private static LocalCache INSTANCE = null;
    private static final String NEWS_FILE = "news";

    static {
        INSTANCE = new LocalCache();
    }

    private LocalCache() {
    }

    public static LocalCache getInstance() {
        return INSTANCE;
    }

    private void saveToFile(String fileName, String text) {
        try {
            new PrintWriter(fileName).println(text);
        } catch (FileNotFoundException e) {
//            Timber.e(Log.getStackTraceString(e));
        }
    }

    private String readFromFile(String fileName) {
        IOException ioException;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            try {
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    sb.append(line);
                    sb.append('\n');
                }
                bufferedReader.close();
            } catch (IOException iOException) {
                ioException = iOException;
//                Timber.e(Log.getStackTraceString(ioException));
                return sb.toString();
            } catch (Throwable th) {
                bufferedReader.close();
            }
        } catch (IOException iOException) {
            ioException = iOException;
//            Timber.e(Log.getStackTraceString(ioException));
            return sb.toString();
        }
        return sb.toString();
    }

    public void clear() {
        List<String> fileList = new ArrayList<>();
        fileList.add(NEWS_FILE);
        for (String item : fileList) {
//            Timber.d("Deleting file %s, result: %b", item, TaxiKzApp.get().deleteFile(item));
        }
    }

    // STORE NEWS
    public void storeNews(List<News> news) {
        SerializerHelper.storeObject((Serializable) news, NEWS_FILE);
    }

    // READ NEWS
    public List<News>  readNews() {
        return SerializerHelper.readObject(NEWS_FILE);
    }

}
