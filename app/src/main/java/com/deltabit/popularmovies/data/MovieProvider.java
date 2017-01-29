package com.deltabit.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.deltabit.popularmovies.data.MovieContract.*;

/**
 * Created by rigel on 23/01/17.
 */

public class MovieProvider extends ContentProvider {

    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;
    private static final int FAVORITE = 3;
    private MovieDbHelper mMovieDbHelper;
    public static final UriMatcher uriMatcher = buildUriMatcher();


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
                    returnUri = FavoriteEntry.buildUri(_id);
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

        return matcher;
    }


}
