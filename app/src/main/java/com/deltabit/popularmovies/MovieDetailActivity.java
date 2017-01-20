package com.deltabit.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private static final String APPBAR_TRANSITION = "appbar_transition";

    @BindView(R.id.activity_movie_detail) CoordinatorLayout mRoot;
    @BindView(R.id.imageview_background_details) ImageView mBackground;
    @BindView(R.id.toolbar_movie_details) Toolbar mToolbar;
    @BindView(R.id.scrollview_details) NestedScrollView mScrollView;
    @BindView(R.id.imageview_poster_details) ImageView mPoster;
    @BindView(R.id.textview_plot_details) TextView mPlot;
    @BindView(R.id.textview_releaseDate_details) TextView mReleaseDate;
    @BindView(R.id.ratingBar_movie_details) RatingBar mRatingBar;
    @BindView(R.id.collapsingtblayout_details) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.separator_details) LinearLayout mSeparator;
    @BindView(R.id.background_color_overlay) View backgroundOverlay;

    Bundle extras;
    MovieModel movieModel;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        ButterKnife.bind(this);

        context = this;
        extras = getIntent().getExtras();
        movieModel = (MovieModel) extras.getSerializable(DiscoveryActivity.MOVIEMODEL_EXTRA);

        ViewCompat.setTransitionName(findViewById(R.id.appbar), APPBAR_TRANSITION);

        Picasso.with(this)
                .load(Utilities.getMediumPosterUrlFor(movieModel,this))
                .into(mPoster,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        BitmapDrawable drawable = (BitmapDrawable) mPoster.getDrawable();
                        Bitmap posterBitmap = drawable.getBitmap();
                        if (posterBitmap != null && !posterBitmap.isRecycled()) {
                            Palette palette = Palette.from(posterBitmap).generate();
                            applyBlur(palette,posterBitmap);
                            applyPalette(palette);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

        mToolbar.setTitle(movieModel.getTitle());
        mPlot.setText(movieModel.getOverview());
        mReleaseDate.setText("Release date: "+movieModel.getFormattedReleaseDate());
        mRatingBar.setRating(movieModel.getVoteAverage().floatValue()/2f);
    }

    private void applyBlur(Palette palette, Bitmap bitmap) {
        Blurry.with(context)
                .from(bitmap)
                .into(mBackground);
    }

    private void applyPalette(Palette palette) {
        mCollapsingToolbarLayout.setContentScrimColor(
                        palette.getMutedColor(getResources().getColor(R.color.primary))
                );

        mCollapsingToolbarLayout.setStatusBarScrimColor(
                        palette.getDarkMutedColor(getResources().getColor(R.color.primary))
                );

        mRoot.setBackgroundColor(
                palette.getDarkMutedColor(getResources().getColor(R.color.primary))
        );

        mSeparator.setBackgroundColor(
                palette.getLightMutedColor(getResources().getColor(R.color.cardview_light_background))
        );


        backgroundOverlay.setBackgroundColor(
                Utilities.lighten(palette.getLightMutedColor(Color.WHITE),0.6f)
        );
    }


}
