package com.deltabit.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deltabit.popularmovies.adapters.MovieAdapter;
import com.deltabit.popularmovies.data.MovieContract.*;
import com.deltabit.popularmovies.databinding.FragmentMoviesBinding;
import com.deltabit.popularmovies.model.MovieModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 1;

    //Using butterknife here because recyclerviews aren't databinding properly.
    @BindView(R.id.recyclerview_main_activity)
    RecyclerView mRecyclerView;

    private FragmentMoviesBinding mBinding;
    private MovieAdapter mMovieAdapter;
    private String mSource;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        mSource = getArguments().getString(mContext.getString(R.string.filter_key));

        mMovieAdapter = new MovieAdapter(mContext,
                new MovieAdapter.MovieAdapterOnClickHandler() {
                    @Override
                    public void onClick(MovieModel movieModel, MovieAdapter.MovieAdapterViewHolder vh) {
                        Intent i = new Intent(mContext,DetailActivity.class);
                        i.putExtra(
                                mContext.getString(R.string.EXTRA_MOVIE_ID),
                                Parcels.wrap(movieModel)
                        );

                        //TODO Add shared element transition
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptionsCompat options = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(getActivity(), vh.imageView, getString(R.string.transition_poster));
                            mContext.startActivity(i,options.toBundle());
                        }else {
                            mContext.startActivity(i);
                        }
                    }
                }
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, rootView);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);

        mBinding.progressbarFragmentMovies.setVisibility(View.VISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Log.d(LOG_TAG,"onCreateLoader executing on "+mSource);

        Uri selectedUri;
        String filterPopularity = mContext.getString(R.string.filter_popularity);
        String filterTopRated = mContext.getString(R.string.filter_topRated);
        String filterFavorites = mContext.getString(R.string.filter_favorites);


        if(mSource.equals(filterPopularity)){
            selectedUri = PopularEntry.CONTENT_URI;
        }else if(mSource.equals(filterTopRated)){
            selectedUri = TopRatedEntry.CONTENT_URI;
        }else if(mSource.equals(filterFavorites)){
            selectedUri = FavoriteEntry.CONTENT_URI;
        }else{
            throw new UnsupportedOperationException("Unknown mSource: "+ mSource);
        }

        //Log.d(LOG_TAG,"onCreateLoader() "+ selectedUri.toString());

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
        mBinding.progressbarFragmentMovies.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }



}
