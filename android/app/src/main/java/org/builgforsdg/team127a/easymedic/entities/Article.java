package org.builgforsdg.team127a.easymedic.entities;


public class Article {
    private int id;
    private String description;
    private String category;
    private String author;
    private String body;
    private String type;
    private String path;
    private String dateTime;
    private String day;
    private String month;
    private String year;

    public Article() {
    }

    public Article( String description, String category, String author, String body,
                    String type, String path, String dateTime, String day, String month,
                    String year) {
        this.description = description;
        this.category = category;
        this.author = author;
        this.body = body;
        this.type = type;
        this.path = path;
        this.dateTime = dateTime;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

