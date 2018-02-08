package com.example.lokesh.inclass07;

/**
 * Created by Lokesh on 23/10/2017.
 */

public class itunesApp {
    private long _id;
    private String title, price, thumbUrl;

    public itunesApp(String title, String price, String thumbUrl) {
        this.title = title;
        this.price = price;
        this.thumbUrl = thumbUrl;
    }

    public itunesApp() {

    }

    @Override
    public String toString() {
        return "itunesApp{" +
                " title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public int hashCode() {
        int result =  thumbUrl!= null ? thumbUrl.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        itunesApp items1 = (itunesApp) o;

        if (price != null ? !price.equals(items1.price) : items1.price != null) return false;
        if (title != null ? !title.equals(items1.title) : items1.title != null)
            return false;
        if (thumbUrl != null ? !thumbUrl.equals(items1.thumbUrl) : items1.thumbUrl != null) return false; ;
            return false;
    }

}

