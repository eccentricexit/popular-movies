package com.deltabit.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.databinding.FragmentMovieTrailersBinding;
import com.deltabit.popularmovies.model.MovieModel;
import com.deltabit.popularmovies.model.TrailerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrailersFragment extends Fragment {

    private static final String LOG_TAG = TrailersFragment.class.getSimpleName();
    FragmentMovieTrailersBinding mBinding;
    MovieModel mMovieModel;
    TrailersAdapter mTrailersAdapter;

    public TrailersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_trailers, container, false);
        View view = mBinding.getRoot();
        mMovieModel = Parcels.unwrap(
                this.getArguments().getParcelable(MovieDetailActivity.MOVIE_MODEL_BUNDLE));

        mTrailersAdapter = new TrailersAdapter();
        mBinding.recyclerviewTrailersFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerviewTrailersFragment.setAdapter(mTrailersAdapter);

        Log.d(LOG_TAG, "onCreateView...");
        new AsyncGetTrailers().execute();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    //TODO Create generic asynctask
    class AsyncGetTrailers extends AsyncTask<String, Void, List<TrailerModel>> {

        @Override
        protected List<TrailerModel> doInBackground(String... strings) {

            Uri uri = new Uri.Builder()
                    .encodedPath(MovieContract.TMDB_BASE_URL)
                    .appendPath(MovieContract.MOVIE_URL)
                    .appendPath(mMovieModel.getId().toString())
                    .appendPath(MovieContract.TRAILERS_URL)
                    .appendQueryParameter(MovieContract.PARAM_API_KEY,BuildConfig.THE_MOVIE_DB_KEY)
                    .build();


            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                String response = null;
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    response = scanner.next();
                    Type listType = new TypeToken<List<TrailerModel>>() {
                    }.getType();
                    JSONObject jsonObject;

                    jsonObject = new JSONObject(response);
                    return new Gson().fromJson(jsonObject.get("results").toString(), listType);
                }
                else
                    throw new Exception("Error: No input returned.");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if(urlConnection!=null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(List<TrailerModel> trailerModels) {
            super.onPostExecute(trailerModels);
            if (trailerModels != null) {
                mTrailersAdapter.updateData(trailerModels);
                mTrailersAdapter.notifyDataSetChanged();
            }
        }
    }


    public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

        List<TrailerModel> mTrailerModels = new ArrayList<>();

        public void updateData(List<TrailerModel> newTrailerModels) {
            mTrailerModels.clear();
            mTrailerModels.addAll(newTrailerModels);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View trailerItemView = inflater.inflate(R.layout.item_recycler_trailers, parent, false);
            TrailersAdapter.ViewHolder viewHolder = new TrailersAdapter.ViewHolder(trailerItemView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TrailersAdapter.ViewHolder holder, int position) {
            final TrailerModel trailerModel = mTrailerModels.get(position);
            if (holder != null && holder.trailerThumbnail != null) {
                Picasso.with(getContext())
                        .load("http://img.youtube.com/vi/" + trailerModel.getKey() + "/0.jpg")
                        .into(holder.trailerThumbnail);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + trailerModel.getKey()))
                        );
                    }
                });
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mTrailerModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView trailerThumbnail;

            public ViewHolder(View itemView) {
                super(itemView);
                trailerThumbnail = (ImageView) itemView.findViewById(R.id.imageview_trailer_thumbnail);
            }

        }

    }


}