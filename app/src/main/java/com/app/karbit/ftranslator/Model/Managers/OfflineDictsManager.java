package com.app.karbit.ftranslator.Model.Managers;

import android.content.Context;
import android.util.Log;

import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karbit on 01.03.2017.
 */

public class OfflineDictsManager {

    public static OfflineDictsManager odm = new OfflineDictsManager();


    public void translateOffline(final TranslationEntity entity, final int idResource, final Context context, Observer<TranslationEntity> subscriber) {
        final Observable<TranslationEntity> observable = Observable.create(new ObservableOnSubscribe<TranslationEntity>() {
            @Override
            public void subscribe(ObservableEmitter<TranslationEntity> e) throws Exception {
                String translation = null;
                //this reading from dictionaries is complicated because of different structure of dictionaries
                if (entity.getFromLanguage().equals("ru") | entity.getToLanguage().equals("ru"))
                    translation = searchMatchRu(entity.getFromText(),idResource,context);
                else {
                    if (entity.getFromLanguage().equals("en"))
                        translation = searchMatchDirect(entity.getFromText(),idResource,context);
                    else
                        translation = searchMatchReversed(entity.getFromText(),idResource,context);
                }
                if (translation == null)
                    entity.setToText("Translation not found");
                else {
                    entity.setToText(translation);
                    cacheResult(entity, context);
                }
                e.onNext(entity);
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private String searchMatchRu(String fromText, int idResource, Context context) {
        InputStream is = context.getResources().openRawResource(idResource);
        String translation = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() >= fromText.length() && line.charAt(0) == fromText.charAt(0)){
                    for (int i = 1; i < fromText.length(); i++) {
                        if (line.charAt(i) == fromText.charAt(i)) {
                            if (i == fromText.length() - 1) {
                                line = br.readLine();
                                br.close();
                                is.close();
                                return line;
                            }
                        } else
                            break;
                    }
                }
            }
            br.close();
            is.close();
            return null;
        }
        catch (Exception e) {
            Log.d("Read from dictionaries:",e.getMessage());
        }
        return translation;
    }

    private String searchMatchDirect(String fromText, int idResource, Context context) {
        InputStream is = context.getResources().openRawResource(idResource);
        String translation = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() >= fromText.length() && line.charAt(0) == fromText.charAt(0)){
                    for (int i = 1; i < fromText.length(); i++) {
                        if (line.charAt(i) == fromText.charAt(i)) {
                            if (i == fromText.length() - 1) {
                                line = line.substring(i+2);
                                br.close();
                                is.close();
                                return line;
                            }
                        } else
                            break;
                    }
                }
            }
            br.close();
            is.close();
            return null;
        }
        catch (Exception e) {
            Log.d("Read from dictionaries:",e.getMessage());
        }
        return translation;
    }

    private String searchMatchReversed(String fromText, int idResource, Context context) {
        InputStream is = context.getResources().openRawResource(idResource);
        String translation = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '\t') {
                        for (int j = 0; j < fromText.length();j++){
                            if (line.length() >= (i+j+1) && line.charAt(i+j+1) == fromText.charAt(j)) {
                                if (j == fromText.length() - 1) {
                                    line = line.substring(0,i);
                                    br.close();
                                    is.close();
                                    return line;
                                }
                            } else
                                break;
                        }
                        break;
                    }
                }
            }
            br.close();
            is.close();
            return null;
        }
        catch (Exception e) {
            Log.d("Read from dictionaries:",e.getMessage());
        }
        return translation;
    }

    private void cacheResult(TranslationEntity entity, Context context) {
        DataManager dm = DataManager.DManager;
        dm.initDb(context);
        dm.insertTranslationEntity(entity);
    }
}
