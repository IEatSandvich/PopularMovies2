package com.conuirwilliamson.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.models.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by conuirwilliamson on 30/03/2017.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ViewHolder> {

    private ArrayList<Review> data;
    public void setData(ArrayList<Review> reviews){
        data = reviews;
        notifyDataSetChanged();
    }

    @Override
    public MovieReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieReviewsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { holder.bind(data.get(position)); }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_content) TextView reviewContent;
        @BindView(R.id.tv_review_author) TextView reviewAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Review review){
            reviewContent.setText(review.getContent());
            reviewAuthor.setText(review.getAuthor());
        }
    }
}