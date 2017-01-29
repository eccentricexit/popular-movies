package com.deltabit.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deltabit.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by rigel on 23/01/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "popular_movies.db";
    private static final int DB_VERSION = 1;

    public static final int COL_POSTER = 1;
    public static final int COL_ADULT = 2;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_RELEASE_DATE = 4;
    public static final int COL_MOVIE_ID = 5;
    public static final int COL_ORIGINAL_TITLE = 6;
    public static final int COL_ORIGINAL_LANGUAGE = 7 ;
    public static final int COL_TITLE = 8;
    public static final int COL_BACKDROP_PATH = 9;
    public static final int COL_POPULARITY = 10;
    public static final int COL_VOTE_COUNT = 11;
    public static final int COL_VIDEO = 12;
    public static final int COL_VOTE_AVERAGE = 13;



    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_MOVIE_ENTRY_COLUMNS = " (" +

                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieEntry.COLUMN_POSTER_PATH+" TEXT, "+
                MovieEntry.COLUMN_ADULT + " INTEGER DEFAULT 0, "+
                MovieEntry.COLUMN_OVERVIEW+" TEXT, "+
                MovieEntry.COLUMN_RELEASE_DATE+" INTEGER, "+
                MovieEntry.COLUMN_MOVIE_ID+" TEXT UNIQUE, "+
                MovieEntry.COLUMN_ORIGINAL_TITLE+" TEXT, "+
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE+" TEXT, "+
                MovieEntry.COLUMN_TITLE+" TEXT,"+
                MovieEntry.COLUMN_BACKDROP_PATH +" TEXT, "+
                MovieEntry.COLUMN_POPULARITY+" REAL, "+
                MovieEntry.COLUMN_VOTE_COUNT+" INTEGER,"+
                MovieEntry.COLUMN_VIDEO+" INT DEFAULT 0,"+
                MovieEntry.COLUMN_VOTE_AVERAGE+" REAL "+

                ");";

        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE "+
                MovieContract.TopRatedEntry.TABLE_NAME+
                SQL_MOVIE_ENTRY_COLUMNS;

        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE "+
                MovieContract.PopularEntry.TABLE_NAME+
                SQL_MOVIE_ENTRY_COLUMNS;

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE "+
                MovieContract.FavoriteEntry.TABLE_NAME+" ( "+
                MovieContract.FavoriteEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+" TEXT UNIQUE NOT NULL "+
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieContract.FavoriteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieContract.PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieContract.TopRatedEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
