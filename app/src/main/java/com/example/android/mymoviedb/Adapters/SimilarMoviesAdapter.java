package com.example.android.mymoviedb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.mymoviedb.IntentConstants;
import com.example.android.mymoviedb.Models.Movie;
import com.example.android.mymoviedb.Activities.MovieDetailActivity;
import com.example.android.mymoviedb.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Archita on 21-08-2017.
 */

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie.Results> mSimilarMovies;

    public SimilarMoviesAdapter(Context context, ArrayList<Movie.Results> movies) {
        mContext = context;
        mSimilarMovies = movies;
    }

    @Override
    public SimilarMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.similar_movie_item_view, parent, false);
        return new SimilarMovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimilarMovieViewHolder holder, int position) {
        Glide.with(mContext.getApplicationContext())
                .load("https://image.tmdb.org/t/p/w342/" + mSimilarMovies.get(position).getPosterPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);

        if (mSimilarMovies.get(position).getTitle() != null)
            holder.movieTitleTextView.setText(mSimilarMovies.get(position).getTitle());
        else
            holder.movieTitleTextView.setText("");

    }

    @Override
    public int getItemCount() {
        return mSimilarMovies.size();
    }

    public class SimilarMovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_show_card)
        public CardView movieCard;
        @BindView(R.id.image_view_show_card)
        public ImageView moviePosterImageView;
        @BindView(R.id.text_view_title_show_card)
        public TextView movieTitleTextView;

        public SimilarMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            moviePosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            moviePosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(IntentConstants.MOVIE_ID, mSimilarMovies.get(getAdapterPosition()).getId());
                    intent.putExtra(IntentConstants.MOVIE_TITLE, mSimilarMovies.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
