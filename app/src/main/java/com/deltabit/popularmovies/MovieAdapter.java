package com.deltabit.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rigel on 12/01/17.
 */
public class MovieAdapter extends BaseAdapter {
    Context context;
    List<Movie> movies;
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (view == null) {


            gridView = inflater.inflate(R.layout.item_discovery_movie, null);

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.imageview_item_discovery);

            String url = movies.get(position).getPosterPath();
            Picasso.with(context).load(url).into(imageView);


        } else {
            gridView = view;
        }

        return gridView;

    }
}
