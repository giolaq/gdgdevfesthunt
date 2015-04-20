package com.laquysoft.droidconnl.rest.service;

import com.laquysoft.droidconnl.rest.model.Hunt;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by joaobiriba on 21/04/15.
 */
public interface ApiService {

    @GET("/api/hunt")
    public List<Hunt> getHunts();

}