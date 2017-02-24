package com.app.karbit.ftranslator.Model;

import com.app.karbit.ftranslator.Presenter.observes.TranslationEntityObserver;

import java.io.IOException;

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
    Observer<TranslationEntity> subscriber;
    Retrofit retrofit;
    final String KEY = "trnsl.1.1.20170218T180441Z.7406683044641478.9a2625e19fcfb8c3af76021448f327d2c7901e98";

    public TranslationManager(){
        retrofit = new Retrofit.Builder().baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public void setSubscriber(TranslationEntityObserver subscriber) {
        this.subscriber = subscriber;
    }

    public void translate(final TranslationEntity entity) {
        Observable<TranslationEntity> observable = Observable.create(new ObservableOnSubscribe<TranslationEntity>() {
            @Override
            public void subscribe(ObservableEmitter<TranslationEntity> e) throws Exception {
                YandexTranslatorApi translatorApi = retrofit.create(YandexTranslatorApi.class);
                Response response = null;
                try {
                    response = translatorApi.getTranslation(KEY, entity.getFromText(), "ru-en").execute();
                } catch (IOException exc){
                    e.onNext(entity);
                }
                if (response != null)
                    entity.setToText(((TranslationEntity.GsonTranslationEntity) response.body()).getText().get(0));
                e.onNext(entity);
            }
        });
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
