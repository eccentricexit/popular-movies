package com.deltabit.popularmovies;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.databinding.ActivityMovieDetailBinding;
import com.deltabit.popularmovies.model.MovieModel;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;


public class MovieDetailActivity extends AppCompatActivity implements
        AppBarLayout.OnOffsetChangedListener {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private static final float SCREEN_PERCENTAGE = 0.85f;

    @BindView(R.id.scrollView_movie_detail)
    NestedScrollView mScrollView;

    private ActivityMovieDetailBinding mBinding;
    private boolean mIsFabHidden;
    private int mMaxScrollSize;
    private boolean mDidAnimateEnter = false;
    GradientDrawable mOval;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        mOval = ((GradientDrawable)mBinding.fabContainterMovieDetails.getBackground());


        MovieModel mMovieModel = Parcels.unwrap(
                (Parcelable) getIntent().getExtras()
                        .get(this.getString(R.string.EXTRA_MOVIE_ID))
        );
        mBinding.appBarLayout.addOnOffsetChangedListener(this);

        setupFab();
        displayMovieInfo(mMovieModel);
        fetchReviews();
        fetchTrailers();
    }

    private void setupFab() {
        //TODO Add logic to save favorite movies
        mBinding.likeButtonMovieDetails.setIconSizeDp(40);
        mBinding.likeButtonMovieDetails.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mOval.setAlpha(0);
                likeButton.setIconSizeDp(25);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                mOval.setAlpha(255);
                likeButton.setIconSizeDp(40);
            }
        });
    }

    private void fetchTrailers() {
        //TODO Fetch Trailers with async task
    }

    private void fetchReviews() {
        //TODO Fetch Reviews with async task

    }

    protected void performAnimation() {
        if (mDidAnimateEnter) {
            return;
        }
        mDidAnimateEnter = true;
        //TODO Animate scrollup to reveal content
    }

    private void displayMovieInfo(MovieModel movieModel) {
        //TODO Apply pallete to appbar/scrim after picasso loads
        Picasso.with(this)
                .load(MovieContract.getMediumPosterUrlFor(movieModel.getPosterPath(), this))
                .into(mBinding.imageviewPosterMoviedetails);

        mBinding.toolbarMovieDetails.setTitle(movieModel.getTitle().toUpperCase());
        mBinding.cLayoutMovieDetail.textviewPlotMoviedetails.setText(movieModel.getOverview());
        mBinding.cLayoutMovieDetail.releaseDate.setText(movieModel.getFormattedReleaseDate());
        mBinding.cLayoutMovieDetail.materialRatingBarMovieDetail.setRating(movieModel.getVoteAverage().floatValue() / 2f);

        mBinding.toolbarMovieDetails.setTitleTextColor(Color.WHITE);
        mBinding.toolbarMovieDetails.setSubtitleTextColor(Color.WHITE);

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
                ViewCompat.animate(mBinding.fabContainterMovieDetails).scaleY(0).scaleX(0).start();
            }
        }

        if(absoluteOffset < mMaxScrollSize) {
            if (mIsFabHidden) {
                mIsFabHidden = false;
                ViewCompat.animate(mBinding.fabContainterMovieDetails).scaleY(1).scaleX(1).start();
            }
        }

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        Log.d(LOG_TAG, "onEnterAnimationComplete()");
        performAnimation();
    }
}
