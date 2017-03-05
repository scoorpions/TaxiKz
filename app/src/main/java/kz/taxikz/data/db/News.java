package kz.taxikz.data.db;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

import kz.taxikz.data.api.pojo.Photo;

@DatabaseTable(tableName = "news")
public class News implements Serializable {
    @SerializedName("date")
    @DatabaseField(columnName = "date")
    protected String date;
    @SerializedName("description")
    @DatabaseField(columnName = "description")
    protected String description;
    @SerializedName("id")
    @DatabaseField(columnName = "news_id", id = true)
    protected int id;
    @SerializedName("images")
    @DatabaseField(columnName = "images")
    protected List<Photo> images;
    @SerializedName("title")
    @DatabaseField(columnName = "title")
    protected String title;

    public interface Columns {
        public static final String NEWS_ID = "news_id";
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public List<Photo> getImages() {
        return this.images;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        News news = (News) o;
        if (this.id != news.id) {
            return false;
        }
        if (this.title != null) {
            if (!this.title.equals(news.title)) {
                return false;
            }
        } else if (news.title != null) {
            return false;
        }
        if (this.description != null) {
            if (!this.description.equals(news.description)) {
                return false;
            }
        } else if (news.description != null) {
            return false;
        }
        if (this.date != null) {
            if (!this.date.equals(news.date)) {
                return false;
            }
        } else if (news.date != null) {
            return false;
        }
        if (this.images != null) {
            z = this.images.equals(news.images);
        } else if (news.images != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int i2 = this.id * 31;
        if (this.title != null) {
            hashCode = this.title.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.description != null) {
            hashCode = this.description.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.date != null) {
            hashCode = this.date.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.images != null) {
            i = this.images.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return "News{id=" + this.id + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", date='" + this.date + '\'' + ", images=" + this.images + '}';
    }
}
