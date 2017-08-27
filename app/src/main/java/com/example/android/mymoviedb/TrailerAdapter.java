package com.example.android.mymoviedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.key;

/**
 * Created by Archita on 04-08-2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    ArrayList<Trailer.TrailerResults> trailerResults;
    Context mContext;

    public TrailerAdapter(Context mContext, ArrayList<Trailer.TrailerResults> trailerResults) {
        this.trailerResults = trailerResults;
        this.mContext = mContext;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_list, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load("http://img.youtube.com/vi/" + trailerResults.get(position).getKey() + "/hqdefault.jpg")
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.trailer);

        if (trailerResults.get(position).getName() != null)
            holder.videoTextView.setText(trailerResults.get(position).getName());
        else
            holder.videoTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return trailerResults.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_thumbnail)
        ImageView trailer;
        @BindView(R.id.card_view_video)
        CardView videoCard;
        @BindView(R.id.text_view_video_name)
        TextView videoTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            videoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(IntentConstants.YOUTUBE_PREFIX + trailerResults.get(getAdapterPosition()).getKey()));
                    mContext.startActivity(youtubeIntent);
                }
            });
        }
    }
}
