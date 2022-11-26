package com.efisteiner.wewatch;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.efisteiner.wewatch.model.Movie;
import com.efisteiner.wewatch.network.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchMoviesHolder> {
    private static final String TAG = "SEARCH_ADAPTER";
    private final List<Movie> movieList;
    private final Context context;
    private final SearchActivity.RecyclerItemListener itemListener;

    public SearchAdapter(List<Movie> movieList, Context context, SearchActivity.RecyclerItemListener listener){
        this.movieList = movieList;
        this.context = context;
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchMoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_details, parent, false);

        SearchMoviesHolder viewHolder = new SearchMoviesHolder(view);
        view.setOnClickListener(v -> itemListener.onItemClick(v, viewHolder.getAdapterPosition()));
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchMoviesHolder holder, int position) {
           holder.titleTextView.setText(movieList.get(position).getTitle());
           holder.releaseDateTextView.setText(movieList.get(position).getReleaseDate());
           holder.overviewTextView.setText(movieList.get(position).getOverview());

        if(movieList.get(position).getPosterPath() != null )
            Picasso.get().load(RetrofitClient.TMDB_IMAGE_URL + movieList.get(position).getPosterPath()).into(holder.movieImageView);
        else
            holder.movieImageView.setImageDrawable( AppCompatResources.getDrawable(context, R.drawable.ic_movie_teal_24dp));
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

    public Movie getItemAtPosition(Integer position) {
        return movieList.get(position);
    }

    public class SearchMoviesHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView overviewTextView;
        private final TextView releaseDateTextView;
        private final ImageView movieImageView;
        public SearchMoviesHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.details_title_textview);
            overviewTextView = itemView.findViewById(R.id.details_overview_overview);
            releaseDateTextView = itemView.findViewById(R.id.details_release_date_textview);
            movieImageView = itemView.findViewById(R.id.details_movie_imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
