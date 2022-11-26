package com.efisteiner.wewatch.network;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/popular")
    public Call<TmdbResponse> getPopularMovies(@Query("api_key")String api_key);

    @GET("search/movie")
    public Observable<TmdbResponse> searchMovies(@Query("api_key")String api_key, @Query("query") String query);
}
