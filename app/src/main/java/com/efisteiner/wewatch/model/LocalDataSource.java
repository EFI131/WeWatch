package com.efisteiner.wewatch.model;

import android.app.Application;

import androidx.room.Room;

import java.util.List;
import io.reactivex.Observable;

public class LocalDataSource {
    private MovieDao movieDao;
    private Observable<List<Movie>> movies;

    public LocalDataSource(Application application){
        LocalDataBase db = LocalDataBase.getInstance(application);
        movieDao = db.movieDao();
        movies = movieDao.getAll();
    }

    public void insert(Movie movie){
        new Thread(){
            @Override
            public void run() {
                super.run();
                movieDao.insert(movie);
            }
        }.run();
    }

    public void delete(Movie movie){
        new Thread(){
            @Override
            public void run() {
                super.run();
                movieDao.delete(movie.getId());
            }
        }.run();
    }

    public void update(Movie movie){
        new Thread(){
            @Override
            public void run() {
                super.run();
                movieDao.insert(movie);
            }
        }.run();
    }

    public Observable<List<Movie>> getAllMovies(){
        return movies;
    }

}
