package com.example.flavi.client.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "song")
public class Song {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    Integer id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "releaseYear")
    private String releaseYear;
    @ColumnInfo(name = "album")
    private String album;

    public Song(@NonNull Integer id, String name,String album,String releaseYear) {
        this.id = id;
        this.name = name;
        this.album = album;
        this.releaseYear = releaseYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
