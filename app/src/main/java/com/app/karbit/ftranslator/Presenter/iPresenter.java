package com.app.karbit.ftranslator.Presenter;

import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
import com.app.karbit.ftranslator.View.iService;

import java.util.List;
import java.util.Observer;

/**
 * Created by Karbit on 13.02.2017.
 */

public interface iPresenter{

    void setBind(iService service);

    void getTranslation(TranslationEntity entity);

    void getLanguages(Observer observer);

    List<TranslationEntity> getHistory();

    void setIsAlive(boolean isAlive);
}
