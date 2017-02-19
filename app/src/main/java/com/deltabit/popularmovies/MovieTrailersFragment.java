package com.deltabit.popularmovies;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.databinding.FragmentMovieTrailersBinding;
import com.deltabit.popularmovies.model.MovieModel;

import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieTrailersFragment extends Fragment {

    private static final String LOG_TAG = MovieTrailersFragment.class.getSimpleName();
    FragmentMovieTrailersBinding mBinding;
    MovieModel mMovieModel;

    public MovieTrailersFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_trailers, container, false);
        View view = mBinding.getRoot();
        mMovieModel = Parcels.unwrap(
                this.getArguments().getParcelable(MovieDetailActivity.MOVIE_MODEL_BUNDLE));

        new AsyncGetTrailers().execute();

        return view;
    }

    class AsyncGetTrailers extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = new Uri.Builder()
                    .encodedPath(MovieContract.TMDB_BASE_URL)
                    .appendPath(MovieContract.MOVIE_URL)
                    .appendPath(mMovieModel.getId().toString())
                    .appendPath(MovieContract.TRAILERS_URL)
                    .appendQueryParameter(MovieContract.PARAM_API_KEY,BuildConfig.THE_MOVIE_DB_KEY)
                    .build();
            Log.d(LOG_TAG,uri.toString());

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if(hasInput)
                    return scanner.next();
                else
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            } finally {
                if(urlConnection!=null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //TODO treat json response and popoulate recyclerview
        }
    }

}
