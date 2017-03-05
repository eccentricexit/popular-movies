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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.deltabit.popularmovies.adapters.MovieAdapter;
import com.deltabit.popularmovies.data.MovieContract.*;
import com.deltabit.popularmovies.databinding.FragmentMoviesBinding;
import com.deltabit.popularmovies.model.MovieModel;
import com.novoda.merlin.MerlinsBeard;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 1;

    //Using butterknife here because recyclerviews aren't databinding properly.
    @BindView(R.id.recyclerview_main_activity)
    RecyclerView mRecyclerView;

    //Using butterknife here because changes in visibility weren't affecting the view
    //when using databinding.
    @BindView(R.id.item_no_data_available)
    LinearLayout  mItemNoData;

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
        FragmentMoviesBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        View rootView = mBinding.getRoot();
        ButterKnife.bind(this, rootView);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

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
        mMovieAdapter.swapCursor(data);

        if(data.getCount()==0) {
            mItemNoData.setVisibility(View.VISIBLE);
            if(!MerlinsBeard.from(mContext).isConnected()){
                Toast.makeText(mContext, getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
            }

        }else {
            mItemNoData.setVisibility(View.INVISIBLE);
        }

        Log.d(LOG_TAG,"recyclerview height: "+mRecyclerView.getHeight());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


}
