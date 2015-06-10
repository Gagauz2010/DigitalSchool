package ru.spsu.fmf.digitalschool.structures;

public class jsonNews {
    private String newsHead;
    private String newsText;
    private String publTime;
    private int newsCategory;
    private int newsId;
    private int whoAdd;
    private int important;

    public jsonNews(){}

    public int getNewsId() {
        return newsId;
    }

    public int getNewsCategory() {
        return newsCategory;
    }

    public String getNewsHead() {
        return newsHead;
    }

    public String getNewsText() {
        return newsText;
    }

    public String getPublTime() {
        return publTime;
    }

    public int getWhoAdd() {
        return whoAdd;
    }

    public int getImportant() {
        return important;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public void setNewsHead(String newsHead) {
        this.newsHead = newsHead;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public void setPublTime(String publTime) {
        this.publTime = publTime;
    }

    public void setNewsCategory(int newsCategory) {
        this.newsCategory = newsCategory;
    }

    public void setWhoAdd(int whoAdd) {
        this.whoAdd = whoAdd;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
