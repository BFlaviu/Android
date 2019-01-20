package com.example.flavi.client.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.flavi.client.model.Song;
import com.example.flavi.client.model.User;

@Database(entities = {Song.class, User.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract SongDatabase songDatabase();

    public abstract UserDatabase userDatabase();

    private static volatile LocalDatabase db;

    public static LocalDatabase getDatabase(final Context context) {
        if (db == null) {
            synchronized (LocalDatabase.class) {
                if (db == null) {
                    db = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "local_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return db;
    }

}
