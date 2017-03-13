package com.app.karbit.ftranslator.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.app.karbit.ftranslator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Karbit on 28.02.2017.
 */

public class MenuDialog extends Dialog {
    private iService service;
    private boolean alwaysWrap = false;

    @OnClick(R.id.menu_history)
    void showHistory(){
        service.showHistoryDialog();
        dismiss();
    }

    @BindView(R.id.always_wrap_mod)
    Switch alwaysWrapSwitch;

    public MenuDialog(Context context, iService service, boolean alwaysWrap) {
        super(context);
        this.service = service;
        this.alwaysWrap = alwaysWrap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        ButterKnife.bind(this);

        alwaysWrapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                service.setAlwaysWrapMode(isChecked);
            }
        });

        alwaysWrapSwitch.setChecked(alwaysWrap);
    }
}
