package com.app.karbit.ftranslator.Presenter;

import android.widget.Toast;

import com.app.karbit.ftranslator.Model.LanguageEntity;
import com.app.karbit.ftranslator.Model.ModelFacade;
import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.Model.TranslationManager;
import com.app.karbit.ftranslator.Presenter.observes.LanguagesObserver;
import com.app.karbit.ftranslator.Presenter.observes.TranslationEntityObserver;
import com.app.karbit.ftranslator.View.iService;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Observer;

import javax.inject.Inject;

/**
 * Created by Karbit on 13.02.2017.
 */

public class Presenter implements iPresenter, iObserversInterface{
    iService service;
    ModelFacade modelFacade;
    Observer languagesObserver;
    TranslationManager translationManager;

    @Inject Presenter(ModelFacade modelFacade){
        this.modelFacade = modelFacade;
        translationManager = modelFacade.getTranslationManager();
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
        translationManager.translate(entity, new TranslationEntityObserver(this));
    }

    @Override
    public void getLanguages(Observer observer) {
        languagesObserver = observer;
        translationManager.getLanguages(new LanguagesObserver(this), Locale.getDefault().getDisplayLanguage());
    }

    // ----------- observers callbacks ----------

    @Override
    public void showTranslation(TranslationEntity entity) {
        service.showTranslation(entity);
    }

    @Override
    public void showLanguages(ArrayList<LanguageEntity> languageEntities) {
        if (languagesObserver != null){
            languagesObserver.update(null,languageEntities);
        }
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText(service.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
