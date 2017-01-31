package com.deltabit.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.deltabit.popularmovies.sync.SyncAdapter;

public class MoviesActivity extends AppCompatActivity {

    private static final String LOG_TAG = MoviesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem_filter,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String filterKey = this.getString(R.string.filter_key);
        String filterTopRated = this.getString(R.string.filter_topRated);
        String filterPopularity = this.getString(R.string.filter_popularity);

        switch (itemId){
            case(R.id.action_sort_by_top_rated): {
//                Log.d(LOG_TAG,"changing sorting to top rated");
                editor.putString(filterKey,filterTopRated);
                editor.apply();

                break;
            }
            case(R.id.action_sort_by_popularity):{
//                Log.d(LOG_TAG,"changing sorting to popularity");
                editor.putString(filterKey,filterPopularity);
                editor.apply();

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
