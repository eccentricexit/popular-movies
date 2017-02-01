package com.deltabit.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/*-----Generated with http://www.jsonschema2pojo.org/----*/
@Parcel
class ReviewModel {

    @SerializedName("id")
    @Expose
    protected String id;
    @SerializedName("author")
    @Expose
    protected String author;
    @SerializedName("content")
    @Expose
    protected String content;
    @SerializedName("url")
    @Expose
    protected String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}