package com.example.android.mymoviedb.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.android.mymoviedb.Activities.MovieDetailActivity;
import com.example.android.mymoviedb.IntentConstants;
import com.example.android.mymoviedb.Models.Movie;
import com.example.android.mymoviedb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Archita on 28-07-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MovieViewHolder> {

    Context mContext;
    ArrayList<Movie.Results> moviesList;
    int pos;


    public RecyclerAdapter(Context context, ArrayList<Movie.Results> moviesList) {
        mContext = context;
        this.moviesList = moviesList;
    }

    @Override
    public RecyclerAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false);
        return new MovieViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.MovieViewHolder holder, int position) {

        pos = position;

        Movie.Results b = moviesList.get(position);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        holder.movieImageButton.setLayoutParams(new LinearLayout.LayoutParams(width / 2, height / 2));
        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w500" + b.getPosterPath()).fit()
                .into(holder.movieImageButton);
    }

    @Override
    public int getItemCount() {
        if (moviesList.size() != 0)
            return moviesList.size();
        else return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton movieImageButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImageButton = itemView.findViewById(R.id.movie_image_button);
            movieImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Log.d("TAG", "CLICKED ON: " + adapterPosition);
                Intent i = new Intent(mContext, MovieDetailActivity.class);
                Log.d("TAG", "REACHED HERE");
                i.putExtra(IntentConstants.MOVIE_ID, moviesList.get(adapterPosition).getId());
                i.putExtra(IntentConstants.MOVIE_TITLE, moviesList.get(adapterPosition).getTitle());
                mContext.startActivity(i);
            }
        }

    }
}
