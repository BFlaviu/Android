package com.example.flavi.client.api;

import com.example.flavi.client.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TestResource {
    String BASE_URL = "http://192.168.43.136:8080/";

    @GET("greeting")
    Call<String> getGreeting(@Query("name") String name);

    @GET("songs")
    Call<List<Song>> getSongs(@Header("Auth") String token);

    @PUT("songs/update")
    Call<Boolean> updateSong(@Header("Auth") String token,@Body Song updatedSong);

    @POST("songs/replace")
    Call<Boolean> replaceSongList(@Header("Auth") String token,@Body List<Song> newSongList);

}
