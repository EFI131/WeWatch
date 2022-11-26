package com.efisteiner.wewatch.model;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie_table")
    Observable<List<Movie>> getAll();

    @Insert(onConflict = REPLACE)
    void insert(Movie movie);

    @Query("DELETE FROM movie_table WHERE id = :id")
    void delete(Integer id);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Update
    void update(Movie movie);
}
