package com.userstream.web.request;

public class WebRequest {
    private String id;
    private String type;
    private String data;
    private long timestamp;

    public WebRequest() {
        this.timestamp = System.currentTimeMillis();
    }

    public WebRequest(String id, String type, String data) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
