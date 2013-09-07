package com.afollestad.twitter.ui.theming;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.afollestad.twitter.R;

/**
 * Provides a standardized base for all activities that automatically theme themselves based on
 * the application's theme setting; this is basically every activity in the app.
 *
 * @author Aidan Follestad (afollestad)
 */
public class ThemedActivity extends Activity {

    private int mTheme;
    private boolean mDisplayRealNames;

    public final boolean shouldRecreate() {
        int currentTheme = getBoidTheme();
        boolean displayRealNames = shouldDisplayRealNames();
        return currentTheme != mTheme || displayRealNames != mDisplayRealNames;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTheme = getBoidTheme();
        mDisplayRealNames = shouldDisplayRealNames();
        setTheme(mTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldRecreate()) recreate();
    }

    public final boolean shouldDisplayRealNames() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("display_realname", true);
    }

    public final int getBoidTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = Integer.parseInt(prefs.getString("boid_theme", "0"));
        switch (theme) {
            default:
                return R.style.Theme_Boid;
            case 1:
                return R.style.Theme_Boidlight;
            case 2:
                return R.style.Theme_Boidblack;
        }
    }
}