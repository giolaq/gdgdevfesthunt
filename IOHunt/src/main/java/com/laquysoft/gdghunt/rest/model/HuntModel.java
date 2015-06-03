package com.laquysoft.gdghunt.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by joaobiriba on 04/05/15.
 */
@Parcel
public class HuntModel {

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("imageUrl")
    private String imageUrl;

    private boolean downloaded = false;

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
