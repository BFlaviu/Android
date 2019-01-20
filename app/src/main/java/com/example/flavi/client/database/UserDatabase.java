package com.example.flavi.client.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.flavi.client.model.User;

@Dao
public interface UserDatabase {

    @Query("SELECT COUNT(*) FROM user WHERE username = :username AND password = :password")
    Integer checkLogin(String username, String password);

    @Insert
    void insert(User user);
}
