package com.deltabit.popularmovies;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.sync.SyncAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncAdapter.initializeSyncAdapter(this);

        Log.d(LOG_TAG, MovieContract.PopularEntry.CONTENT_URI.toString());

        Cursor cursor = getContentResolver().query(
                MovieContract.TopRatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
                );


        Log.d(LOG_TAG,"cursor size: "+cursor.getCount());

        cursor.close();
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
                Log.d(LOG_TAG,"changing sorting to top rated");
                editor.putString(filterKey,filterTopRated);
                editor.commit();

                break;
            }
            case(R.id.action_sort_by_popularity):{
                Log.d(LOG_TAG,"changing sorting to popularity");
                editor.putString(filterKey,filterPopularity);
                editor.commit();

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
