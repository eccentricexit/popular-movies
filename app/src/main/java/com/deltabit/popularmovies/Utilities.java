package com.deltabit.popularmovies;

import android.content.Context;

/**
 * Created by rigel on 17/01/17.
 */

public class Utilities {


    public static String getMediumPosterUrlFor(MovieModel movieModel, Context context) {

        String posterPath = context.getString(R.string.poster_api_path)
                +context.getString(R.string.poster_size_medium) ;
        posterPath = posterPath + movieModel.getPosterPath();

        return posterPath;
    }

    public static String getBigPosterUrlFor(MovieModel movieModel, Context context) {

        String posterPath = context.getString(R.string.poster_api_path)
                +context.getString(R.string.poster_size_large);

        posterPath = posterPath + movieModel.getPosterPath();

        return posterPath;
    }
}
