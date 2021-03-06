package com.afollestad.twitter.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.afollestad.twitter.R;
import com.afollestad.twitter.adapters.SearchPagerAdapter;
import com.afollestad.twitter.ui.theming.ThemedPtrActivity;
import com.afollestad.twitter.utilities.Utils;

/**
 * @author Aidan Follestad (afollestad)
 */
public class SearchActivity extends ThemedPtrActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(new SearchPagerAdapter(this, getIntent().getExtras(), getFragmentManager()));

        String query = getIntent().getStringExtra("query");
        if (query.startsWith("@"))
            mPager.setCurrentItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}