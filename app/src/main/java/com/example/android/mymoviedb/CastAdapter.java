package com.example.android.mymoviedb;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Archita on 03-08-2017.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    Context mContext;
    ArrayList<CastAndCrew.Cast> mCastList;
    int pos;

    public CastAdapter(Context context, ArrayList<CastAndCrew.Cast> castList) {
        mContext = context;
        mCastList = castList;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item_view, parent, false);
        return new CastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        pos = position;

        CastAndCrew.Cast c = mCastList.get(position);
        holder.castNameTextView.setText(c.getName());
        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w500" + c.getProfile_path()).fit()
                .into(holder.castImageView);
    }

    @Override
    public int getItemCount() {
        return mCastList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {

        ImageView castImageView;
        TextView castNameTextView;

        public CastViewHolder(View itemView) {
            super(itemView);
            castImageView = itemView.findViewById(R.id.cast_image_view);
            castNameTextView = itemView.findViewById(R.id.cast_name);
        }

    }
}
