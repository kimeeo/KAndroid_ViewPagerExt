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


        //mViewPager.setPageTransformer(true, new RotateUpTransformer());

        next();
        configViewPager(mViewPager, mAdapter, mIndicator);

        if(mRootView.findViewById(getProgressBarResID())!=null)
            mProgressBar= mRootView.findViewById(getProgressBarResID());


        onViewCreated(mRootView);
        return mRootView;
    }
    protected void configSwipeRefreshLayout(SwipeRefreshLayout view) {
        super.configSwipeRefreshLayout(view);
        /*
        if(view!=null)
            view.setEnabled(false);
        */
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
            Object iBaseObject = getDataProvider().get(position);
            onPageChange(iBaseObject, position);

            if (getDataProvider().getCanLoadNext() && position == getDataProvider().size() - 1)
                next();


            if (getDataProvider().getCanLoadRefresh() && position == 0) {
                if (getSwipeRefreshLayout() != null) {
                    refresh();

                    getSwipeRefreshLayout().setEnabled(true);
                }
                else
                    refresh();
            } else if (getSwipeRefreshLayout() != null) {
                getSwipeRefreshLayout().setEnabled(false);
            }
        }
    }



    public void onFetchingStart(boolean isFetchingRefresh) {
        super.onFetchingStart(isFetchingRefresh);
        if(mProgressBar!=null && firstItemIn==false)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onFetchingError(Object error) {
        if(this.mEmptyViewHelper != null) {
            this.mEmptyViewHelper.updateView(this.getDataProvider());
        }

        this.updateSwipeRefreshLayout(false);
        updateProgress();
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {
        super.onFetchingEnd(dataList,isFetchingRefresh);
        updateProgress();
    }
    public void onFetchingFinish(boolean isFetchingRefresh) {
        super.onFetchingFinish(isFetchingRefresh);
        updateProgress();
    }
    protected void updateProgress() {
        final Handler handler = new Handler();
        final Runnable runnablelocal = new Runnable() {
            @Override
            public void run() {
                if(firstDataIn) {
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.invalidate();
                    firstDataIn=false;
                }
            }
        };
        handler.postDelayed(runnablelocal, 1000);

        if(mProgressBar!=null)
            mProgressBar.setVisibility(View.GONE);
        firstItemIn = true;
    }
}
