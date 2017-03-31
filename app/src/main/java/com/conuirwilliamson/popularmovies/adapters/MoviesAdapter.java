package com.conuirwilliamson.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public final MoviesAdapterOnClickHandler mClickHandler;

    private ArrayList<Movie> data;
    public void setData(ArrayList<Movie> movies){
        data = movies;
        notifyDataSetChanged();
    }

    private Context context;

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MoviesAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { holder.bind(data.get(position)); }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster) ImageView mMoviePosterImage;
        @BindView(R.id.tv_movie_title)  TextView mMovieTitleDisplay;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(final Movie movie){
            mMovieTitleDisplay.setText("");
            String url = TheMovieDBUtil.getImageUrl(context, movie.getPosterPath(), R.string.w_342).toString();
            Picasso.with(context)
                    .load(url)
                    .into(mMoviePosterImage, new Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError() {
                            mMovieTitleDisplay.setText(movie.getTitle());
                        }
                    });
        }

        @Override
        public void onClick(View v) { mClickHandler.handleClick(data.get(getAdapterPosition()).getID()); }
    }

    public interface MoviesAdapterOnClickHandler{
        void handleClick(int movieId);
    }

}
