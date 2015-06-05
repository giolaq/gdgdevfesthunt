package com.laquysoft.gdghunt.rest.service;

import com.laquysoft.gdghunt.rest.model.HuntItem;
import com.laquysoft.gdghunt.rest.model.HuntListModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by joaobiriba on 21/04/15.
 */
public interface ApiService {

    @GET("/hunt")
    public void getHunts(Callback<HuntListModel> callback);

    @GET("/hunt/{id}")
    public HuntItem getHunt(@Path("id") String id);
}
