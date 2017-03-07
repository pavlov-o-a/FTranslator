package com.app.karbit.ftranslator.View;

import android.content.Context;

import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;

import java.util.Observer;

/**
 * Created by Karbit on 14.02.2017.
 */

public interface iService {

    Context getContext();

    void showTranslation(TranslationEntity entity);

    void setLanguages(String languageFrom, String languageTo);

    void getLanguages(Observer observer);

    void showHistoryDialog();

    void setAlwaysWrapMode(boolean alwaysWrap);
}
