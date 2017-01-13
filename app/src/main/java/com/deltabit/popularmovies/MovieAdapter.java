package com.deltabit.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigel on 12/01/17.
 */
public class MovieAdapter extends BaseAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    Context context;
    List<MovieModel> movies;
    OnMovieClicked caller;

    public MovieAdapter(Context context, List<MovieModel> movies, OnMovieClicked caller) {
        super();
        this.context = context;
        this.movies = movies;
        this.caller = caller;

    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return buildMovieItem(position, convertView);
    }

    @NonNull
    private View buildMovieItem(int position, View convertView) {
        View itemView;
        final MovieModel movieModel = movies.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item_discovery_movie, null);
        }
        else
        {
            itemView = convertView;
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caller.onMovieClicked(movieModel);
            }
        });

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview_item_discovery);
        loadImageInto(imageView, movieModel);

        return itemView;
    }

    private void loadImageInto(ImageView imageView,MovieModel movieModel) {
        String posterPath = context.getString(R.string.poster_api_path);
        posterPath = posterPath + movieModel.getPosterPath();
        Picasso.with(context).load(posterPath).into(imageView);
    }


    public void update(List<MovieModel> newMovies) {
//        Log.i(LOG_TAG,"Current movies list item count: "+movies.size());

        if(movies==null){
            movies = new ArrayList<>();
        }

        movies.clear();
        movies.addAll(newMovies);

        Log.i(LOG_TAG,"Current movies list item count: "+movies.size());

        notifyDataSetChanged();
    }
}
