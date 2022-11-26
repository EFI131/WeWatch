package com.efisteiner.wewatch.model;

import com.efisteiner.wewatch.network.RetrofitClient;
import com.efisteiner.wewatch.network.TmdbResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {
    public Observable<TmdbResponse> searchResultsObservable(String query){
        return RetrofitClient
                .getInstance().getApi().searchMovies(RetrofitClient.API_KEY, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
