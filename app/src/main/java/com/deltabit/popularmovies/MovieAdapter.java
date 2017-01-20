package com.deltabit.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigel on 12/01/17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView movieTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageview_item_discovery);
            movieTitle = (TextView) itemView.findViewById(R.id.textview_item_discovery);
        }


    }
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieItemView = inflater.inflate(R.layout.item_discovery_movie,parent,false);

        ViewHolder viewHolder = new ViewHolder(movieItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder holder, int position) {
        final MovieModel movieModel = movies.get(position);

        holder.movieTitle.setText(movieModel.getTitle());
        Picasso.with(context)
                .load(Utilities.getMediumPosterUrlFor(movieModel,context))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caller.onMovieClicked(movieModel,holder.itemView);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        final View itemView;
//        final MovieModel movieModel = movies.get(position);
//
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            itemView = inflater.inflate(R.layout.item_discovery_movie, null);
//        }
//        else
//        {
//            itemView = convertView;
//        }
//
//        TextView title = (TextView) itemView.findViewById(R.id.textview_item_discovery);
//        title.setText(movieModel.getTitle());
//
//
//        if(movieModel.getPosterPath()!=null && !movieModel.getPosterPath().equals("")) {
//            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview_item_discovery);
//
//
//            Picasso.with(context).load(Utilities.getMediumPosterUrlFor(movieModel,context)).into(imageView);
//        }else{
//            Log.e(LOG_TAG,"error, missing posterPath on "+movieModel.getTitle()+" movie model.");
//        }
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                caller.onMovieClicked(movieModel,itemView);
//            }
//        });
//
//        return itemView;
//    }

    public void update(List<MovieModel> newMovies) {
//        Log.i(LOG_TAG,"Current movies list item count: "+movies.size());

        if(movies==null){
            movies = new ArrayList<>();
        }

        movies.clear();
        for(MovieModel movie : newMovies)
            if(movie.getPosterPath()!=null && !movie.getPosterPath().equals(""))
                movies.add(movie);

        //Log.i(LOG_TAG,"Current movies list item count: "+movies.size());

        notifyDataSetChanged();
    }
}
