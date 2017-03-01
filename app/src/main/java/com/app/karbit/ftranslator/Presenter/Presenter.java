package com.app.karbit.ftranslator.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.app.karbit.ftranslator.Model.LanguageEntity;
import com.app.karbit.ftranslator.Model.ModelFacade;
import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.Presenter.observes.LanguagesObserver;
import com.app.karbit.ftranslator.Presenter.observes.TranslationEntityObserver;
import com.app.karbit.ftranslator.View.iService;

import java.util.ArrayList;
import java.util.List;
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
        if (isInternetAvailable())
            modelFacade.translate(entity, new TranslationEntityObserver(this), service.getContext());
        else{
            Toast.makeText(service.getContext(),"offline mode",Toast.LENGTH_SHORT).show();
            getOfflineTranslation(entity, new TranslationEntityObserver(this), service.getContext());
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) service.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void getOfflineTranslation(TranslationEntity entity, TranslationEntityObserver translationEntityObserver, Context context) {
        if (entity.getToLanguage().equals("ru") | entity.getFromLanguage().equals("ru")){
            int idResource = context.getResources().getIdentifier(
                    entity.getFromLanguage() + "_" + entity.getToLanguage(),"raw",
                    context.getPackageName());
            modelFacade.translateOffline(entity,idResource,service.getContext(),translationEntityObserver);
        }
    }

    @Override
    public void getLanguages(Observer observer) {
        languagesObserver = observer;
        modelFacade.getLanguages(new LanguagesObserver(this), Locale.getDefault().getDisplayLanguage());
    }

    @Override
    public List<TranslationEntity> getHistory() {
        return modelFacade.getHistory(service.getContext());
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
