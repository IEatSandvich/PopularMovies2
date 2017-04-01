package com.conuirwilliamson.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private ArrayList<Trailer> data;
    public void setData(ArrayList<Trailer> trailers){
        data = trailers;
        notifyDataSetChanged();
    }

    private Context context;
    private TrailersAdapterOnClickHandler mClickHandler;

    public MovieTrailersAdapter(TrailersAdapterOnClickHandler clickHandler){ mClickHandler = clickHandler; }

    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MovieTrailersAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_trailer_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { holder.bind(data.get(position), position); }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_trailer_thumbnail) ImageView ivTrailerThumbnail;
        @BindView(R.id.tv_trailer_title) TextView tvTrailerTitle;
        @BindView(R.id.tv_trailer_size) TextView tvTrailerSize;

        String trailerUrl;

        private View currentView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            currentView = itemView;
            currentView.setOnClickListener(this);
        }

        void bind(final Trailer trailer, int position){
            if (position == 0) {
                currentView.setPaddingRelative((int)context.getResources().getDimension(R.dimen.trailer_item_margin_horizontal),0,0,0);
            } else if(position == getItemCount() - 1){
                currentView.setPaddingRelative(0,0,(int)context.getResources().getDimension(R.dimen.trailer_item_margin_horizontal),0);
            }

            trailerUrl = trailer.getSource();

            tvTrailerTitle.setText(trailer.getTrailerName());
            tvTrailerSize.setText(trailer.getSize());

            Picasso.with(context)
                    .load(trailer.getThumbnail())
                    .placeholder(R.drawable.ic_trailer_placeholder)
                    .into(ivTrailerThumbnail, null);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.handleClick(trailerUrl);
        }
    }

    public interface TrailersAdapterOnClickHandler{
        void handleClick(String url);
    }
}
