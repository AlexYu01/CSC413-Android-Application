package com.example.team33.groupfinder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/*
 * Created by abhijit on 11/20/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> movieList;
    private OnClickListener listener;

    public RecyclerViewAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card_layout, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.setTitle(movie.getTitle());
        cardViewHolder.setYear(movie.getYear());
        cardViewHolder.setPosterUrl(movie.getPosterUrl());
        if(listener!=null) {
            cardViewHolder.bindClickListener(listener, movie);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * Removes older data from movieList and update it.
     * Once the data is updated, notifies RecyclerViewAdapter.
     * @param modelList list of movies
     */
    public void updateDataSet(List<Movie> modelList) {
        this.movieList.clear();
        this.movieList.addAll(modelList);
        notifyDataSetChanged();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onCardClick(Movie movie);
        void onPosterClick(Movie movie);
    }

    /**
     *  CardViewHolder will hold the layout of the each item in the RecyclerView.
     */
    private class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView title;
        private TextView year;
        private NetworkImageView poster;

        /**
         * Class constructor.
         * @param view  layout of each item int the RecyclerView
         */
        CardViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
            this.title = (TextView) view.findViewById(R.id.tvTitle);
            this.year = (TextView) view.findViewById(R.id.tvYear);
            this.poster = (NetworkImageView) view.findViewById(R.id.nivPoster);
        }

        /**
         * append title text to Title:
         * @param title String of Title of movie
         */
        void setTitle(String title) {
            String t = "Title:\n" + title;
            this.title.setText(t);
        }

        /**
         * append year text to Release Year:
         * @param year String of year of release
         */
        void setYear(String year) {
            String y = "Release Year:\n" + year;
            this.year.setText(y);
        }

        /**
         * Sends ImageRequest using volley using imageLoader and Cache.
         * This is pre-implemented feature of Volley to cache images for faster responses.
         * Check VolleySingleton class for more details.
         * @param imageUrl URL to poster of the Movie
         */
        void setPosterUrl(String imageUrl) {
            ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();
            this.poster.setImageUrl(imageUrl, imageLoader);
        }

        /**
         *
         * @param listener {@link OnClickListener}
         * @param movie
         */
        void bindClickListener(final OnClickListener listener, final Movie movie){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardClick(movie);
                }
            });

            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPosterClick(movie);
                }
            });
        }
    }
}
