package com.deltabit.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.model.MovieModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 1;

    @BindView(R.id.recyclerview_main_activity)
    private RecyclerView mRecyclerViewMovies;
    @BindView(R.id.progressbar_discovery)
    private ProgressBar mProgressBar;

    private MovieAdapter mMovieAdapter;
    private SharedPreferences mSharedPreferences;
    private String mSortOrder;
    private Context mContext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSortOrder = mSharedPreferences.getString(
                getContext().getString(R.string.filter_key),
                getContext().getString(R.string.filter_default)
        );
        mProgressBar.setVisibility(View.VISIBLE);


        getLoaderManager().initLoader(MOVIES_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerViewMovies.setLayoutManager(new GridLayoutManager(mContext,2));

        mMovieAdapter = new MovieAdapter(mContext,
                new MovieAdapter.MovieAdapterOnClickHandler() {
                    @Override
                    public void onClick(MovieModel movieModel, MovieAdapter.MovieAdapterViewHolder vh) {
                        Intent i = new Intent(mContext,MovieDetailActivity.class);
                        i.putExtra(
                                mContext.getString(R.string.EXTRA_MOVIE_ID),
                                Parcels.wrap(movieModel)
                        );

                        mContext.startActivity(i);
                    }
                }
        );
        mRecyclerViewMovies.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.d(LOG_TAG,"onCreateLoader executing...");

        Uri selectedUri;
        String filterPopularity = mContext.getString(R.string.filter_popularity);
        String filterTopRated = mContext.getString(R.string.filter_topRated);

        if(mSortOrder.equals(filterPopularity)){
            selectedUri = MovieContract.PopularEntry.CONTENT_URI;
        }else if(mSortOrder.equals(filterTopRated)){
            selectedUri = MovieContract.TopRatedEntry.CONTENT_URI;
        }else{
            throw new UnsupportedOperationException("Unknown mSortOrder: "+ mSortOrder);
        }

        return new CursorLoader(
                mContext,
                selectedUri,
                null,
                null,
                null,
                null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Log.d(LOG_TAG,"onLoadFinished executing...");
//        Log.d(LOG_TAG,"cursor has size:" + data.getCount());

        mMovieAdapter.swapCursor(data);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        Log.d(LOG_TAG,"onSharedPreferenceChanged executing...");
        mSortOrder = sharedPreferences.getString(
                mContext.getString(R.string.filter_key),
                mContext.getString(R.string.filter_default)
        );

        getLoaderManager().restartLoader(MOVIES_LOADER,null,this);
    }

    @Override
    public void onResume() {
//        Log.d(LOG_TAG,"onResume executing...");
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
//        Log.d(LOG_TAG,"onPause executing...");
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
