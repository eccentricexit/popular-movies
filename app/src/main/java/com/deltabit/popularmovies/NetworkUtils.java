package com.deltabit.popularmovies;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by rigel on 11/01/17.
 */

public class NetworkUtils {

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String DISCOVER_URL = "discover";
    private static final String MOVIE_URL = "movie";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static Uri buildUri(Context context, String sortFilter) {

        android.net.Uri uri = android.net.Uri.parse(TMDB_BASE_URL)
                .buildUpon()
                .appendPath(DISCOVER_URL)
                .appendPath(MOVIE_URL)
                .appendQueryParameter(PARAM_API_KEY, context.getString(R.string.api_key))
                .appendQueryParameter(PARAM_SORT_BY, sortFilter)
                .build();

        return uri;
    }

    public static Uri buildUri(Context context) {

        android.net.Uri uri = android.net.Uri.parse(TMDB_BASE_URL)
                .buildUpon()
                .appendPath(DISCOVER_URL)
                .appendPath(MOVIE_URL)
                .appendQueryParameter(PARAM_API_KEY,context.getString(R.string.api_key))
                .build();

        return uri;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
//        Log.i(LOG_TAG,url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput)
                return scanner.next();
            else
                return null;
        }finally {
            urlConnection.disconnect();
        }
    }
}
