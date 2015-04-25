package com.laquysoft.droidconnl.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by joaobiriba on 21/04/15.
 */
@Parcel
public class HuntModel {

    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private String type;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("imgUrl")
    private String imgUrl;


    public String getDisplayName() {
        return displayName;
    }


}
