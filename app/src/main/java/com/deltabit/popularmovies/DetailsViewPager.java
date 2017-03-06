package com.deltabit.popularmovies;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rigel on 18/02/17.
 */

public class DetailsViewPager extends ViewPager {

    private View mCurrentView;
    boolean mAutoResize = true;

    public DetailsViewPager(Context context) {
        super(context);
    }

    public DetailsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        if(mAutoResize ==true) {
            if (mCurrentView == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            int height = 0;
            mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = mCurrentView.getMeasuredHeight() > heightMeasureSpec ? mCurrentView.getMeasuredHeight() : heightMeasureSpec;
            if (h > height) height = h;

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }
}

