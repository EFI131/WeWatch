package com.efisteiner.wewatch.model;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(IntegerListTypeConverter.class)
public abstract class LocalDataBase extends RoomDatabase {
    private static Object lock = new Object();
    private static final String DB_NAME = "movie_database";
    private static LocalDataBase INSTANCE;

    public static LocalDataBase getInstance(Application application) {
        synchronized (lock) {
            if(INSTANCE == null){
                INSTANCE =
                        Room.databaseBuilder(application,LocalDataBase.class, DB_NAME)
                                .allowMainThreadQueries().build();
            }
        }

        return INSTANCE;
    }

    public abstract MovieDao movieDao();


}
