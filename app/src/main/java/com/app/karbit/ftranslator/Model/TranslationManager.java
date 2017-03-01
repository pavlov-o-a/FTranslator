package com.app.karbit.ftranslator.Model;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Karbit on 18.02.2017.
 */

public class TranslationManager {
    static TranslationManager TManager = new TranslationManager();
    YandexTranslatorApi translatorApi;
    final String KEY = "trnsl.1.1.20170218T180441Z.7406683044641478.9a2625e19fcfb8c3af76021448f327d2c7901e98";

    public TranslationManager(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        translatorApi = retrofit.create(YandexTranslatorApi.class);
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
                    e.onError(new Exception("Something gone wrong"));
                }
            }
        });
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private void cacheResult(TranslationEntity entity, Context context) {
        DataManager dm = DataManager.DManager;
        dm.initDb(context);
        dm.insertTranslationEntity(entity);
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
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(languagesObserver);
    }
}
