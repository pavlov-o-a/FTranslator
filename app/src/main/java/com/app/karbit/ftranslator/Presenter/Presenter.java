package com.app.karbit.ftranslator.Presenter;

import javax.inject.Inject;

/**
 * Created by Karbit on 13.02.2017.
 */

public class Presenter implements iPresenter {

    @Inject Presenter(){

    }

    @Override
    public String getValue() {
        return value;
    }
}
