package com.efisteiner.wewatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efisteiner.wewatch.model.Movie;
import com.efisteiner.wewatch.model.RemoteDataSource;
import com.efisteiner.wewatch.network.MovieResponse;
import com.efisteiner.wewatch.network.RetrofitClient;
import com.efisteiner.wewatch.network.TmdbResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";
    public static final String EXTRA_TITLE = "SearchActivity.TITLE_REPLY";
    public static final String EXTRA_RELEASE_DATE = "SearchActivity.RELEASE_DATE_REPLY";
    public static final String EXTRA_POSTER_PATH = "SearchActivity.POSTER_PATH_REPLY";
    public static final String SEARCH_QUERY = "searchQuery";
    public static final int REQUEST_CODE = 2;
    public static final int RESULT_CODE = 2;

    private RecyclerView searchResultsRecyclerView;
    private SearchAdapter adapter;
    private TextView noMoviesTextView;
    private ProgressBar progressBar;

    private final RemoteDataSource dataSource = new RemoteDataSource();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noMoviesTextView = findViewById(R.id.no_movies_textview);
        progressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        query = intent.getStringExtra(SEARCH_QUERY);
    }

    private void getPopularMovies() {
        Call<TmdbResponse> call = RetrofitClient.getInstance().getApi().getPopularMovies(RetrofitClient.API_KEY);

        call.enqueue(new Callback<TmdbResponse>() {
            @Override
            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayResult(response.body());
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<TmdbResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }


//    private void searchMovies(String query){
//        Call<TmdbResponse> call = RetrofitClient.getInstance().getApi().searchMovies(RetrofitClient.API_KEY, query);
//
//        call.enqueue(new Callback<TmdbResponse>() {
//            @Override
//            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        displayResult(response.body());
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<TmdbResponse> call, Throwable t) {
//
//            }
//        });
//    }
    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        getSearchResults(query);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(compositeDisposable != null)
//            compositeDisposable.clear();
//    }
//
    private void setupViews() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
//
//
    private Observable<TmdbResponse> searchResultsObservable(String query) {
        return dataSource.searchResultsObservable(query);
    }

    private DisposableObserver<TmdbResponse> observer = new DisposableObserver<TmdbResponse>() {
        @Override
        public void onNext(TmdbResponse tmdbResponse) {
            Log.d(TAG, "OnNext" + tmdbResponse.totalResults);
            displayResult(tmdbResponse);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "Error$"+e);
            e.printStackTrace();
            displayError("Error fetching Movie Data");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "Completed");
        }
    };

    private void getSearchResults(String query) {
        Disposable searchResultsDisposable = searchResultsObservable(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);

        compositeDisposable.add(searchResultsDisposable);
    }

    private void displayResult(TmdbResponse tmdbResponse) {
        progressBar.setVisibility(View.INVISIBLE);

        if(tmdbResponse.totalResults == null || tmdbResponse.totalResults ==  0) {
            searchResultsRecyclerView.setVisibility(View.INVISIBLE);
            noMoviesTextView.setVisibility(View.VISIBLE);
        } else {
            adapter = new SearchAdapter(tmdbResponse.results, SearchActivity.this, itemListener);
            searchResultsRecyclerView.setAdapter(adapter);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            noMoviesTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String string) {
        Toast.makeText(SearchActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private void displayError(String string) {
        showToast(string);
    }

    public RecyclerItemListener itemListener = new RecyclerItemListener() {
        @Override
        public void onItemClick(View v, Integer position) {
            Movie movie = adapter.getItemAtPosition(position);

            Intent replyIntent = new Intent();
            replyIntent.putExtra("resultCode", SearchActivity.RESULT_CODE);
            replyIntent.putExtra(EXTRA_TITLE, movie.getTitle());
            replyIntent.putExtra(EXTRA_RELEASE_DATE, movie.getReleaseDate());
            replyIntent.putExtra(EXTRA_POSTER_PATH, movie.getPosterPath());
            setResult(Activity.RESULT_OK, replyIntent);

            finish();
        }
    };

    public interface RecyclerItemListener {
        public void onItemClick(View v, Integer position);
    }

}