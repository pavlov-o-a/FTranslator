package com.app.karbit.ftranslator.View;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.Presenter.PresenterModule;
import com.app.karbit.ftranslator.Presenter.iPresenter;
import com.app.karbit.ftranslator.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Component;

/**
 * Created by Karbit on 12.02.2017.
 */

public class MainService extends Service implements iService {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private WindowManager.LayoutParams mParams;
    private boolean isCollapsed = true;
    private int FOCUS = WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN;
    private int FREE = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    private int accessCounter = 0;
    private String languageFrom;
    private String languageTo;

    @Inject iPresenter presenter;

    @BindView(R.id.extended_layout)
    protected RelativeLayout extendedLayout;

    @BindView(R.id.wraped_layout)
    protected RelativeLayout wrappedLayout;

    @BindView(R.id.wraped_cancel_view)
    protected View cancelView;

    @BindView(R.id.extended_wrap)
    protected View wrapButton;

    @BindView(R.id.extended_translate)
    protected View translate;

    @BindView(R.id.extended_close)
    protected View closeButton;

    @BindView(R.id.extended_from_text)
    protected EditText userInput;

    @BindView(R.id.extended_menu)
    protected View menu;

    @BindView(R.id.extended_translated_text)
    protected TextView translatedText;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public Context getContext() {
        return mFloatingView.getContext();
    }

    @Override
    public void showTranslation(TranslationEntity entity) {
        translatedText.setText(entity.getToText());
    }

    @Component(modules = PresenterModule.class)
    public interface MainServiceComponent{
        void inject(MainService ms);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();

        MainServiceComponent msc = DaggerMainService_MainServiceComponent.builder()
                .presenterModule(new PresenterModule()).build();
        msc.inject(this);

        presenter.setBind(this);

        Toast.makeText(mFloatingView.getContext(),presenter.getValue(),Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        final Service service = this;
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.main_layout, null);
        ButterKnife.bind(this,mFloatingView);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        setFocusedWindow(FREE,0,100,false,mFloatingView);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) extendedLayout.getLayoutParams();
        lp.width = 480;
        extendedLayout.setLayoutParams(lp);

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.stopSelf();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.stopSelf();
            }
        });
        mFloatingView.setOnTouchListener(new DragListener());
        wrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendedLayout.setVisibility(View.GONE);
                wrappedLayout.setVisibility(View.VISIBLE);
                isCollapsed = true;
            }
        });
        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocusedWindow(FOCUS,mParams.x,mParams.y,true,mFloatingView);
                userInput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (userInput != null) {
                            userInput.requestFocus();
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                },500);
            }
        });
        //translation method
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslationEntity entity = new TranslationEntity(userInput.getText().toString(),languageFrom,languageTo);
                presenter.getTranslation(entity);
            }
        });
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

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = mParams.x;
                    initialY = mParams.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    setFocusedWindow(FREE,mParams.x,mParams.y,true,mFloatingView);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (accessCounter >= 2) {
                        mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, mParams);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    int Xdiff = (int) (event.getRawX() - initialTouchX);
                    int Ydiff = (int) (event.getRawY() - initialTouchY);
                    if (Xdiff < 20 && Ydiff < 20) {
                        if (isCollapsed) {
                            wrappedLayout.setVisibility(View.GONE);
                            extendedLayout.setVisibility(View.VISIBLE);
                            isCollapsed = false;
                        }
                    }
                    return true;
            }
            return false;
        }
    }

    private void setFocusedWindow(int flag, int x, int y, boolean isUpdate, View view){
        ButterKnife.bind(this,mFloatingView);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                flag,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = x;
        params.y = y;
        mParams = params;
        if (isUpdate) {
            mWindowManager.updateViewLayout(view, params);
        }
        else {
            mWindowManager.addView(view, params);
        }
        if (flag == FREE){
            accessCounter ++;
        } else {
            accessCounter = 0;
        }
    }
}
