package com.example.flavi.client.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.flavi.client.AppContext;
import com.example.flavi.client.api.TestResource;
import com.example.flavi.client.database.LocalDatabase;
import com.example.flavi.client.database.SongDatabase;
import com.example.flavi.client.model.Song;
import com.example.flavi.client.service.LoginService;
import com.example.flavi.client.service.SongService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SongsViewModel extends ViewModel {
    private static final String TAG = "tag";
    private MutableLiveData<List<Song>> songs;
    private MutableLiveData<List<Song>> localSongsList;
    private LocalDatabase localDb;
    private SongDatabase songDatabase;
    private SongListFragment songListFragment;
    public Boolean sendToServer = false;

    public void startLocalDb() {
        localDb = LocalDatabase.getDatabase(AppContext.getContext());
        songDatabase = localDb.songDatabase();
    }

    public SongsViewModel(SongListFragment songListFragment) {
        startLocalDb();
        this.songListFragment = songListFragment;
    }

    public LiveData<List<Song>> getSongs() {
        if (songs == null) {
            songs = new MutableLiveData<List<Song>>();
            localSongsList = new MutableLiveData<>();
            loadSongs();
        }
        return songs;
    }

    private void loadSongs() {

        if (!LoginService.offline) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TestResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TestResource api = retrofit.create(TestResource.class);
            Call<List<Song>> call = api.getSongs(LoginService.token);
            Log.d(TAG, "loadSongs");
            call.enqueue(new Callback<List<Song>>() {
                @Override
                public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                    Log.d(TAG, "loadSongs worked");
                    songs.setValue(response.body());
                    getSongsFromDB();
                    songs.observe(songListFragment, remoteSongs -> localSongsList.observe(songListFragment, localSong -> {
                        sendToServer = true;
                        songListFragment.sendData();
                    }));


                }

                @Override
                public void onFailure(Call<List<Song>> call, Throwable t) {
                    Log.e(TAG, "loadSongs failed", t);
                }
            });
        }
        else{
            getSongsFromDB();
            songs = localSongsList;
        }
    }

    public void sendData() {
        songs = localSongsList;
        localSongsList.observe(songListFragment, localSongs -> {
            SongService.sendListToServer(localSongs);
            songs = localSongsList;
        });
    }

    private void getSongsFromDB() {
        songDatabase.getSongs().observe(songListFragment, (songs) -> {
            if (songs == null) {
                return;
            }
            localSongsList.setValue(songs);
        });
    }

}
