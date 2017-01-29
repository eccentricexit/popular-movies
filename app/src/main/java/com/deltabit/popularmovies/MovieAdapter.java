package com.deltabit.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.data.MovieDbHelper;
import com.deltabit.popularmovies.data.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.deltabit.popularmovies.data.MovieDbHelper.*;

/**
 * Created by rigel on 12/01/17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    Context mContext;
    Cursor mCursor;

    public MovieAdapter(Context context) {
        this.mContext = context;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String MOVIE_ID_EXTRA = "movie_id";
        ImageView imageView;
        TextView movieTitle;
        public String movieId;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_item_discovery);
            movieTitle = (TextView) itemView.findViewById(R.id.textview_item_discovery);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext,MovieDetailActivity.class);
            i.putExtra(MOVIE_ID_EXTRA,movieId);

            mContext.startActivity(i);
        }


    }
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( parent instanceof RecyclerView ) {
            int layoutId = R.layout.item_discovery_movie;

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

        holder.movieId = mCursor.getString(MovieDbHelper.COL_MOVIE_ID);
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



}
