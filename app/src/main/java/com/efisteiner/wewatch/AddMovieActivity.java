package com.efisteiner.wewatch;

import static com.efisteiner.wewatch.network.RetrofitClient.TMDB_IMAGE_URL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.efisteiner.wewatch.model.LocalDataSource;
import com.efisteiner.wewatch.model.Movie;
import com.squareup.picasso.Picasso;

public class AddMovieActivity extends AppCompatActivity {
    private final static String TAG = "AddMovieActivity";
    public final static int REQUEST_CODE = 1;
    private EditText titleEditText;
    private EditText releaseDateEditText;
    private ImageView movieImageView;
    private LocalDataSource localDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        setupViews();
        localDataSource = new LocalDataSource(getApplication());
    }

    private void setupViews() {
        titleEditText = findViewById(R.id.movie_title);
        releaseDateEditText = findViewById(R.id.movie_release_date);
        movieImageView = findViewById(R.id.movie_imageview);
    }

    public ActivityResultLauncher<Intent> activityForResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if(data!= null && data.getIntExtra("resultCode", -1) == SearchActivity.RESULT_CODE ){
                            final String title = data.getStringExtra(SearchActivity.EXTRA_TITLE);
                            final String releaseDate = data.getStringExtra(SearchActivity.EXTRA_RELEASE_DATE);
                            final String posterPath = data.getStringExtra(SearchActivity.EXTRA_POSTER_PATH);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(title != null)
                                        titleEditText.setText(title);
                                    if(releaseDate != null)
                                        releaseDateEditText.setText(releaseDate);
                                    if(posterPath != null) {
                                        movieImageView.setTag(posterPath);
                                        Picasso.get().load(TMDB_IMAGE_URL + posterPath).into(movieImageView);
                                    }

                                }
                            });
                        } else {
                            displayError("Movie could not be added.");
                        }

                    }
                }
            });

    // on search click
    public void goToSearchMovieActivity(View v){
        if(TextUtils.isEmpty(titleEditText.getText())){
            showToast("Movie Title Cannot be Empty");
        } else {
            String title = titleEditText.getText().toString();
            Intent intent = new Intent(AddMovieActivity.this, SearchActivity.class);
            intent.putExtra("requestCode", SearchActivity.REQUEST_CODE);
            intent.putExtra(SearchActivity.SEARCH_QUERY, title);

            activityForResultLauncher.launch(intent);
        }
    }

    public void onClickAddMovie(View v) {
        if(TextUtils.isEmpty(titleEditText.getText())) {
            showToast("Movie title cannot be empty");
        } else {
            String title = titleEditText.getText().toString();
            String releaseDate = releaseDateEditText.getText().toString();
            String posterPath = movieImageView.getTag() == null ? "" : movieImageView.getTag().toString();

            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setReleaseDate(releaseDate);
            movie.setPosterPath(posterPath);

            localDataSource.insert(movie);
            Intent intent = new Intent();
            intent.putExtra("requestCode", MainActivity.ADD_MOVIE_ACTIVITY_REQUEST_CODE);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    protected void showToast(String str){
        Toast.makeText(AddMovieActivity.this, str , Toast.LENGTH_LONG).show();
    }

    protected void displayError( String error ) {
        showToast(error);
    }
}