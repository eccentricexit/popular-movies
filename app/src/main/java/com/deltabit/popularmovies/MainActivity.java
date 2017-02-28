package com.deltabit.popularmovies;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.deltabit.popularmovies.adapters.CustomFragmentPagerAdapter;
import com.deltabit.popularmovies.databinding.ActivityMainBinding;
import com.deltabit.popularmovies.sync.SyncAdapter;

import org.parceler.Parcels;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        SyncAdapter.initializeSyncAdapter(this);

        setupViewPager();
        mBinding.tabLayoutMovies.setupWithViewPager(mBinding.viewpagerMovies);
    }

    private void setupViewPager() {
        CustomFragmentPagerAdapter fragmentAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());

        Fragment topRatedFragment = new MoviesFragment();
        Fragment popularFragment = new MoviesFragment();
        Fragment favoritesFragment = new MoviesFragment();

        Bundle topRatedBundle = new Bundle();
        topRatedBundle.putString(getString(R.string.filter_key),getString(R.string.filter_topRated));

        Bundle favoritesBundle = new Bundle();
        favoritesBundle.putString(getString(R.string.filter_key),getString(R.string.filter_favorites));

        Bundle popularBundle = new Bundle();
        popularBundle.putString(getString(R.string.filter_key),getString(R.string.filter_popularity));


        topRatedFragment.setArguments(topRatedBundle);
        popularFragment.setArguments(popularBundle);
        favoritesFragment.setArguments(favoritesBundle);

        fragmentAdapter.addFragment(topRatedFragment, getString(R.string.tab_title_topRated));
        fragmentAdapter.addFragment(popularFragment, getString(R.string.tab_title_popular));
        fragmentAdapter.addFragment(favoritesFragment, getString(R.string.tab_title_favorites));

        mBinding.viewpagerMovies.setAdapter(fragmentAdapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
