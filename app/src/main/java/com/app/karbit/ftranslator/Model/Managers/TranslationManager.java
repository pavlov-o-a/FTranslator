package com.app.karbit.ftranslator.Model.Managers;

import android.content.Context;

import com.app.karbit.ftranslator.Model.Entities.LanguageEntity;
import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
import com.app.karbit.ftranslator.Model.Net.LanguagesAnswer;
import com.app.karbit.ftranslator.Model.Net.YandexTranslatorApi;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Karbit on 18.02.2017.
 */

public class TranslationManager {
    public static TranslationManager TManager = new TranslationManager();
    YandexTranslatorApi translatorApi;
    private Scheduler subscribeOnScheduler;
    private Scheduler observeOnScheduler;
    final String KEY = "trnsl.1.1.20170218T180441Z.7406683044641478.9a2625e19fcfb8c3af76021448f327d2c7901e98";

    public TranslationManager(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        translatorApi = retrofit.create(YandexTranslatorApi.class);
    }

    public void setSchedulers(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler){
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
    }

    public void translate(final TranslationEntity entity, Observer<TranslationEntity> subscriber, final Context context) {
        Observable<TranslationEntity> observable = Observable.create(new ObservableOnSubscribe<TranslationEntity>() {
            @Override
            public void subscribe(ObservableEmitter<TranslationEntity> e) throws Exception {
                String pairOfLanguages = entity.getFromLanguage() + "-" + entity.getToLanguage();
                Response response = null;
                try {
                    response = translatorApi.getTranslation(KEY, entity.getFromText(), pairOfLanguages).execute();
                } catch (IOException exc){
                    e.onError(exc);
                }
                if (response != null && response.body() != null) {
                    entity.setToText(((TranslationEntity.GsonTranslationEntity) response.body()).getText().get(0));
                    e.onNext(entity);
                    e.onComplete();
                    cacheResult(entity, context);
                } else {
                    e.onError(new Throwable("Something gone wrong"));
                }
            }
        });
        if (subscribeOnScheduler == null)
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        else
            observable.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(subscriber);
    }

    private void cacheResult(TranslationEntity entity, Context context) {
        if (context != null) {
            DataManager dm = DataManager.DManager;
            dm.initDb(context);
            dm.insertTranslationEntity(entity);
        }
    }

    public void getLanguages(final Observer<ArrayList<LanguageEntity>>  languagesObserver, final String ui) {
        Observable<ArrayList<LanguageEntity>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<LanguageEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<LanguageEntity>> e) throws Exception {
                Response response = null;
                LanguagesAnswer la = new LanguagesAnswer();
                try {
                    response = translatorApi.getLanguages(KEY,ui).execute();
                } catch (IOException exc){
                    e.onError(new Exception("some error"));
                }
                if (response != null)
                    la = (LanguagesAnswer) response.body();
                e.onNext(la.getList());
            }
        });
        if(subscribeOnScheduler == null)
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(languagesObserver);
        else
            observable.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(languagesObserver);
    }
}
