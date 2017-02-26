package com.deltabit.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.deltabit.popularmovies.data.MovieContract.FavoriteEntry;
import com.deltabit.popularmovies.data.MovieContract.PopularEntry;
import com.deltabit.popularmovies.data.MovieContract.TopRatedEntry;
import com.deltabit.popularmovies.data.MovieContract.*;

import org.abego.treelayout.internal.util.Contract;

/**
 * Created by rigel on 23/01/17.
 */

public class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;
    private static final int FAVORITE = 3;
    private static final int FAVORITE_WITH_ID = 4;

    private MovieDbHelper mMovieDbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case TOP_RATED: {
                return TopRatedEntry.CONTENT_TYPE;
            }
            case POPULAR: {
                return PopularEntry.CONTENT_TYPE;
            }
            case FAVORITE: {
                return FavoriteEntry.CONTENT_TYPE;
            }
            case FAVORITE_WITH_ID:{
                return FavoriteEntry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor resultCursor;
        if(selection!=null)
            selection = " 1=1 AND "+ selection;
        else
            selection = " 1=1 ";

        int match = uriMatcher.match(uri);

        switch (match) {
            case (TOP_RATED): {
                resultCursor = db.query(
                        TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case (POPULAR): {
                resultCursor = db.query(
                        PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case (FAVORITE): {
                String topRatedProjection =
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_POSTER_PATH+
                            " AS "+MovieEntry.COLUMN_POSTER_PATH +", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_ADULT+
                            " AS "+MovieEntry.COLUMN_ADULT+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_OVERVIEW+
                            " AS "+MovieEntry.COLUMN_OVERVIEW+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_RELEASE_DATE+
                            " AS "+MovieEntry.COLUMN_RELEASE_DATE+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_MOVIE_ID+
                            " AS "+MovieEntry.COLUMN_MOVIE_ID+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_ORIGINAL_TITLE+
                            " AS "+MovieEntry.COLUMN_ORIGINAL_TITLE+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_ORIGINAL_LANGUAGE+
                            " AS "+MovieEntry.COLUMN_ORIGINAL_LANGUAGE+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_TITLE+
                            " AS "+MovieEntry.COLUMN_TITLE+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_BACKDROP_PATH+
                            " AS "+MovieEntry.COLUMN_BACKDROP_PATH+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_POPULARITY+
                            " AS "+MovieEntry.COLUMN_POPULARITY+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_VOTE_COUNT+
                            " AS "+MovieEntry.COLUMN_VOTE_COUNT+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_VIDEO+
                            " AS "+MovieEntry.COLUMN_VIDEO+", "+
                        TopRatedEntry.TABLE_NAME+"."+MovieEntry.COLUMN_VOTE_AVERAGE+
                            " AS "+MovieEntry.COLUMN_VOTE_AVERAGE;


                String selectTopRatedFavorites = "SELECT "+topRatedProjection+" FROM "+
                        FavoriteEntry.TABLE_NAME+" INNER JOIN " +
                        TopRatedEntry.TABLE_NAME+" ON "+
                            FavoriteEntry.TABLE_NAME+"."+ MovieEntry.COLUMN_MOVIE_ID+" = "+
                            TopRatedEntry.TABLE_NAME+"."+ MovieEntry.COLUMN_MOVIE_ID;



                String popularProjection = PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_POSTER_PATH+
                        " AS "+MovieEntry.COLUMN_POSTER_PATH +", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_ADULT+
                        " AS "+MovieEntry.COLUMN_ADULT+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_OVERVIEW+
                        " AS "+MovieEntry.COLUMN_OVERVIEW+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_RELEASE_DATE+
                        " AS "+MovieEntry.COLUMN_RELEASE_DATE+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_MOVIE_ID+
                        " AS "+MovieEntry.COLUMN_MOVIE_ID+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_ORIGINAL_TITLE+
                        " AS "+MovieEntry.COLUMN_ORIGINAL_TITLE+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_ORIGINAL_LANGUAGE+
                        " AS "+MovieEntry.COLUMN_ORIGINAL_LANGUAGE+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_TITLE+
                        " AS "+MovieEntry.COLUMN_TITLE+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_BACKDROP_PATH+
                        " AS "+MovieEntry.COLUMN_BACKDROP_PATH+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_POPULARITY+
                        " AS "+MovieEntry.COLUMN_POPULARITY+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_VOTE_COUNT+
                        " AS "+MovieEntry.COLUMN_VOTE_COUNT+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_VIDEO+
                        " AS "+MovieEntry.COLUMN_VIDEO+", "+
                        PopularEntry.TABLE_NAME+"."+MovieEntry.COLUMN_VOTE_AVERAGE+
                        " AS "+MovieEntry.COLUMN_VOTE_AVERAGE;

                String selectPopularFavorites = "SELECT "+popularProjection+" FROM "+
                        FavoriteEntry.TABLE_NAME+" INNER JOIN " +
                        PopularEntry.TABLE_NAME+" ON "+
                        FavoriteEntry.TABLE_NAME+"."+ MovieEntry.COLUMN_MOVIE_ID+" = "+
                        PopularEntry.TABLE_NAME+"."+ MovieEntry.COLUMN_MOVIE_ID;

                Log.d(LOG_TAG,"topRatedSelect: "+selectTopRatedFavorites);
                Log.d(LOG_TAG,"popularSelect: "+selectPopularFavorites);

                String rawQuery = selectTopRatedFavorites + " UNION " +
                        selectPopularFavorites;

                resultCursor = db.rawQuery(rawQuery,selectionArgs);
                break;
            }
            case(FAVORITE_WITH_ID):{
                resultCursor = db.query(
                        FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        Log.d(LOG_TAG,"query() "+uri.toString()+" gave:"+ resultCursor.getCount());
        Log.d(LOG_TAG,"resultCursor has "+resultCursor.getColumnCount()+" columns");

        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return resultCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        Uri returnUri;
        switch (match) {
            case (TOP_RATED): {
                long _id = db.insert(
                        TopRatedEntry.TABLE_NAME,
                        null,
                        values
                );

                if (_id > 0)
                    returnUri = TopRatedEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            case (POPULAR): {
                long _id = db.insert(
                        PopularEntry.TABLE_NAME,
                        null,
                        values
                );

                if (_id > 0)
                    returnUri = PopularEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            case (FAVORITE): {
                long _id = db.insert(
                        FavoriteEntry.TABLE_NAME,
                        null,
                        values
                );
                if (_id > 0)
                    returnUri = FavoriteEntry.buildFavoriteWithIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        int rowsAffected;
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        switch (match) {
            case (TOP_RATED): {
                rowsAffected = db.delete(TopRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case (POPULAR): {
                rowsAffected = db.delete(PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case (FAVORITE): {
                rowsAffected = db.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (rowsAffected > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int rowsAffected;
        switch (match) {
            case (TOP_RATED): {
                rowsAffected = db.update(TopRatedEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case (POPULAR): {
                rowsAffected = db.update(TopRatedEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case (FAVORITE): {
                rowsAffected = db.update(TopRatedEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (rowsAffected > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsAffected;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int match = uriMatcher.match(uri);
        int rowsAffected;

        switch (match) {
            case (TOP_RATED): {
                rowsAffected = bulkInsertForTable(
                        TopRatedEntry.TABLE_NAME, values);
                break;
            }
            case (POPULAR): {
                rowsAffected = bulkInsertForTable(
                        PopularEntry.TABLE_NAME, values);
                break;
            }
            case (FAVORITE): {
                rowsAffected = bulkInsertForTable(
                        FavoriteEntry.TABLE_NAME, values);
                break;
            }
            default: {
                return super.bulkInsert(uri, values);
            }
        }

        if (rowsAffected > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return -1;
    }

    private int bulkInsertForTable(String tableName, ContentValues[] values) {
        int rowsAffected = 0;
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                long _id = db.insert(
                        tableName, null, value);
                if (_id != -1)
                    rowsAffected++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return rowsAffected;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.TopRatedEntry.CONTENT_PATH, TOP_RATED);
        matcher.addURI(authority, MovieContract.PopularEntry.CONTENT_PATH, POPULAR);
        matcher.addURI(authority, MovieContract.FavoriteEntry.CONTENT_PATH, FAVORITE);
        matcher.addURI(authority, MovieContract.FavoriteEntry.CONTENT_PATH+"/*",FAVORITE_WITH_ID);

        return matcher;
    }


}
