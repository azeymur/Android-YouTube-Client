package com.zeymur.youtubeclient.model;

import java.util.List;

public class YTubeResponse {
    private String nextPageToken;
    private List<Item> items;

    public String getNextPageToken() { return nextPageToken; }
    public List<Item> getItems() { return items; }
}
