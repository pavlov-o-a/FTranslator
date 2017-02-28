package com.app.karbit.ftranslator.Presenter;

import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.View.iService;

import java.util.Observer;

/**
 * Created by Karbit on 13.02.2017.
 */

public interface iPresenter{
    public String value = "dagger test toast";

    public String getValue();

    public void setBind(iService service);

    public void getTranslation(TranslationEntity entity);

    void getLanguages(Observer observer);
}
