package com.kimeeo.kAndroid.viewPagerExtDemo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;

/**
 * Created by BhavinPadhiyar on 22/05/16.
 */
public class FlippableVerticalStackViewPager extends com.kimeeo.kAndroid.viewPagerExt.flippableStackView.VerticalFlippableStackViewPager {

    public String getItemTitle(int position, Object o) {
        if (o instanceof DataObject) {
            DataObject data = (DataObject) o;
            return data.name;

        }
        return "";
    }

    @Override
    public View getView(int i, Object o) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sample_list_view_item_card_view, null);
        return view;
    }

    @NonNull
    @Override
    protected DataProvider createDataProvider() {
        return new StaticDataProvider1(true, true,4);
    }

    @Override
    public BaseItemHolder getItemHolder(View view, int i, Object o) {
        return new BaseItemHolder1(view);
    }

    public class BaseItemHolder1 extends BaseItemHolder {
        public BaseItemHolder1(View itemView) {
            super(itemView);
        }

        @Override
        public void cleanView(View view, int i) {

        }

        @Override
        public void updateItemView(Object o, View view, int i) {
            DataObject data = (DataObject) o;
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(i + ". " + data.name);
        }
    }
}
