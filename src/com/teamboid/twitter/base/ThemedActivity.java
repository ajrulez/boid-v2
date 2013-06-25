package com.teamboid.twitter.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.teamboid.twitter.R;

public class ThemedActivity extends Activity {

    private int mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTheme = getBoidTheme();
        setTheme(mTheme);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int currentTheme = getBoidTheme();
        if (currentTheme != mTheme) {
            // The theme has changed since the activity was started, recreate the activity now
            recreate();
        }
    }

    public int getBoidTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = Integer.parseInt(prefs.getString("boid_theme", "0"));
        switch (theme) {
            default:
                return R.style.Theme_Boid;
            case 1:
                return R.style.Theme_Boidlight;
        }
    }
}
