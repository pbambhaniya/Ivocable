package com.multipz.dictionary.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 17-08-2017.
 */

public class CustText extends TextView {

    public CustText(Context context) {
        super(context);
        init();
    }

    public CustText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font.ttf");
        setTypeface(tf);
    }
}
