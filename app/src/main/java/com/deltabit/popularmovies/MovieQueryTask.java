package com.deltabit.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;

/**
 * Created by rigel on 12/01/17.
 */

public class MovieQueryTask extends AsyncTask<URL,Void,String> {

    private static final String LOG_TAG = MovieQueryTask.class.getSimpleName();
    OnTaskComplete caller;
    public MovieQueryTask(OnTaskComplete caller) {
        this.caller = caller;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(URL... urls) {
        String results = "";
        try {
            results = NetworkUtils.getResponseFromUrl(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
            caller.showErrorMessage();
        }

        return results;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null && !s.equals("")) {
            Log.i(LOG_TAG, s);
            caller.onTaskCompleted(s);
        }
        else
            caller.showErrorMessage();

    }

}
