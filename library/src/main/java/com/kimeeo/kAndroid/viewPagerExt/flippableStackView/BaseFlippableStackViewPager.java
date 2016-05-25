package com.kimeeo.kAndroid.viewPagerExt.flippableStackView;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.pager.BaseViewPager;
import com.kimeeo.kAndroid.listViews.pager.viewPager.BaseViewPagerAdapter;
import com.kimeeo.kAndroid.viewPagerExt.R;


import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/22/16.
 */
abstract public class BaseFlippableStackViewPager extends BaseViewPager
{
    private FlippableStackView mViewPager;
    private View mProgressBar;
    private boolean firstItemIn = false;
    private boolean firstDataIn = true;

    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        mViewPager=null;
        mProgressBar=null;

    }
    protected View createRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getDataProvider().getRefreshEnabled())
            return inflater.inflate(getRootRefreshLayoutResID() , container, false);
        else
            return inflater.inflate(getRootLayoutResID(), container, false);
    }

    @Override
    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_fliper_page_view_with_swipe_refresh_layout;
    }
    @Override
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_fliper_page_view;
    }
    @IdRes
    protected int getProgressBarResID() {
        return  R.id.progressBar;
    }




    protected FlippableStackView createFlippableStackView(View rootView)
    {
        FlippableStackView viewPager = (FlippableStackView)rootView.findViewById(getViewPagerResID());
        return viewPager;
    }
    abstract public StackPageTransformer.Orientation getOrientation();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configViewParam();

        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mViewPager = createFlippableStackView(mRootView);

        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.initStack(4, getOrientation());

        mEmptyViewHelper = createEmptyViewHelper();
        createAdapter(mViewPager);
        configViewPager(mViewPager, mAdapter, mIndicator);
        if(mRootView.findViewById(getProgressBarResID())!=null)
            mProgressBar= mRootView.findViewById(getProgressBarResID());
        onViewCreated(mRootView);
        next();
        return mRootView;
    }

    protected void configViewPager(FlippableStackView mList, BaseViewPagerAdapter mAdapter, View indicator) {

    }

    protected void createAdapter(FlippableStackView mViewPager) {
        mAdapter = createViewPagerAdapter();
        //mAdapter.supportLoader=false;

    }

    public void onPageSelected(int position)
    {
        if(mViewPager!=null)
        {
            Object iBaseObject = this.getDataProvider().get(position);
            onPageChange(iBaseObject, position);
            setCurrentItem(position);

            onPageChange(iBaseObject, position);

            if (getDataProvider().getCanLoadNext() && position == 0)
                next();


            if (getDataProvider().getCanLoadRefresh() && position == (getDataProvider().size() - 1)) {
                if (getSwipeRefreshLayout() != null)
                    getSwipeRefreshLayout().setEnabled(true);
            } else if (getSwipeRefreshLayout() != null)
                getSwipeRefreshLayout().setEnabled(false);
        }
    }
    protected void configDataManager(DataProvider dataProvider) {
        dataProvider.setEnterReverse(true);
    }


    public void onFetchingStart(boolean isFetchingRefresh) {
        super.onFetchingStart(isFetchingRefresh);
        if(mProgressBar!=null && isFetchingRefresh==false)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onFetchingError(Object error) {
        if(this.mEmptyViewHelper != null) {
            this.mEmptyViewHelper.updateView(this.getDataProvider());
        }

        this.updateSwipeRefreshLayout(false);
        updateProgress();
    }

    public void onFetchingEnd(final List<?> dataList,final boolean isFetchingRefresh) {
        super.onFetchingEnd(dataList,isFetchingRefresh);
        updateProgress();

        final Handler handler = new Handler();
        final Runnable runnablelocal = new Runnable() {
            @Override
            public void run() {
                try {
                    if(firstDataIn) {
                        mViewPager.setAdapter(mAdapter);
                        mViewPager.invalidate();
                        firstDataIn=false;
                    }
                    else {
                        if(isFetchingRefresh) {
                            int oldIndex = getDataProvider().size()-1;
                            mViewPager.setAdapter(mAdapter);
                            mViewPager.invalidate();
                            gotoItem(oldIndex, true);
                        }
                        else
                        {
                            int oldIndex = getCurrentItem() + dataList.size();
                            mViewPager.setAdapter(mAdapter);
                            mViewPager.invalidate();
                            gotoItem(oldIndex, false);
                        }
                        firstDataIn = false;
                    }
                }catch (Exception e)
                {

                }

            }
        };
        handler.postDelayed(runnablelocal, 500);
    }
    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
        updateProgress();
    }
    @Override
    public void gotoItem(int value, Boolean scroll) {
        if(this.mViewPager != null) {
            this.mViewPager.setCurrentItem(value, scroll.booleanValue());
        }

        this.setCurrentItem(value);
    }
    protected void updateProgress() {
        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn = true;
    }
}
