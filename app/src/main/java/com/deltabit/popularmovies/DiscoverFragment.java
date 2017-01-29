package com.deltabit.popularmovies;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;


public class DiscoverFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DiscoverFragment.class.getSimpleName();

    @BindView(R.id.recyclerview_discovery_movies) RecyclerView recyclerViewMovies;
    @BindView(R.id.progressbar_discovery) ProgressBar progressBar;

    MovieAdapter mMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, rootView);

        mMovieAdapter = new MovieAdapter(getContext());
        recyclerViewMovies.setAdapter(mMovieAdapter);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(),2));

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getContext(),
                MovieContract.TopRatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
