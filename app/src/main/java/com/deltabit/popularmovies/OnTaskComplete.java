package com.deltabit.popularmovies;

/**
 * Created by rigel on 12/01/17.
 */

public interface OnTaskComplete {
    void onTaskCompleted(String response);
    void showErrorMessage();
}
