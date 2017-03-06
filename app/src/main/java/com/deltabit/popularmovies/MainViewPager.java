package com.deltabit.popularmovies;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rigel on 18/02/17.
 */

public class MainViewPager extends ViewPager {

    private View mCurrentView;
    boolean mAutoResize = true;

    public MainViewPager(Context context) {
        super(context);
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}

