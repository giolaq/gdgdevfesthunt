package com.laquysoft.gdghunt.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by joaobiriba on 04/05/15.
 */
@Parcel
public class HuntItem {
    @SerializedName("Items")
    private HuntModel hunt;

    public HuntModel getHunt() {
        return hunt;
    }
}
