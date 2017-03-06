package com.deltabit.popularmovies;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.deltabit.popularmovies.adapters.DetailsFragmentPagerAdapter;
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
    private GradientDrawable mOval;
    private ActivityMovieDetailBinding mBinding;

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
        DetailsFragmentPagerAdapter mAdapter = new DetailsFragmentPagerAdapter(getSupportFragmentManager());

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

        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();

        return isFavorite;
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

    private void performAnimation() {
        if (mDidAnimateEnter) {
            return;
        }
        mDidAnimateEnter = true;

        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) mBinding.appBarLayout.getLayoutParams();
        final AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt();
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    behavior.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
                    mBinding.appBarLayout.requestLayout();
                }
            });
            valueAnimator.setIntValues(0, -500);
            valueAnimator.setDuration(300);
            valueAnimator.setStartDelay(500);
            valueAnimator.start();
        }
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
        int vibrantColor = palette.getVibrantColor(
                palette.getMutedColor(getResources().getColor(R.color.content_background_color))
        );
        int darkVibrantColor = palette.getDarkVibrantColor(
                palette.getDarkMutedColor(getResources().getColor(R.color.content_dark_background_color))
        );

        mBinding.collapsingToolbarLayout.setContentScrimColor(vibrantColor);
        mBinding.collapsingToolbarLayout.setStatusBarScrimColor(vibrantColor);
        mBinding.linearlayoutContentMovieDetails.setBackgroundColor(vibrantColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(darkVibrantColor);

        if(isBrightColor(vibrantColor)){
            mBinding.tabLayoutMovieDetails.setTabTextColors(
                    getResources().getColor(R.color.primary_text),
                    getResources().getColor(R.color.primary_text)
            );

            mBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(
                    getResources().getColor(R.color.primary_text)
            );
        }

    }

    public void share_onClick() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Hey, what do you think of "+mMovieModel.getTitle()+"?";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Movie suggestion");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    private boolean isBrightColor(int color) {
        if (android.R.color.transparent == color)
            return true;

        boolean rtnValue = false;

        int[] rgb = { Color.red(color), Color.green(color), Color.blue(color) };

        int brightness = (int) Math.sqrt(rgb[0] * rgb[0] * .241 + rgb[1]
                * rgb[1] * .691 + rgb[2] * rgb[2] * .068);

        // color is light
        if (brightness >= 180) {
            rtnValue = true;
        }

        return rtnValue;
    }
}
