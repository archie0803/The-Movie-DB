package com.example.android.mymoviedb;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Archita on 27-08-2017.
 */

public class PersonMovieAdapter extends RecyclerView.Adapter<PersonMovieAdapter.PersonMovieViewHolder> {

    Context mContext;
    ArrayList<PersonMovie> mMovieList;

    public PersonMovieAdapter(Context context, ArrayList<PersonMovie> movieList) {
        mContext = context;
        mMovieList = movieList;

    }

    @Override
    public PersonMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.similar_movie_item_view, parent, false);
        return new PersonMovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonMovieViewHolder holder, int position) {
        Glide.with(mContext.getApplicationContext())
                .load("https://image.tmdb.org/t/p/w342/" + mMovieList.get(position).getPosterPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);

        if (mMovieList.get(position).getTitle() != null)
            holder.movieTitleTextView.setText(mMovieList.get(position).getTitle());
        else
            holder.movieTitleTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class PersonMovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view_show_card)
        public CardView movieCard;
        @BindView(R.id.image_view_show_card)
        public ImageView moviePosterImageView;
        @BindView(R.id.text_view_title_show_card)
        public TextView movieTitleTextView;

        public PersonMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            moviePosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            moviePosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(IntentConstants.MOVIE_ID, mMovieList.get(getAdapterPosition()).getId());
                    intent.putExtra(IntentConstants.MOVIE_TITLE, mMovieList.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
