package com.zeymur.youtubeclient.model;

import java.util.Date;

public class Snippet {
    private Date publishedAt;
    private String title;
    private String description;
    private Thumbnails thumbnails;

    public Date getPublishedAt() { return publishedAt; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Thumbnails getThumbnails() { return thumbnails; }
}
