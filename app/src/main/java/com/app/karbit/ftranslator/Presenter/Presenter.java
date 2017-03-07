package com.app.karbit.ftranslator.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.app.karbit.ftranslator.Model.Entities.LanguageEntity;
import com.app.karbit.ftranslator.Model.ModelFacade;
import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
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
    Observer languagesDialogObserver;
    private boolean isAlive = true;

    @Inject public Presenter(ModelFacade modelFacade){
        this.modelFacade = modelFacade;
    }

    // ----------- iPresenter methods --------

    @Override
    public void setBind(iService service) {
        this.service = service;
    }

    @Override
    public void getTranslation(TranslationEntity entity) {
        TranslationEntityObserver teo = new TranslationEntityObserver(this);
        if (isInternetAvailable())
            modelFacade.translate(entity, teo, service.getContext());
        else{
            getOfflineTranslation(entity, teo, service.getContext());
        }
    }

    @Override
    public void getLanguages(Observer observer) {
        languagesDialogObserver = observer;
        if (isInternetAvailable()) {
            LanguagesObserver lo = new LanguagesObserver(this);
            modelFacade.getLanguages(lo, Locale.getDefault().getDisplayLanguage());
        }
        else
            showLanguages(null);
    }

    @Override
    public List<TranslationEntity> getHistory() {
        return modelFacade.getHistory(service.getContext());
    }

    @Override
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    // ------------------------

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) service.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void getOfflineTranslation(TranslationEntity entity, TranslationEntityObserver translationEntityObserver, Context context) {
            int idResource = context.getResources().getIdentifier(
        entity.getFromLanguage() + "_" + entity.getToLanguage(),"raw",
        context.getPackageName());
        if (idResource == 0)
            idResource = context.getResources().getIdentifier(
                    entity.getToLanguage()+"_"+entity.getFromLanguage(),"raw",
                    context.getPackageName());
        if (idResource == 0)
            Toast.makeText(context,"This languages not available in ofline mod. Turn on internet.",Toast.LENGTH_SHORT).show();
        else
            modelFacade.translateOffline(entity,idResource,context,translationEntityObserver);
    }

    // ----------- observers callbacks ----------

    @Override
    public void showTranslation(TranslationEntity entity) {
        if (isAlive)
            service.showTranslation(entity);
    }

    @Override
    public void showLanguages(ArrayList<LanguageEntity> languageEntities) {
        if (isAlive && languagesDialogObserver != null){
            languagesDialogObserver.update(null,languageEntities);
        }
    }

    @Override
    public void showError(Throwable e) {
        if (isAlive)
            Toast.makeText(service.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
