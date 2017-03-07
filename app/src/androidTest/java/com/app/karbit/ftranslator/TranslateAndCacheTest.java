package com.app.karbit.ftranslator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.app.karbit.ftranslator.Model.ModelFacade;
import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
import com.app.karbit.ftranslator.Presenter.Presenter;
import com.app.karbit.ftranslator.View.iService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Observer;

/**
 * Created by Karbit on 03.03.2017.
 */
@RunWith(AndroidJUnit4.class)
public class TranslateAndCacheTest {
    private Object trigger = new Object();

    @Test
    public void testTranslateAndCacheTest() throws Exception {
        Presenter presenter = new Presenter(new ModelFacade());
        presenter.setBind(new Service());
        TranslationEntity entity = new TranslationEntity("test","en","ru");
        presenter.getTranslation(entity);
        try {
            synchronized (trigger) {
                trigger.wait(3000);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    class Service implements iService{

        @Override
        public Context getContext() {
            return InstrumentationRegistry.getContext();
        }

        @Override
        public void showTranslation(TranslationEntity entity) {
            System.out.println("translation of 'test' = " + entity.getToText() + "\n");
            Assert.assertEquals("тест",entity.getToText());
            synchronized (trigger) {
                trigger.notify();
            }
        }

        @Override
        public void setLanguages(String languageFrom, String languageTo) {

        }

        @Override
        public void getLanguages(Observer observer) {

        }

        @Override
        public void showHistoryDialog() {

        }
    }
}
