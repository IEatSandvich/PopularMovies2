package com.conuirwilliamson.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conuirwilliamson.popularmovies.R;
import com.conuirwilliamson.popularmovies.models.Movie;
import com.conuirwilliamson.popularmovies.utilities.TheMovieDBUtil;
import com.conuirwilliamson.popularmovies.utilities.UIUtil;
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

        @BindView(R.id.rl_movie_item_root)  RelativeLayout rlMovieItemRoot;
        @BindView(R.id.iv_movie_poster)     ImageView ivMoviePoster;
        @BindView(R.id.tv_movie_title)      TextView tvMovieTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(final Movie movie){
            if(UIUtil.displayLinearLayoutMovies(context.getResources().getDisplayMetrics())){
                rlMovieItemRoot.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                rlMovieItemRoot.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            tvMovieTitle.setText("");
            Uri uri = TheMovieDBUtil.getImageUri(context, movie.getPosterPath(), R.string.main_poster_width);
            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .into(ivMoviePoster, new Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError() {
                            tvMovieTitle.setText(movie.getTitle());
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
