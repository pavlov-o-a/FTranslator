package com.app.karbit.ftranslator.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by Karbit on 12.02.2017.
 */

public class CustomRelativeLayout extends RelativeLayout {
    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        SharedPreferences sp = getContext().getSharedPreferences("screen_resolution",Context.MODE_PRIVATE);
        int width = sp.getInt("screen_width",-2);
        Log.d("FTRtags","got width = " + width + " instead " + widthMeasureSpec);
        setMeasuredDimension(width,heightMeasureSpec);
        super.onMeasure(width, heightMeasureSpec);
    }
}
