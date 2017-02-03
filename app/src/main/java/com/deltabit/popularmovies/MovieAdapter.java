package com.deltabit.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.data.MovieDbHelper;
import com.deltabit.popularmovies.model.MovieModel;
import com.squareup.picasso.Picasso;

/**
 * Created by rigel on 12/01/17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private final Context mContext;
    private final MovieAdapterOnClickHandler mOnClickHandler;
    private Cursor mCursor;


    public MovieAdapter(Context context,MovieAdapterOnClickHandler onClickHandler) {
        this.mContext = context;
        mOnClickHandler = onClickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( parent instanceof RecyclerView ) {
            int layoutId = R.layout.item_recycler_main_activity;

            View movieItemView = LayoutInflater.from(
                    parent.getContext()
            ).inflate(layoutId, parent, false);

            movieItemView.setFocusable(true);
            return new MovieAdapterViewHolder(movieItemView);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.movieModel = movieModelFrom(mCursor);
        holder.movieTitle.setText(mCursor.getString(MovieDbHelper.COL_TITLE));
        Picasso.with(mContext)
                .load(MovieContract.getMediumPosterUrlFor(
                        mCursor.getString(MovieDbHelper.COL_POSTER),
                        mContext)
                )
                .into(holder.imageView);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(mCursor==null)
            return 0;

        return mCursor.getCount();
    }

    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    private MovieModel movieModelFrom(Cursor mCursor) {
        MovieModel movieModel = new MovieModel();

        movieModel.setOriginalLanguage(mCursor.getString(MovieDbHelper.COL_ORIGINAL_LANGUAGE));
        movieModel.setVoteAverage(mCursor.getDouble(MovieDbHelper.COL_VOTE_AVERAGE));
        movieModel.setAdult(mCursor.getInt(MovieDbHelper.COL_ADULT) == 1);
        movieModel.setId(mCursor.getInt(MovieDbHelper.COL_MOVIE_ID));
        movieModel.setTitle(mCursor.getString(MovieDbHelper.COL_TITLE));
        movieModel.setReleaseDate(mCursor.getString(MovieDbHelper.COL_RELEASE_DATE));
        movieModel.setPosterPath(mCursor.getString(MovieDbHelper.COL_POSTER));
        movieModel.setPopularity(mCursor.getDouble(MovieDbHelper.COL_POPULARITY));
        movieModel.setVideo(mCursor.getInt(MovieDbHelper.COL_VIDEO) == 1);
        movieModel.setVoteCount(mCursor.getInt(MovieDbHelper.COL_VOTE_COUNT));
        movieModel.setOriginalTitle(mCursor.getString(MovieDbHelper.COL_ORIGINAL_TITLE));
        movieModel.setOverview(mCursor.getString(MovieDbHelper.COL_OVERVIEW));
        movieModel.setBackdropPath(mCursor.getString(MovieDbHelper.COL_BACKDROP_PATH));

        return movieModel;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieModel movieModel, MovieAdapterViewHolder vh);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        final ImageView imageView;
        final TextView movieTitle;
        MovieModel movieModel;
        View itemView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_item_main_activity);
            movieTitle = (TextView) itemView.findViewById(R.id.textview_item_main_activity);
            itemView.setOnClickListener(this);
            this.itemView = itemView;
        }

        @Override
        public void onClick(View view) {
            mOnClickHandler.onClick(movieModel, this);
        }


    }



}
