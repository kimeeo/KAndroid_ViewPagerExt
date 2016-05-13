package com.kimeeo.kAndroid.viewPagerExt.flippableStackView;

import android.view.View;

import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.pager.viewPager.BaseViewPagerAdapter;
import com.kimeeo.kAndroid.listViews.pager.viewPager.DefaultViewPagerAdapter;
import com.kimeeo.kAndroid.listViews.pager.viewPager.IViewProvider;

/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class DefaultFlippableStackViewPager extends BaseFlippableStackViewPager implements IViewProvider {


    abstract public View getView(int position, Object data);
    public void removeView(View view, int position, BaseItemHolder itemHolder) {

    }
    abstract public BaseItemHolder getItemHolder(View view, int position, Object data);


    protected BaseViewPagerAdapter createViewPagerAdapter()
    {
        return new DefaultViewPagerAdapter(getDataProvider(),this);
    }
}
