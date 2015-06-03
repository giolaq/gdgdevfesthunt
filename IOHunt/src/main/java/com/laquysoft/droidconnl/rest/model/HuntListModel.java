package com.laquysoft.droidconnl.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by joaobiriba on 21/04/15.
 */
@Parcel
public class HuntListModel {
    @SerializedName("Items")
    private final ArrayList<HuntModel> hunts = new ArrayList<>();

    public ArrayList<HuntModel> getHunts() {
        return hunts;
    }
}
