package com.deltabit.popularmovies;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem_filter,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
