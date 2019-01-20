package com.example.flavi.client.api;

import com.example.flavi.client.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginResource {
    String BASE_URL = "http://192.168.43.136:8080/";

    @POST("login")
    Call<Void> login(@Body User login);
}
