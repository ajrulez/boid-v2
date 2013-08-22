package com.afollestad.twitter.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.afollestad.twitter.BoidApp;
import com.afollestad.twitter.R;
import com.afollestad.twitter.adapters.DrawerItemAdapter;
import com.afollestad.twitter.adapters.MainPagerAdapter;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * The activity_main app UI.
 *
 * @author Aidan Follestad (afollestad)
 */
public class MainActivity extends ThemedDrawerActivity {

    private ViewPager mPager;
    private ListView drawerList;
    private PullToRefreshAttacher mPullToRefreshAttacher;

    public PullToRefreshAttacher getPullToRefreshAttacher() {
        return mPullToRefreshAttacher;
    }

    @Override
    public int getDrawerShadowRes() {
        return R.drawable.drawer_shadow;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public int getOpenedTextRes() {
        return R.string.app_name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPullToRefreshAttacher = PullToRefreshAttacher.get(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MainPagerAdapter(getFragmentManager()));
        mPager.setOffscreenPageLimit(5);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                drawerList.setItemChecked(position + 1, true);
            }
        });

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new DrawerItemAdapter(this));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onDrawerItemClicked(position);
            }
        });

        if (BoidApp.get(this).hasAccount()) {
            // Restore the last viewed fragment
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int index = prefs.getInt("recent_fragment_main", 0);
            drawerList.setItemChecked(index + 1, true);
            onDrawerItemClicked(index);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void onDrawerItemClicked(int index) {
        getDrawerLayout().closeDrawers();
        if (index == 0) {
            //Profile viewer
            //TODO
            return;
        }
        mPager.setCurrentItem(index - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("recent_fragment_main", mPager.getCurrentItem()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose:
                startActivity(new Intent(this, ComposeActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.logout:
                confirmLogout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.confirm_logout)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BoidApp.get(MainActivity.this).logout();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}