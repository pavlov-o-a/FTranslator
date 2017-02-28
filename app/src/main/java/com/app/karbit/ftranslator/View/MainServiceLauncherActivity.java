package com.app.karbit.ftranslator.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.app.karbit.ftranslator.R;

public class MainServiceLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FTRtags","onCreate");
        setContentView(R.layout.activity_layout);
        findViewById(R.id.start_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainServiceLauncherActivity.this, MainService.class));
                finish();
            }
        });
        SharedPreferences preferences = getSharedPreferences("FTRANSLATOR_PREFERENCES",MODE_PRIVATE);
        if (!preferences.contains("screen_width")){
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            preferences.edit().putInt("screen_width",width).apply();
            Log.d("FTRtags","saved preference with width = " + width);
        }
    }

}
