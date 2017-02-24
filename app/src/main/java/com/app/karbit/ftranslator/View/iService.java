package com.app.karbit.ftranslator.View;

import android.content.Context;

import com.app.karbit.ftranslator.Model.TranslationEntity;

/**
 * Created by Karbit on 14.02.2017.
 */

public interface iService {

    Context getContext();

    void showTranslation(TranslationEntity entity);
}
