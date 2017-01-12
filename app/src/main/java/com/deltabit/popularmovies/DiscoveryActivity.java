package com.deltabit.popularmovies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoveryActivity extends AppCompatActivity implements OnTaskComplete {

    private static final String LOG_TAG = DiscoveryActivity.class.getSimpleName();

    private static final String POPULARITY = "popularity.desc";
    private static final String DATE = "release_date.asc";
    private static String sortFilter = POPULARITY;

    @BindView(R.id.gridview_discovery_movies) GridView gridViewMovies;
    @BindView(R.id.progressbar_discovery) ProgressBar progressBar;

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
            showErrorMessage();
            e.printStackTrace();
        }

        progressBar.setVisibility(View.VISIBLE);
        new MovieQueryTask(this).execute(url);
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
            default:{
                showErrorMessage();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTaskCompleted(String response) {
        progressBar.setVisibility(View.INVISIBLE);

        Type listType = new TypeToken<List<Movie>>(){}.getType();
        JSONObject jsonObject;
        List<Movie> movies;
        try {
            jsonObject = new JSONObject(response);
            movies = new Gson().fromJson(jsonObject.get("results").toString(),listType);
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessage();
            return;
        }

        Toast.makeText(this, movies.get(0).getTitle(), Toast.LENGTH_SHORT).show();

        gridViewMovies.setAdapter(new MovieAdapter(this,movies));
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }


}
