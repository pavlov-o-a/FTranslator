package com.app.karbit.ftranslator.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.karbit.ftranslator.R;

public class MainServiceLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        findViewById(R.id.start_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainServiceLauncherActivity.this, MainService.class));
                finish();
            }
        });
        Toast.makeText(this,"hello!",Toast.LENGTH_SHORT).show();
        Log.d("started","oncreate");
    }

}
