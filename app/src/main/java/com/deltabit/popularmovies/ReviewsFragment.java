package com.deltabit.popularmovies;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deltabit.popularmovies.data.MovieContract;
import com.deltabit.popularmovies.databinding.FragmentMovieReviewsBinding;
import com.deltabit.popularmovies.model.MovieModel;
import com.deltabit.popularmovies.model.ReviewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.novoda.merlin.MerlinsBeard;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    private static final String LOG_TAG = ReviewsFragment.class.getSimpleName();
    FragmentMovieReviewsBinding mBinding;
    MovieModel mMovieModel;
    ReviewsAdapter mReviewAdapter;


    public ReviewsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_reviews, container, false);
        View rootView = mBinding.getRoot();
        ButterKnife.bind(this, rootView);


        mMovieModel = Parcels.unwrap(
                this.getArguments().getParcelable(DetailActivity.MOVIE_MODEL_BUNDLE));

        mReviewAdapter = new ReviewsAdapter();
        mBinding.recyclerviewReviewsFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerviewReviewsFragment.setAdapter(mReviewAdapter);

        //TODO change to execute only once
        if(MerlinsBeard.from(getContext()).isConnected()) {
            new AsyncGetReviews().execute();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    class AsyncGetReviews extends AsyncTask<String, Void, List<ReviewModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBinding.progressbar.setVisibility(View.VISIBLE);
            mBinding.includedItemNoData.itemNoDataAvailable.setVisibility(View.GONE);
        }

        @Override
        protected List<ReviewModel> doInBackground(String... strings) {

            Uri uri = new Uri.Builder()
                    .encodedPath(MovieContract.TMDB_BASE_URL)
                    .appendPath(MovieContract.MOVIE_URL)
                    .appendPath(mMovieModel.getId().toString())
                    .appendPath(MovieContract.REVIEWS_URL)
                    .appendQueryParameter(MovieContract.PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_KEY)
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
                    Type listType = new TypeToken<List<ReviewModel>>() {
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
        protected void onPostExecute(List<ReviewModel> reviewModels) {
            super.onPostExecute(reviewModels);
            mBinding.progressbar.setVisibility(View.GONE);

            if (reviewModels != null) {
                mReviewAdapter.updateData(reviewModels);
                mReviewAdapter.notifyDataSetChanged();
            }

            if(reviewModels == null || reviewModels.size()==0)
                mBinding.includedItemNoData.itemNoDataAvailable.setVisibility(View.VISIBLE);
            else
                mBinding.includedItemNoData.itemNoDataAvailable.setVisibility(View.GONE);
        }
    }


    public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

        List<ReviewModel> mReviewModels = new ArrayList<>();

        public void updateData(List<ReviewModel> newReviewModels) {
            mReviewModels.clear();
            mReviewModels.addAll(newReviewModels);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View reviewItemView = inflater.inflate(R.layout.item_recycler_reviews, parent, false);
            ViewHolder viewHolder = new ViewHolder(reviewItemView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ReviewModel reviewModel = mReviewModels.get(position);

            holder.author.setText(reviewModel.getAuthor());
            holder.review.setText(reviewModel.getContent());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mReviewModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView author;
            TextView review;

            public ViewHolder(View itemView) {
                super(itemView);
                author = (TextView) itemView.findViewById(R.id.author_reviews);
                review = (TextView) itemView.findViewById(R.id.review_reviews);
            }


        }

    }


}
