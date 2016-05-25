package com.kimeeo.kAndroid.viewPagerExt.flippableStackView;

import android.support.v4.widget.SwipeRefreshLayout;

import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;

/**
 * Created by bhavinpadhiyar on 1/22/16.
 */
abstract public class VerticalFlippableStackViewPager extends DefaultFlippableStackViewPager
{
    public StackPageTransformer.Orientation getOrientation()
    {
        return StackPageTransformer.Orientation.VERTICAL;
    }
    protected void configSwipeRefreshLayout(SwipeRefreshLayout view) {
        if (view!= null)
            view.setEnabled(false);

    }
    protected void configDataManager(DataProvider dataProvider) {
        super.configDataManager(dataProvider);
        dataProvider.setRefreshEnabled(false);
    }
}
