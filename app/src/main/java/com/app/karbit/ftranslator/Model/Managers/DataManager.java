package com.app.karbit.ftranslator.Model.Managers;

import android.content.Context;

import com.app.karbit.ftranslator.Model.DaoMaster;
import com.app.karbit.ftranslator.Model.DaoSession;
import com.app.karbit.ftranslator.Model.Entities.TranslationEntity;

import org.greenrobot.greendao.database.Database;

import java.util.Collections;
import java.util.List;

/**
 * Created by Karbit on 01.03.2017.
 */

public class DataManager {
    public static DataManager DManager = new DataManager();
    private DaoSession daoSession;

    public void initDb(Context context){
        if (daoSession == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db");
            Database db = helper.getWritableDb();
            daoSession = new DaoMaster(db).newSession();
        }
    }

    public void insertTranslationEntity(TranslationEntity entity){
        daoSession.getTranslationEntityDao().insert(entity);
        daoSession.clear();
    }

    public List<TranslationEntity> getAllTranslationEntities(){
        List<TranslationEntity> entities = daoSession.getTranslationEntityDao().loadAll();
        daoSession.clear();
        Collections.reverse(entities);
        return entities;
    }
}
