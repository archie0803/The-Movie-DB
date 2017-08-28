package com.example.android.mymoviedb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mymoviedb.R;
import com.example.android.mymoviedb.Models.Review;

import java.util.ArrayList;

/**
 * Created by Archita on 03-08-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    Context mContext;
    ArrayList<Review.ReviewResult> mReviewResults;

    public ReviewAdapter(Context context, ArrayList<Review.ReviewResult> reviewResults) {
        mContext = context;
        mReviewResults = reviewResults;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_view, parent, false);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review.ReviewResult r = mReviewResults.get(position);
        holder.authorName.setText(r.getAuthor());
        holder.reviewContent.setText(r.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewResults.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        TextView authorName, reviewContent;

        public ReviewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_text_View);
            reviewContent = itemView.findViewById(R.id.review_content);

        }
    }
}
