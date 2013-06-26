package com.teamboid.twitter.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.teamboid.twitter.R;
import com.teamboid.twitter.base.BoidAdapter;
import twitter4j.Trend;

public class TrendAdapter extends BoidAdapter<Trend> {

    public TrendAdapter(Context context) {
        super(context);
    }

    @Override
    public View fillView(int index, View view) {
        Trend item = getItem(index);
        ((TextView) view).setText(item.getName());
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_trend;
    }
}