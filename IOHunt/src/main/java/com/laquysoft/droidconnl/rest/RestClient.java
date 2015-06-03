package com.laquysoft.droidconnl.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laquysoft.droidconnl.Hunt;
import com.laquysoft.droidconnl.rest.model.HuntItem;
import com.laquysoft.droidconnl.rest.model.HuntListModel;
import com.laquysoft.droidconnl.rest.model.HuntModel;
import com.laquysoft.droidconnl.rest.service.ApiService;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * Created by joaobiriba on 21/04/15.
 */
public class RestClient {
    private static final String BASE_URL = "http://162.248.167.159:8080/";
    private ApiService apiService;

    public RestClient(ErrorHandler errorHandler)
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setErrorHandler(errorHandler)
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

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

    public HuntListModel getHunts() {
        return apiService.getHunts();
    }

    public HuntModel getHunt(String id) {
        return apiService.getHunt(id).getHunt();
    }
}
