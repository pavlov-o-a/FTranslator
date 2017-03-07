package com.app.karbit.ftranslator.View;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
import com.app.karbit.ftranslator.Presenter.PresenterModule;
import com.app.karbit.ftranslator.Presenter.iPresenter;
import com.app.karbit.ftranslator.R;

import java.util.List;
import java.util.Observer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Component;

import static android.content.ClipDescription.MIMETYPE_TEXT_HTML;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

/**
 * Created by Karbit on 12.02.2017.
 */

public class MainService extends Service implements iService {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private WindowManager.LayoutParams mParams;
    private boolean isCollapsed = true;
    private int FOCUS = WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
    private int FREE = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
    private boolean alwaysWrapMode;
    private boolean hidedInNotifications = false;
    private int accessCounter = 0;
    private int screenWidth;
    private String languageFrom;
    private String languageTo;
    private String LANGUAGE_FROM_PREF = "language_from";
    private String LANGUAGE_TO_PREF = "language_to";
    private String ALWAYS_WRAP = "always_wrap";
    private String userInputContent;


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

    @BindView(R.id.extended_hide)
    protected View hideButton;

    @BindView(R.id.extended_from_text)
    protected EditText userInput;

    @OnClick(R.id.extended_menu)
    void showSettings(){
        showAdvancedSettings();
    }

    @BindView(R.id.extended_translated_text)
    protected TextView translatedText;

    @BindView(R.id.extended_to_language)
    protected TextView toLanguageLabel;

    @BindView(R.id.extended_from_language)
    protected TextView fromLanguageLabel;

    @OnClick(R.id.extended_to_language)
    void changeLanguage(){
       showChooseLanguageDialog();
    }

    @OnClick(R.id.extended_from_language)
    void changeLanguage2(){
        showChooseLanguageDialog();
    }

    @Component(modules = PresenterModule.class)
    public interface MainServiceComponent{
        void inject(MainService ms);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp = getSharedPreferences("FTRANSLATOR_PREFERENCES",MODE_PRIVATE);
        screenWidth = sp.getInt("screen_width",480);
        initView();

        //dagger initialization
        MainServiceComponent msc = DaggerMainService_MainServiceComponent.builder()
                .presenterModule(new PresenterModule()).build();
        msc.inject(this);
        presenter.setBind(this);

        //foregrounding
        throwNotification();

