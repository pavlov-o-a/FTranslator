package com.app.karbit.ftranslator.Model;

import javax.inject.Inject;

/**
 * Created by Karbit on 17.02.2017.
 */

public class ModelFacade {

    @Inject
    public ModelFacade(){

    }

    public TranslationManager getTranslationManager() {
        return new TranslationManager();
    }
}
