package com.deltabit.popularmovies;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deltabit.popularmovies.databinding.FragmentMovieTrailersBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieTrailersFragment extends Fragment {

    FragmentMovieTrailersBinding mBinding;

    public MovieTrailersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_trailers, container, false);
        View view = mBinding.getRoot();

        return view;
    }

}
