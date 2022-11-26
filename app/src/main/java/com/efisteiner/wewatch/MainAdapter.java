package com.efisteiner.wewatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efisteiner.wewatch.model.Movie;
import com.efisteiner.wewatch.network.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.MissingFormatArgumentException;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MoviesHolder> {

    private List<Movie> movieList;
    private Context context;
    // Hashmap to keep track of which items were selected for deletion
    private HashSet<Movie> selectedMovies = new HashSet<Movie>();

    public MainAdapter(List<Movie> movieList, Context context){
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public MainAdapter.MoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie_main, parent, false);
        return new MoviesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MoviesHolder holder, int position) {
        holder.titleTextView.setText(movieList.get(position).getTitle());
        holder.releaseDateTextView.setText(movieList.get(position).getReleaseDate());

        if(movieList.get(position).getPosterPath().equals("")){
            holder.movieImageView.setImageDrawable(context.getDrawable(R.drawable.ic_local_movies_gray));
        } else {
            Picasso.get().load(RetrofitClient.TMDB_IMAGE_URL + movieList.get(position).getPosterPath()).into(holder.movieImageView);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public HashSet<Movie> getSelectedMovies() {
        return selectedMovies;
    }

    public class MoviesHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView releaseDateTextView;
        private ImageView movieImageView;
        private CheckBox checkBox;

        public MoviesHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title_textview);
            releaseDateTextView = itemView.findViewById(R.id.release_date_textview);
            movieImageView = itemView.findViewById(R.id.movie_imageview);
            checkBox = itemView.findViewById(androidx.appcompat.R.id.checkbox);

            checkBox.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int adapterPosition = getAdapterPosition();
                            if(!selectedMovies.contains(movieList.get(adapterPosition))) {
                                checkBox.setChecked(true);
                                selectedMovies.add(movieList.get(adapterPosition));
                            } else {
                                checkBox.setChecked(false);
                                selectedMovies.add(movieList.get(adapterPosition));
                            }
                        }
                    }
            );
        }
    }
}
