package com.app.karbit.ftranslator.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.app.karbit.ftranslator.R;

import butterknife.ButterKnife;

/**
 * Created by Karbit on 28.02.2017.
 */

public class MenuDialog extends Dialog {
    iService service;

    public MenuDialog(Context context, iService service) {
        super(context);
        this.service = service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        ButterKnife.bind(this);
    }
}
