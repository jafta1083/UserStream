package com.userstream.reports;

public class Report {
    private int id;
    private String name;
    private String format;
    private String data;
    private long createdAt;

    public Report() {
        this.createdAt = System.currentTimeMillis();
    }

    public Report(int id, String name, String format, String data) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.data = data;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
