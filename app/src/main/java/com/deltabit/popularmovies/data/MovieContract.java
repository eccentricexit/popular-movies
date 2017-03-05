package com.deltabit.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.deltabit.popularmovies.BuildConfig;
import com.deltabit.popularmovies.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rigel on 23/01/17.
 */

public class MovieContract {

    private static final String LOG_TAG = MovieContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.deltabit.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIE_URL = "movie";
    public static final String POPULAR_URL = "popular";
    public static final String TOP_RATED_URL = "top_rated";
    public static final String REVIEWS_URL = "reviews";
    public static final String TRAILERS_URL = "videos";

    public static final String PARAM_API_KEY = "api_key";

    private static Uri.Builder getMovieBuilder() {
        return Uri.parse(TMDB_BASE_URL).buildUpon().appendPath(MOVIE_URL);
    }

    public static class MovieEntry implements BaseColumns{
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_ADULT = "isAdult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "originalLanguage";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_VIDEO = "hasVideo";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

    }

    public static class FavoriteEntry implements BaseColumns {
        public static final String CONTENT_PATH = "favorite";
        public static final String TABLE_NAME = "favorite";
        
        public static final String COLUMN_MOVIE_ID = "movieId";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_PATH;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CONTENT_PATH).build();



        public static Uri buildFavoriteWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static class TopRatedEntry extends MovieEntry{
        public static final String CONTENT_PATH = "top_rated";
        public static final String TABLE_NAME = "top_rated";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_PATH;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CONTENT_PATH).build();


        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class PopularEntry extends MovieEntry{
        public static final String CONTENT_PATH = "popular";
        public static final String TABLE_NAME = "popular";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_PATH;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CONTENT_PATH).build();

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static URL buildURLFor(String path) {
        Uri.Builder builderPopular = MovieContract.getMovieBuilder();
        builderPopular.appendPath(path);
        builderPopular.appendQueryParameter(MovieContract.PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_KEY);

        URL urlResult = null;
        try {
            urlResult = new URL(builderPopular.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlResult;
    }

    public static String getMediumPosterUrlFor(String partialUrl, Context context) {

        String posterPath = context.getString(R.string.poster_api_path)
                +context.getString(R.string.poster_size_medium) ;
        posterPath = posterPath + partialUrl;

        return posterPath;
    }
}
