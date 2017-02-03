package com.deltabit.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.deltabit.popularmovies.R;
import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.data.MovieContract.MovieEntry;
import com.deltabit.popularmovies.data.MovieContract.PopularEntry;
import com.deltabit.popularmovies.data.MovieContract.TopRatedEntry;
import com.deltabit.popularmovies.model.MovieModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

/**
 * Created by rigel on 23/01/17.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, true);
        ContentResolver mContentResolver = context.getContentResolver();
    }

    public static void initializeSyncAdapter(Context context) {
//        Log.d(LOG_TAG,"Initializing SyncAdapter...");
        getSyncAccount(context);
        syncImmediately(context);
    }

    private static Account getSyncAccount(Context context) {
//        Log.d(LOG_TAG,"getSyncAccount executing...");
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (accountManager.getPassword(newAccount) == null) {
//            Log.d(LOG_TAG,"Account doesn't exist.");
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }

        return newAccount;

    }

    private static void onAccountCreated(Account newAccount, Context context) {
//        Log.d(LOG_TAG,"onAccountCreated executing...");
        ContentResolver.setIsSyncable(newAccount, MovieContract.CONTENT_AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(newAccount, MovieContract.CONTENT_AUTHORITY, true);
    }

    private static void syncImmediately(Context context) {
//        Log.d(LOG_TAG,"syncImmediately executing...");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

//        Log.d(LOG_TAG,"Requesting sync...");
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Log.d(LOG_TAG,"Performing sync...");

        URL popularMoviesUrl = MovieContract.buildURLFor(MovieContract.POPULAR_URL);
        URL topRatedUrl = MovieContract.buildURLFor(MovieContract.TOP_RATED_URL);

        String popularMoviesJson = getJsonResponseFor(popularMoviesUrl);
        String topRatedJson = getJsonResponseFor(topRatedUrl);

        syncMovies(popularMoviesJson,PopularEntry.CONTENT_URI);
        syncMovies(topRatedJson,TopRatedEntry.CONTENT_URI);
    }

    private void syncMovies(String json,Uri uri) {
        List<MovieModel> movies = getMoviesFromJSON(json);
        Vector<ContentValues> cVVector = getContentVVectorFor(movies);

        if(cVVector.size()>0){
            getContext().getContentResolver().delete(uri,null,null);

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            getContext().getContentResolver().bulkInsert(uri,cvArray);
        }

        Log.d(LOG_TAG,"Synced "+uri.getLastPathSegment()+". "+cVVector.size()+" added");

    }

    private Vector<ContentValues> getContentVVectorFor(List<MovieModel> movies) {
        Vector<ContentValues> contentValuesVector = new Vector<>(movies.size());

        for(MovieModel movie:movies){
            ContentValues values = new ContentValues();

            values.put(MovieEntry.COLUMN_POSTER_PATH,movie.getPosterPath());
            values.put(MovieEntry.COLUMN_ADULT,movie.getAdult());
            values.put(MovieEntry.COLUMN_OVERVIEW,movie.getOverview());
            values.put(MovieEntry.COLUMN_RELEASE_DATE,movie.getReleaseDate());
            values.put(MovieEntry.COLUMN_MOVIE_ID,movie.getId());
            values.put(MovieEntry.COLUMN_ORIGINAL_TITLE,movie.getOriginalTitle());
            values.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE,movie.getOriginalLanguage());
            values.put(MovieEntry.COLUMN_TITLE,movie.getTitle());
            values.put(MovieEntry.COLUMN_BACKDROP_PATH,movie.getBackdropPath());
            values.put(MovieEntry.COLUMN_POPULARITY,movie.getPopularity());
            values.put(MovieEntry.COLUMN_VOTE_COUNT,movie.getVoteCount());
            values.put(MovieEntry.COLUMN_VIDEO,movie.getVideo());
            values.put(MovieEntry.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());

            contentValuesVector.add(values);
        }
        return  contentValuesVector;
    }

    private List<MovieModel> getMoviesFromJSON(String json) {

        Type listType = new TypeToken<List<MovieModel>>(){}.getType();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            return new Gson().fromJson(jsonObject.get("results").toString(),listType);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getJsonResponseFor(URL url) {

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if(inputStream==null)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line; //for debugging purposes
            while((line = reader.readLine())!=null)
                buffer.append(line).append("\n");


            if(buffer.length()==0)
                return null;

            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
