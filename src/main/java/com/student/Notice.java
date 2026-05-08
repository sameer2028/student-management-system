package com.student;

import java.sql.Timestamp;

public class Notice {

    private String title;
    private String content;
    private String postedBy;
    private Timestamp postedDate;

    // Constructor
    public Notice(String title, String content, String postedBy, Timestamp postedDate) {
        this.title = title;
        this.content = content;
        this.postedBy = postedBy;
        this.postedDate = postedDate;
    }

    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getPostedBy() { return postedBy; }
    public Timestamp getPostedDate() { return postedDate; }
}