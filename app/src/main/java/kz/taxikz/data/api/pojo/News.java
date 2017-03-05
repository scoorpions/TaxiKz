package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable {
    @SerializedName("date")
    protected String date;
    @SerializedName("description")
    protected String description;
    @SerializedName("id")
    protected int id;
    @SerializedName("title")
    protected String title;

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
            z = this.date.equals(news.date);
        } else if (news.date != null) {
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
        hashCode = (i2 + hashCode) * 31;
        if (this.date != null) {
            i = this.date.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return "News{id=" + this.id + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", date='" + this.date + '\'' + '}';
    }
}
