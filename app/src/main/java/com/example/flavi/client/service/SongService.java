package com.example.flavi.client.service;

import android.widget.Toast;

import com.example.flavi.client.AppContext;
import com.example.flavi.client.api.TestResource;
import com.example.flavi.client.database.LocalDatabase;
import com.example.flavi.client.database.SongDatabase;
import com.example.flavi.client.model.Song;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SongService extends Observable {

    private LocalDatabase localDb;
    private SongDatabase songDatabase;

    public void startLocalDb() {
        localDb= LocalDatabase.getDatabase(AppContext.getContext());
        songDatabase = localDb.songDatabase();
    }

    public SongService(){
        startLocalDb();
    }

    public void updateSong(Song newSong){
        if(!LoginService.offline) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TestResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TestResource api = retrofit.create(TestResource.class);
            Call<Boolean> call = api.updateSong(LoginService.token,newSong);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    updateLocal(newSong);
                    setChanged();
                    notifyObservers(response.body());
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(AppContext.getContext(), "Eroare server!", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            //Toast.makeText(AppContext.getContext(), "Server-ul nu e pornit!", Toast.LENGTH_LONG).show();
            updateLocal(newSong);
            setChanged();
            notifyObservers(true);
        }
    }

    private static class update implements Runnable {

        private SongDatabase songDb;
        private Song newSong;

        public update(SongDatabase songDb, Song newSong) {
            this.songDb = songDb;
            this.newSong = newSong;
        }

        @Override
        public void run() {
            songDb.update(newSong);
        }
    }

    private void updateLocal(Song newSong){
        Thread update = new Thread(new update(songDatabase, newSong));
        update.start();
        try {
            update.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendListToServer(List<Song> newSongList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TestResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TestResource api = retrofit.create(TestResource.class);
        Call<Boolean> call = api.replaceSongList(LoginService.token, newSongList);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body().equals(false)) {
                    throw new RuntimeException("An error occurred while replacing server data with local data.");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                throw new RuntimeException("Server error!");
            }
        });
    }

}
