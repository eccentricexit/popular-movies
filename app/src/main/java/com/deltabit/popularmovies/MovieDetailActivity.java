package com.deltabit.popularmovies;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.scrollview_details) ScrollView mScrollView;
    @BindView(R.id.imageview_poster_details) ImageView mPoster;
    @BindView(R.id.textview_title_details) TextView mTitle;
    @BindView(R.id.textview_plot_details) TextView mPlot;
    @BindView(R.id.textview_releaseDate_details) TextView mReleaseDate;
    @BindView(R.id.ratingBar_movie_details) RatingBar mRatingBar;

    Bundle extras;
    MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        extras = getIntent().getExtras();
        movieModel = (MovieModel) extras.getSerializable(DiscoveryActivity.MOVIEMODEL_EXTRA);

        Picasso.with(this)
                .load(Utilities.getMediumPosterUrlFor(movieModel,this))
                .into(mPoster,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        BitmapDrawable drawable = (BitmapDrawable) mPoster.getDrawable();
                        Bitmap posterBitmap = drawable.getBitmap();
                        if (posterBitmap != null && !posterBitmap.isRecycled()) {

                            Palette palette = Palette.from(posterBitmap).generate();

                            mTitle.setBackgroundColor(palette.getLightVibrantColor(
                                    ContextCompat.getColor(getBaseContext(), R.color.primary_light))
                            );

                            mTitle.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.primary_text));

                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

        mTitle.setText(movieModel.getTitle());
        mPlot.setText(movieModel.getOverview());
        mReleaseDate.setText("Release date: "+movieModel.getFormattedReleaseDate());
        mRatingBar.setRating(movieModel.getVoteAverage().floatValue()/2f);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        final int startScrollPos = getResources().getDimensionPixelSize(R.dimen.init_scroll_up_distance);

        Animator animator = ObjectAnimator.ofInt(mScrollView,"scrollY",startScrollPos)
                .setDuration(300);

        animator.setStartDelay(200);
        animator.start();
    }

    @Override
    public void onBackPressed() {
        Animator animator = ObjectAnimator.ofInt(mScrollView,"scrollY",0)
                .setDuration(300);

        animator.start();

        super.onBackPressed();
    }
}
