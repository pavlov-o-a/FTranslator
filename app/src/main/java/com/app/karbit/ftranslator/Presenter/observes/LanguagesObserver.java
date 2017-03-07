package com.app.karbit.ftranslator.Presenter.observes;

import com.app.karbit.ftranslator.Model.Entities.LanguageEntity;
import com.app.karbit.ftranslator.Presenter.iObserversInterface;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Karbit on 28.02.2017.
 */

public class LanguagesObserver implements Observer<ArrayList<LanguageEntity>> {
    iObserversInterface ioInterface;

    public LanguagesObserver(iObserversInterface ioInterface){
        this.ioInterface = ioInterface;
    }

    @Override
    public void onError(Throwable e) {
        ioInterface.showError(e);
        ioInterface.showLanguages(null);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ArrayList<LanguageEntity> languageEntities) {
        ioInterface.showLanguages(languageEntities);
    }
}
