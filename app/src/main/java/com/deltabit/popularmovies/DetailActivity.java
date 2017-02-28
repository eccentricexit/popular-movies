package com.deltabit.popularmovies;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.deltabit.popularmovies.adapters.CustomFragmentPagerAdapter;
import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.databinding.ActivityMovieDetailBinding;
import com.deltabit.popularmovies.model.MovieModel;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DetailActivity extends AppCompatActivity implements
        AppBarLayout.OnOffsetChangedListener {

    public static final String MOVIE_MODEL_BUNDLE = "movie_model";

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();


    private static final float SCREEN_PERCENTAGE = 0.85f;
    GradientDrawable mOval;
    private ActivityMovieDetailBinding mBinding;
    private CustomFragmentPagerAdapter mAdapter;
    private boolean mIsFabHidden;
    private int mMaxScrollSize;
    private boolean mDidAnimateEnter = false;
    private MovieModel mMovieModel;

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


        mMovieModel = Parcels.unwrap(
                (Parcelable) getIntent().getExtras()
                        .get(this.getString(R.string.EXTRA_MOVIE_ID))
        );
        mBinding.appBarLayout.addOnOffsetChangedListener(this);

        setupFab();
        setupBasicInfo(mMovieModel);
        setupViewPager(mMovieModel);
        mBinding.tabLayoutMovieDetails
                .setupWithViewPager(mBinding.viewPagerMovieDetails);
    }

    private void setupViewPager(MovieModel mMovieModel) {
        mAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_MODEL_BUNDLE, Parcels.wrap(mMovieModel));

        Fragment reviewsFragment = new ReviewsFragment();
        Fragment trailerFragment = new TrailersFragment();
        Fragment detailsFragment = new DetailsFragment();


        reviewsFragment.setArguments(bundle);
        trailerFragment.setArguments(bundle);
        detailsFragment.setArguments(bundle);

        mAdapter.addFragment(detailsFragment, getString(R.string.tab_title_details));
        mAdapter.addFragment(reviewsFragment, getString(R.string.tab_title_reviews));
        mAdapter.addFragment(trailerFragment, getString(R.string.tab_title_trailers));

        mBinding.viewPagerMovieDetails.setAdapter(mAdapter);
    }

    private void setupFab() {
        mBinding.likeButtonMovieDetails.setIconSizeDp(40);
        mBinding.likeButtonMovieDetails.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mOval.setAlpha(0);
                likeButton.setIconSizeDp(25);
                saveAsFavorite();
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                mOval.setAlpha(255);
                likeButton.setIconSizeDp(40);
                removeFromFavorite();
            }
        });

        if(isFavorite()) {
            mOval.setAlpha(0);
            mBinding.likeButtonMovieDetails.setIconSizeDp(40);
            mBinding.likeButtonMovieDetails.setLiked(true);
        }
        else {
            mOval.setAlpha(255);
            mBinding.likeButtonMovieDetails.setIconSizeDp(25);
            mBinding.likeButtonMovieDetails.setLiked(false);
        }
    }

    private boolean isFavorite() {
        Cursor cursor = getContentResolver().query(
                MovieContract.FavoriteEntry.buildFavoriteWithIdUri(mMovieModel.getId()),
                null,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID+" = ?",
                new String[]{mMovieModel.getId().toString()},
                null
        );

        return cursor.getCount() > 0;
    }

    private void saveAsFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,mMovieModel.getId());

        getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI,cv);
    }

    private void removeFromFavorite() {
        getContentResolver().delete(
                MovieContract.FavoriteEntry.CONTENT_URI,
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{mMovieModel.getId().toString()}
        );
    }

    protected void performAnimation() {
        if (mDidAnimateEnter) {
            return;
        }
        mDidAnimateEnter = true;

        Log.d(LOG_TAG,"performAnimation()");
        //TODO Animate scrollup to reveal content
        final int startScrollPos = getResources()
                .getDimensionPixelSize(R.dimen.scroll_animation_start);

        Animator anim = ObjectAnimator.ofInt(
                mBinding.coordinatorLDetailsActivity,
                "scrollY",
                startScrollPos)
                .setDuration(375);
        anim.setStartDelay(800);
        //anim.start();

    }

    private void setupBasicInfo(MovieModel movieModel) {
        Picasso.with(this)
                .load(MovieContract.getMediumPosterUrlFor(movieModel.getPosterPath(), this))
                .into(mBinding.imageviewPosterMoviedetails);


        BitmapDrawable drawable = (BitmapDrawable)mBinding.imageviewPosterMoviedetails.getDrawable();
        if(drawable!=null) {
            Palette p = Palette.from(
                    (drawable).getBitmap())
                    .generate();
            applyPalette(p);
        }

        mBinding.toolbarMovieDetails.setTitle(movieModel.getTitle().toUpperCase());
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
        performAnimation();
    }

    private void applyPalette(Palette palette) {
        mBinding.collapsingToolbarLayout.setContentScrimColor(
                palette.getVibrantColor(getResources().getColor(R.color.primary))
        );

        mBinding.collapsingToolbarLayout.setStatusBarScrimColor(
                palette.getVibrantColor(getResources().getColor(R.color.primary))
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(
                    palette.getDarkVibrantColor(getResources().getColor(R.color.primary_dark))
            );

        mBinding.tabLayoutMovieDetails.setBackgroundColor(
                palette.getVibrantColor(getResources().getColor(R.color.primary))
        );

        mBinding.linearlayoutContentMovieDetails.setBackgroundColor(
                palette.getVibrantColor(getResources().getColor(R.color.primary))
        );
    }




}
