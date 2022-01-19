package com.wangmengyao.factsheetnews;

public class News {
    private String mTitle;
    private String mTopic;
    private String mDate;
    private String mOrigin;
    private String mUrl;

    public News() {
    }

    public News(String title, String topic, String date, String origin, String url) {
        mTitle = title;
        mTopic = topic;
        mDate = date;
        mOrigin = origin;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
