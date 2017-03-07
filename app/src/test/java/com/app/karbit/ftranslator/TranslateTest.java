package com.app.karbit.ftranslator;

import com.app.karbit.ftranslator.Model.Entities.LanguageEntity;
import com.app.karbit.ftranslator.Model.ModelFacade;
import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;
import com.app.karbit.ftranslator.Model.Managers.TranslationManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karbit on 04.03.2017.
 */
public class TranslateTest {
    private Object trigger = new Object();
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void checkTranslation() throws Exception {
        TestObserver<TranslationEntity> to = new TestObserver<TranslationEntity>(){
            @Override
            public void onNext(TranslationEntity entity) {
                System.out.print("translation is: " + entity.getToText());
                synchronized (trigger) {
                    trigger.notify();
                }
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t.getMessage());
            }
        };
        TranslationEntity te = new TranslationEntity("test","en","ru");
        ModelFacade mf = new ModelFacade();
        TranslationManager tm = TranslationManager.TManager;
        tm.setSchedulers(Schedulers.newThread(),Schedulers.trampoline());
        mf.translate(te,to,null);
        try {
            synchronized (trigger) {
                trigger.wait(3000);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkGetLanguages() throws Exception{
        TestObserver<ArrayList<LanguageEntity>> to = new TestObserver<ArrayList<LanguageEntity>>(){
            @Override
            public void onNext(ArrayList<LanguageEntity> entity) {
                System.out.print("size of list is: " + entity.size());
                synchronized (trigger) {
                    trigger.notify();
                }
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t.getMessage());
            }
        };
        ModelFacade mf = new ModelFacade();
        TranslationManager tm = TranslationManager.TManager;
        tm.setSchedulers(Schedulers.newThread(),Schedulers.trampoline());
        mf.getLanguages(to,"ru");
        try {
            synchronized (trigger) {
                trigger.wait(3000);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
