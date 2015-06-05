package com.laquysoft.gdghunt.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laquysoft.gdghunt.rest.model.HuntListModel;
import com.laquysoft.gdghunt.rest.model.HuntModel;
import com.laquysoft.gdghunt.rest.service.ApiService;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by joaobiriba on 21/04/15.
 */
public class RestClient {
    private static final String BASE_URL = "http://162.248.167.159:8080/";
    private ApiService apiService;

    public RestClient()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService get() { return apiService; }

    public HuntModel getHunt(String id) {
        return apiService.getHunt(id).getHunt();
    }
}
