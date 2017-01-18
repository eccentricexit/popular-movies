package com.deltabit.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by rigel on 11/01/17.
 */

public class NetworkUtils {


    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String MOVIE_URL = "movie";
    public static final String TOP_RATED = "top_rated";
    public static final String POPULARITY = "popular";

    private static final String PARAM_API_KEY = "api_key";

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static Uri buildUriDiscovery(Context context, String sortFilter) {
        Uri.Builder builder = android.net.Uri.parse(TMDB_BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_URL);

        if(sortFilter.equals(TOP_RATED)) {
            builder.appendPath(TOP_RATED);
        }
        else {
            builder.appendPath(POPULARITY);
        }

        builder.appendQueryParameter(PARAM_API_KEY,context.getString(R.string.api_key));
        Uri uri = builder.build();

//        Log.i(LOG_TAG,uri.toString());

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
