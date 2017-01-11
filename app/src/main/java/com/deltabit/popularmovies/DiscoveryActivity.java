package com.deltabit.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoveryActivity extends AppCompatActivity {

    private static final String LOG_TAG = DiscoveryActivity.class.getSimpleName();

    private static final String POPULARITY = "popularity.desc";
    private static final String DATE = "release_date.asc";
    private static String sortFilter = POPULARITY;

    @BindView(R.id.gridview_discovery_movies) GridView gridViewMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = NetworkUtils.buildUri(this,sortFilter);
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new MovieQueryTask().execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem_filter,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case(R.id.action_sort_by_date): {
                sortFilter = DATE;
                break;
            }
            case(R.id.action_sort_by_popularity):{
                sortFilter = POPULARITY;
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public class MovieQueryTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            String results = "";
            try {
                results = NetworkUtils.getResponseFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(LOG_TAG,s);
        }
    }
}
