package com.app.karbit.ftranslator.Presenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Karbit on 13.02.2017.
 */
@Module
public class PresenterModule {
    @Provides iPresenter getPresenter(){
        return new Presenter();
    }
}
