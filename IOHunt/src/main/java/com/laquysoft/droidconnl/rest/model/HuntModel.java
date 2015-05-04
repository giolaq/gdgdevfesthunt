package com.laquysoft.droidconnl.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by joaobiriba on 21/04/15.
 */
@Parcel
public class HuntModel {

    @SerializedName("Items")
    private final ArrayList<Hunt> hunts = new ArrayList<>();

    public ArrayList<Hunt> getHunts() {
        return hunts;
    }



}
