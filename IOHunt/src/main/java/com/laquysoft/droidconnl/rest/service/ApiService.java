package com.laquysoft.droidconnl.rest.service;

import com.laquysoft.droidconnl.Hunt;
import com.laquysoft.droidconnl.rest.model.HuntItem;
import com.laquysoft.droidconnl.rest.model.HuntListModel;
import com.laquysoft.droidconnl.rest.model.HuntModel;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by joaobiriba on 21/04/15.
 */
public interface ApiService {

    @GET("/hunt")
    public HuntListModel getHunts();

    @GET("/hunt/{id}")
    public HuntItem getHunt(@Path("id") String id);
}
