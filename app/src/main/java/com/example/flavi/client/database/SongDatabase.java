package com.example.flavi.client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.flavi.client.model.Song;

import java.util.List;

@Dao
public interface SongDatabase {

    @Update
    void update(Song updateSong);

    @Insert
    void insert(Song newSong);

    @Query("SELECT * FROM song")
    LiveData<List<Song>> getSongs();

    @Query("DELETE FROM song")
    void delete();
}
