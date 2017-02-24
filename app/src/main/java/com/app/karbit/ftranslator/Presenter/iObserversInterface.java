package com.app.karbit.ftranslator.Presenter;

import com.app.karbit.ftranslator.Model.TranslationEntity;

/**
 * Created by Karbit on 18.02.2017.
 */

public interface iObserversInterface {
    void showError(Throwable e);

    void showTranslation(TranslationEntity entity);
}
