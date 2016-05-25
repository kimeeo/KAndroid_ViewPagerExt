package com.kimeeo.kAndroid.viewPagerExt.verticalViewPager;

import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kimeeo.kAndroid.listViews.pager.viewPager.BaseViewPagerAdapter;
import com.kimeeo.kAndroid.listViews.pager.viewPager.DefaltViewPager;
import com.kimeeo.kAndroid.viewPagerExt.R;

import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;


/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class VerticalViewPager extends DefaltViewPager {

    @Override
    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_vertical_view_pager_with_swipe_refresh_layout;
    }
    @Override
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_vertical_view_pager;
    }
    protected void configViewPager(ViewPager mList, BaseViewPagerAdapter mAdapter, View indicator) {
        ViewPager.PageTransformer pageTransformer=getPageTransformer();
        if(pageTransformer!=null)
            mList.setPageTransformer(false, pageTransformer);
    }
    protected ViewPager.PageTransformer getPageTransformer()
    {
        return new DefaultTransformer();
    }
}