package com.deltabit.popularmovies;

import android.content.Context;
import android.graphics.Color;

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

    public static int lighten(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(150, red, green, blue);
    }
}
