package com.app.karbit.ftranslator.View;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.karbit.ftranslator.R;

/**
 * Created by Karbit on 12.02.2017.
 */

public class MainService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    WindowManager.LayoutParams mParams;
    private RelativeLayout bigLayout;
    private RelativeLayout smallLayout;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Service service = this;
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.main_layout, null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        mParams = params;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        bigLayout = (RelativeLayout) mFloatingView.findViewById(R.id.big_layout);
        smallLayout = (RelativeLayout) mFloatingView.findViewById(R.id.small_layout);

        mFloatingView.findViewById(R.id.small_cancel_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.stopSelf();
            }
        });

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) bigLayout.getLayoutParams();
        lp.width = 480;
        bigLayout.setLayoutParams(lp);

        DragListener dl = new DragListener();
        mFloatingView.setOnTouchListener(dl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    public class DragListener implements View.OnTouchListener{
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        private boolean isCollapsed = true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = mParams.x;
                    initialY = mParams.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                    mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                    mWindowManager.updateViewLayout(mFloatingView, mParams);
                    return true;
                case MotionEvent.ACTION_UP:
                    int Xdiff = (int) (event.getRawX() - initialTouchX);
                    int Ydiff = (int) (event.getRawY() - initialTouchY);
                    if (Xdiff < 20 && Ydiff < 20) {
                        if (!isCollapsed) {
                            bigLayout.setVisibility(View.GONE);
                            smallLayout.setVisibility(View.VISIBLE);
                            isCollapsed = true;
                        } else {
                            smallLayout.setVisibility(View.GONE);
                            bigLayout.setVisibility(View.VISIBLE);
                            isCollapsed = false;
                        }
                    }
                    return true;
            }
            return false;
        }
    }
}
