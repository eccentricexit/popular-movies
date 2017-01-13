package com.deltabit.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoveryActivity extends AppCompatActivity implements OnTaskComplete, OnMovieClicked {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = DiscoveryActivity.class.getSimpleName();
    private static final String FILTER_KEY = "saved_filter";

    private static final String DATE = "release_date.desc";
    private static final String POPULARITY = "popularity.desc";
    private static final String DEFAULT_FILTER = POPULARITY;

    private static String sortFilter = DEFAULT_FILTER;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    MovieAdapter adapter;


    @BindView(R.id.gridview_discovery_movies) GridView gridViewMovies;
    @BindView(R.id.progressbar_discovery) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        adapter = new MovieAdapter(this,new ArrayList<MovieModel>(),this);
        gridViewMovies.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sharedPreferences.contains(FILTER_KEY)) {
            sortFilter = sharedPreferences.getString(FILTER_KEY, DEFAULT_FILTER);
        }
        else{
            editor.putString(FILTER_KEY,DEFAULT_FILTER);
            editor.commit();

            sortFilter = DEFAULT_FILTER;
        }

        updateGridView();

    }

    private void updateGridView() {
        try {
            Uri uri = NetworkUtils.buildUri(this,sortFilter);
            URL url = new URL(uri.toString());

            progressBar.setVisibility(View.VISIBLE);

            new MovieQueryTask(this).execute(url);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,e.getMessage());
            showErrorMessage();
            e.printStackTrace();
        }
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
                editor.putString(FILTER_KEY,DATE);
                editor.commit();

                sortFilter = DATE;
                updateGridView();
                break;
            }
            case(R.id.action_sort_by_popularity):{
                editor.putString(FILTER_KEY,DATE);
                editor.commit();

                sortFilter = POPULARITY;
                updateGridView();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTaskCompleted(String response) {
        progressBar.setVisibility(View.INVISIBLE);

        Type listType = new TypeToken<List<MovieModel>>(){}.getType();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            List<MovieModel> newMovies = new Gson().fromJson(jsonObject.get("results").toString(),listType);

            adapter.update(newMovies);

        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessage();
            return;
        }



    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMovieClicked(MovieModel movieModel) {
        Toast.makeText(this, "MovieModel: "+ movieModel.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
