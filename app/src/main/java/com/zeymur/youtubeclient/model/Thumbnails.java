package com.zeymur.youtubeclient.model;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {
    // "default" is a keyword in java
    @SerializedName("default")
    private Thumbnail def;
    private Thumbnail medium;
    private Thumbnail high;
    private Thumbnail standart;

    public Thumbnail getDef() { return def; }
    public Thumbnail getMedium() { return medium; }
    public Thumbnail getHigh() { return high; }
    public Thumbnail getStandart() { return standart; }
}
