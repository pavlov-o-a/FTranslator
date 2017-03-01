package com.app.karbit.ftranslator.Model;

import android.content.Context;
import android.util.Log;

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


    public void translateOfline(final TranslationEntity entity, final int idResource, final Context context, Observer<TranslationEntity> subscriber) {
        Observable<TranslationEntity> observable = Observable.create(new ObservableOnSubscribe<TranslationEntity>() {
            @Override
            public void subscribe(ObservableEmitter<TranslationEntity> e) throws Exception {
                String translation = searchMatch(entity.getFromText(),idResource,context);
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

    private String searchMatch(String fromText, int idResource, Context context) {
        String searchedWord = fromText.split(" ")[0];
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
            Log.d("Read from dictionaries:",e.getMessage());//You'll need to add proper error handling here
        }
        return translation;
    }

    private void cacheResult(TranslationEntity entity, Context context) {
        DataManager dm = DataManager.DManager;
        dm.initDb(context);
        dm.insertTranslationEntity(entity);
    }
}
