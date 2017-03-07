package com.app.karbit.ftranslator.Model;

import android.content.Context;

import com.app.karbit.ftranslator.Model.Entities.LanguageEntity;
import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
import com.app.karbit.ftranslator.Model.Managers.DataManager;
import com.app.karbit.ftranslator.Model.Managers.OfflineDictsManager;
import com.app.karbit.ftranslator.Model.Managers.TranslationManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;

/**
 * Created by Karbit on 17.02.2017.
 */

public class ModelFacade {

    @Inject
    public ModelFacade(){

    }

    public void translate(final TranslationEntity entity, Observer<TranslationEntity> subscriber, Context context){
        TranslationManager.TManager.translate(entity, subscriber, context);
    }

    public void getLanguages(final Observer<ArrayList<LanguageEntity>>  languagesObserver, final String ui) {
        TranslationManager.TManager.getLanguages(languagesObserver,ui);
    }

    public List<TranslationEntity> getHistory(Context context) {
        DataManager dm = DataManager.DManager;
        dm.initDb(context);
        return dm.getAllTranslationEntities();
    }

    public void translateOffline(TranslationEntity entity, int idResource, Context context, Observer<TranslationEntity> subscriber) {
        OfflineDictsManager.odm.translateOffline(entity,idResource,context,subscriber);
    }
}
