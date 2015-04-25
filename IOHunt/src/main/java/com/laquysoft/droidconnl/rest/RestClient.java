package com.laquysoft.droidconnl.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laquysoft.droidconnl.rest.service.ApiService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by joaobiriba on 21/04/15.
 */
public class RestClient
{
    private static final String BASE_URL = "http://192.168.1.100:8080/";
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

    public ApiService getApiService()
    {
        return apiService;
    }
}