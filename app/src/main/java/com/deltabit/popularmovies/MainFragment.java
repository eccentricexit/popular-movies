package com.deltabit.popularmovies;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.deltabit.popularmovies.data.MovieContract;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 1;

    @BindView(R.id.recyclerview_discovery_movies) RecyclerView mRecyclerViewMovies;
    @BindView(R.id.progressbar_discovery) ProgressBar mProgressBar;

    MovieAdapter mMovieAdapter;
    SharedPreferences mSharedPreferences;
    String mSortOrder;

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
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, rootView);

        mMovieAdapter = new MovieAdapter(getContext());
        mRecyclerViewMovies.setAdapter(mMovieAdapter);
        mRecyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(),2));

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG,"onCreateLoader executing...");

        Uri selectedUri;
        String filterPopularity = getContext().getString(R.string.filter_popularity);
        String filterTopRated = getContext().getString(R.string.filter_topRated);

        if(mSortOrder.equals(filterPopularity)){
            selectedUri = MovieContract.PopularEntry.CONTENT_URI;
        }else if(mSortOrder.equals(filterTopRated)){
            selectedUri = MovieContract.TopRatedEntry.CONTENT_URI;
        }else{
            throw new UnsupportedOperationException("Unknown mSortOrder: "+ mSortOrder);
        }

        return new CursorLoader(
                getContext(),
                selectedUri,
                null,
                null,
                null,
                null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG,"onLoadFinished executing...");
        Log.d(LOG_TAG,"cursor has size:" + data.getCount());

        mMovieAdapter.swapCursor(data);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG,"onSharedPreferenceChanged executing...");
        mSortOrder = sharedPreferences.getString(
                getContext().getString(R.string.filter_key),
                getContext().getString(R.string.filter_default)
        );

        getLoaderManager().restartLoader(MOVIES_LOADER,null,this);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG,"onResume executing...");
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG,"onPause executing...");
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
