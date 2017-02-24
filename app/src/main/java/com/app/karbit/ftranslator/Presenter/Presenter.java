package com.app.karbit.ftranslator.Presenter;

import android.widget.Toast;

import com.app.karbit.ftranslator.Model.ModelFacade;
import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.Model.TranslationManager;
import com.app.karbit.ftranslator.Presenter.observes.TranslationEntityObserver;
import com.app.karbit.ftranslator.View.iService;

import javax.inject.Inject;

/**
 * Created by Karbit on 13.02.2017.
 */

public class Presenter implements iPresenter, iObserversInterface{
    iService service;
    ModelFacade modelFacade;

    @Inject Presenter(ModelFacade modelFacade){
        this.modelFacade = modelFacade;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setBind(iService service) {
        this.service = service;
    }

    @Override
    public void getTranslation(TranslationEntity entity) {
        TranslationManager manager = modelFacade.getTranslationManager();
        manager.setSubscriber(new TranslationEntityObserver(this));
        manager.translate(entity);
    }

    @Override
    public void showTranslation(TranslationEntity entity) {
        service.showTranslation(entity);
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText(service.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