        //last used language
        setLanguages(sp.getString(LANGUAGE_FROM_PREF,"en"),sp.getString(LANGUAGE_TO_PREF,"de"));
        alwaysWrapMode = sp.getBoolean(ALWAYS_WRAP,false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (hidedInNotifications) {//if viewed again from notification
            initView();
            setLanguages(languageFrom, languageTo);
            userInput.setText(userInputContent);
            hidedInNotifications = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void throwNotification() {
        Intent notificationIntent = new Intent(getContext(), MainService.class);
        PendingIntent contentIntent = PendingIntent.getService(getContext(),
                0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getContext())
                .setSmallIcon(R.drawable.logo_book)
                .setContentTitle("FTranslator")
                .setContentIntent(contentIntent)
                .setContentText("Tap to open FTranslator")
                .setAutoCancel(true)
                .build();
        startForeground(1445,notification);
    }

    private void showChooseLanguageDialog(){
        ChooseLanguageDialog dialog = new ChooseLanguageDialog(getContext(),languageFrom,languageTo,this);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void showAdvancedSettings() {
        MenuDialog dialog = new MenuDialog(getContext(),this,alwaysWrapMode);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void initView() {
        final Service service = this;
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.main_layout, null);
        ButterKnife.bind(this,mFloatingView);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        setFocusedWindow(FREE,0,100,false,mFloatingView);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) extendedLayout.getLayoutParams();
        lp.width = screenWidth;
        extendedLayout.setLayoutParams(lp);

        cancelView.setOnClickListener(new StopServiceListener(service));
        closeButton.setOnClickListener(new StopServiceListener(service));
        hideButton.setOnClickListener(new HideServiceListener());
        mFloatingView.setOnTouchListener(new DragListener());
        wrapButton.setOnClickListener(new WrapViewListener());
        //it's needed for showing keyboard
        userInput.setOnClickListener(new UserInputListener());
        //translation method
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslationEntity entity = new TranslationEntity(userInput.getText().toString(),languageFrom,languageTo);
                presenter.getTranslation(entity);
            }
        });
        setFocusedWindow(FOCUS,mParams.x,mParams.y,true,mFloatingView);
        extendedLayout.setVisibility(View.INVISIBLE);
        userInput.postDelayed(new Runnable() {
            @Override
            public void run() { //it is needed for userinput got focus(window become focused). so user mustn't click userinput twice in first time.
                if (userInput != null) {
                    userInput.requestFocus();
                    extendedLayout.setVisibility(View.GONE);
                }
            }
        },500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.setIsAlive(false);
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void tryLoadDataFromClipBoardManager() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN) |
                        clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_HTML))) {
        } else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            userInput.setText(item.getText().toString());
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

    // ---------- click listeners --------------

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
                            if (alwaysWrapMode)
                                tryLoadDataFromClipBoardManager();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_OUTSIDE:
                    if (!isCollapsed && alwaysWrapMode){
                        extendedLayout.setVisibility(View.GONE);
                        wrappedLayout.setVisibility(View.VISIBLE);
                        isCollapsed = true;
                    }
                    return true;
            }
            return false;
        }
    }

    private class StopServiceListener implements View.OnClickListener{
        private Service service;

        public StopServiceListener(Service service){
            this.service = service;
        }

        @Override
        public void onClick(View v) {
            service.stopSelf();
        }
    }

    private class HideServiceListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            throwNotification();
            userInputContent = userInput.getText().toString();
            hidedInNotifications = true;
            isCollapsed = true;
            mWindowManager.removeViewImmediate(mFloatingView);
        }
    }

    private class WrapViewListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            extendedLayout.setVisibility(View.GONE);
            wrappedLayout.setVisibility(View.VISIBLE);
            isCollapsed = true;
        }
    }

    private class UserInputListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            setFocusedWindow(FOCUS,mParams.x,mParams.y,true,mFloatingView);
            userInput.postDelayed(new Runnable() {
                @Override
                public void run() {      //we have to wait until window will get new params and become focused,
                    if (userInput != null) {            //before calling keyboard
                        userInput.requestFocus();
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            },500);
        }
    }

    // ---------- interface methods ------------

    @Override
    public Context getContext() {
        return mFloatingView.getContext();
    }

    @Override
    public void showTranslation(TranslationEntity entity) {
        translatedText.setText(entity.getToText());
    }

    @Override
    public void setLanguages(String languageFrom, String languageTo) {
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        fromLanguageLabel.setText(languageFrom);
        toLanguageLabel.setText(languageTo);
        SharedPreferences sp = getSharedPreferences("FTRANSLATOR_PREFERENCES",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LANGUAGE_FROM_PREF,languageFrom);
        editor.putString(LANGUAGE_TO_PREF,languageTo);
        editor.apply();
    }

    @Override
    public void getLanguages(Observer observer) {
        presenter.getLanguages(observer);
    }

    public void showHistoryDialog() {
        List<TranslationEntity> entities = presenter.getHistory();
        HistoryDialog dialog = new HistoryDialog(getContext());
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setData(entities);
    }

    @Override
    public void setAlwaysWrapMode(boolean alwaysWrap) {
        this.alwaysWrapMode = alwaysWrap;
        getSharedPreferences("FTRANSLATOR_PREFERENCES",MODE_PRIVATE).edit().putBoolean(ALWAYS_WRAP,alwaysWrap).apply();
    }
}
