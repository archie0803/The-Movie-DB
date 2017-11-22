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
import com.example.android.mymoviedb.Activities.MovieDetailActivity;
import com.example.android.mymoviedb.Activities.PersonDetailActivity;
import com.example.android.mymoviedb.IntentConstants;
import com.example.android.mymoviedb.Models.SearchResult;
import com.example.android.mymoviedb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Archita on 01-09-2017.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultViewHolder> {

    private Context mContext;
    private ArrayList<SearchResult.SearchRes> mSearchResults;

    public SearchResultAdapter(Context mContext, ArrayList<SearchResult.SearchRes> mSearchResults) {
        this.mContext = mContext;
        this.mSearchResults = mSearchResults;
    }

    @Override
    public SearchResultAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.search_result_view, parent, false);
        return new ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ResultViewHolder holder, int position) {

        if(mSearchResults.get(position).getPosterPath() != null){
            Glide.with(mContext.getApplicationContext()).load("https://image.tmdb.org/t/p/w342/" + mSearchResults.get(position).getPosterPath())
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.posterImageView);
        } else if(mSearchResults.get(position).getProfilePath() != null) {
            Glide.with(mContext.getApplicationContext()).load("https://image.tmdb.org/t/p/w342/" + mSearchResults.get(position).getProfilePath())
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.posterImageView);
        }

        if (mSearchResults.get(position).getName() != null && !mSearchResults.get(position).getName().trim().isEmpty())
            holder.nameTextView.setText(mSearchResults.get(position).getName());
        else if(mSearchResults.get(position).getTitle()!=null && !mSearchResults.get(position).getTitle().trim().isEmpty())
            holder.nameTextView.setText(mSearchResults.get(position).getTitle());
        else holder.nameTextView.setText(" ");

        if (mSearchResults.get(position).getMediaType() != null && mSearchResults.get(position).getMediaType().equals("movie"))
            holder.mediaTypeTextView.setText("Movie");
        else if (mSearchResults.get(position).getMediaType() != null && mSearchResults.get(position).getMediaType().equals("person"))
            holder.mediaTypeTextView.setText("Person");
        else
            holder.mediaTypeTextView.setText("");

        if (mSearchResults.get(position).getOverview() != null && !mSearchResults.get(position).getOverview().trim().isEmpty())
            holder.overviewTextView.setText(mSearchResults.get(position).getOverview());
        else
            holder.overviewTextView.setText("");

        if (mSearchResults.get(position).getReleaseDate() != null && !mSearchResults.get(position).getReleaseDate().trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(mSearchResults.get(position).getReleaseDate());
                holder.yearTextView.setText(sdf2.format(releaseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.yearTextView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_search)
        public CardView cardView;
        @BindView(R.id.image_view_poster_search)
        public ImageView posterImageView;
        @BindView(R.id.text_view_name_search)
        public TextView nameTextView;
        @BindView(R.id.text_view_media_type_search)
        public TextView mediaTypeTextView;
        @BindView(R.id.text_view_overview_search)
        public TextView overviewTextView;
        @BindView(R.id.text_view_year_search)
        public TextView yearTextView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            posterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            posterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSearchResults.get(getAdapterPosition()).getMediaType().equals("movie") && mSearchResults.get(getAdapterPosition()).getPosterPath() != null) {
                        Intent intent = new Intent(mContext, MovieDetailActivity.class);
                        intent.putExtra(IntentConstants.MOVIE_ID, mSearchResults.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                    } else if (mSearchResults.get(getAdapterPosition()).getMediaType().equals("person")) {
                        Intent intent = new Intent(mContext, PersonDetailActivity.class);
                        intent.putExtra(IntentConstants.CAST_ID, mSearchResults.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }
}

