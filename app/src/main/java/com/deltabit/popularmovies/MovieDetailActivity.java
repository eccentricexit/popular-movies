package com.deltabit.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.deltabit.popularmovies.model.MovieModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements
        AppBarLayout.OnOffsetChangedListener {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private static final float SCREEN_PERCENTAGE = 0.85f;

    @BindView(R.id.fab_favorite_movie_details) FrameLayout mFabConteiner;
    @BindView(R.id.like_button_movie_details) LikeButton mLikeButton;
    @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;

    private boolean mIsFabHidden;
    private Context mContext;
    private MovieModel mMovieModel;
    private int mMaxScrollSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mContext = this;
        mMovieModel = Parcels.unwrap(
                (Parcelable) getIntent().getExtras().get(this.getString(R.string.EXTRA_MOVIE_ID))
        );
        mAppBarLayout.addOnOffsetChangedListener(this);


        //TODO Populate with data from movieModel
        //TODO Fetch Trailers and Reviews with async task
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0) {
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
            mMaxScrollSize *= SCREEN_PERCENTAGE;
        }

        int absoluteOffset = Math.abs(verticalOffset);

        if ( absoluteOffset >= mMaxScrollSize) {
            if (!mIsFabHidden) {
                mIsFabHidden = true;
                ViewCompat.animate(mFabConteiner).scaleY(0).scaleX(0).start();
            }
        }

        if(absoluteOffset < mMaxScrollSize) {
            if (mIsFabHidden) {
                mIsFabHidden = false;
                ViewCompat.animate(mFabConteiner).scaleY(1).scaleX(1).start();
            }
        }

    }
}
