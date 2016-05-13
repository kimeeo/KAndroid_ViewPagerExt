package com.kimeeo.kAndroid.viewPagerExt.flippableStackView;

import com.bartoszlipinski.flippablestackview.StackPageTransformer;

/**
 * Created by bhavinpadhiyar on 1/22/16.
 */
abstract public class VerticalFlippableStackViewPager extends DefaultFlippableStackViewPager
{
    public StackPageTransformer.Orientation getOrientation()
    {
        return StackPageTransformer.Orientation.VERTICAL;
    }
}
