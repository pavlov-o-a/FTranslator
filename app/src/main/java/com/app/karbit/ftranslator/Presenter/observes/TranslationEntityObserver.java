package com.app.karbit.ftranslator.Presenter.observes;

import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.Presenter.iObserversInterface;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Karbit on 18.02.2017.
 */

public class TranslationEntityObserver implements Observer<TranslationEntity> {
    iObserversInterface ioInterface;

    public TranslationEntityObserver(iObserversInterface ioInterface){
        this.ioInterface = ioInterface;
    }

    @Override
    public void onError(Throwable e) {
        ioInterface.showError(e);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(TranslationEntity entity) {
        ioInterface.showTranslation(entity);
    }
}
