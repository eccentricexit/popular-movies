package com.deltabit.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.data.MovieContract.MovieEntry;
import com.deltabit.popularmovies.data.MovieDbHelper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static junit.framework.Assert.*;

/**
 * Created by rigel on 23/01/17.
 */

@RunWith(AndroidJUnit4.class)
public class MovieDbHelperTest {

    private static final String LOG_TAG = MovieDbHelperTest.class.getSimpleName();
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final Class mDbHelperClass = MovieDbHelper.class;

    @Before
    public void setUp(){
        Log.i(LOG_TAG,"setUp called");
        deleteDatabase();
    }

    @Test
    public void createDatabaseTest() throws Exception {
        SQLiteOpenHelper helper = (SQLiteOpenHelper) mDbHelperClass
                .getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = helper.getWritableDatabase();
        final String ERROR_DATABASE_CLOSED = "The database should be open.";

        assertEquals(ERROR_DATABASE_CLOSED,true,database.isOpen());

        Cursor tablesCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        MovieContract.MovieEntry.TABLE_NAME + "'",
                null);

        final String ERROR_NO_TABLES = "No tables in database.";
        assertEquals(ERROR_NO_TABLES,true,tablesCursor.moveToFirst());

        final String ERROR_MISSING_TABLE = "The database is missing table "+ MovieContract.MovieEntry.TABLE_NAME;
        assertEquals(ERROR_MISSING_TABLE, MovieContract.MovieEntry.TABLE_NAME,tablesCursor.getString(0));

        tablesCursor.close();
    }

    @Test
    public void insertRowInMoviesTable() throws Exception {
        SQLiteOpenHelper helper = (SQLiteOpenHelper) mDbHelperClass
                .getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = helper.getWritableDatabase();
        final String ERROR_DATABASE_CLOSED = "The database should be open.";

        assertEquals(ERROR_DATABASE_CLOSED,true,database.isOpen());


        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE,"test_movie");

        long firstRow = database.insert(MovieEntry.TABLE_NAME,null,contentValues);

        final String ERROR_INSERTING_ROW = "Error: Could not insert into "+MovieEntry.TABLE_NAME;
        assertFalse(ERROR_INSERTING_ROW,firstRow==-1);

        Cursor cursor = database.query(
                MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        String ERROR_BAD_COLUMN_COUNT = "Error: Table "+MovieEntry.TABLE_NAME+" should have one row";
        assertEquals(ERROR_BAD_COLUMN_COUNT,1,cursor.getCount());



        cursor.close();

        database.close();
    }

    private void deleteDatabase() {
        try {
            Field f = mDbHelperClass.getDeclaredField("DB_NAME");
            f.setAccessible(true);

            mContext.deleteDatabase((String) f.get(null));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
