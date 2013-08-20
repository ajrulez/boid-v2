package com.teamboid.twitter.utilities.text;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * @author Aidan Follestad (afollestad)
 */
public class URLSpanNoUnderline extends URLSpan {

    public URLSpanNoUnderline(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}