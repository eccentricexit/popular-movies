package com.deltabit.popularmovies;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rigel on 29/01/17.
 */

public class MovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String filterKey = this.getString(R.string.filter_key);

        if(!sharedPreferences.contains(filterKey)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(filterKey,this.getString(R.string.filter_default));
            editor.apply();
        }

    }
}
