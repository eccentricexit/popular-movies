package com.deltabit.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deltabit.popularmovies.model.MovieModel;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private Context mContext;
    private MovieModel mMovieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        mContext = this;

        mMovieModel = Parcels.unwrap(
                (Parcelable) getIntent().getExtras().get(this.getString(R.string.EXTRA_MOVIE_ID))
        );

        Log.d(LOG_TAG,"Movie Id: "+mMovieModel.getId());

        //TODO Populate with data from movieModel
        //TODO Fetch Trailers and Reviews with async task
    }

}
