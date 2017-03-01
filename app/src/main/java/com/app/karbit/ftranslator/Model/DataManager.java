package com.app.karbit.ftranslator.Model;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by Karbit on 01.03.2017.
 */

public class DataManager {
    public static DataManager DManager = new DataManager();
    private DaoSession daoSession;

    public void initDb(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,"notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public void insertTranslationEntity(TranslationEntity entity){
        daoSession.getTranslationEntityDao().insert(entity);
        daoSession.clear();
    }

    public List<TranslationEntity> getAllTranslationEntities(){
        List<TranslationEntity> entities = daoSession.getTranslationEntityDao().loadAll();
        daoSession.clear();
        return entities;
    }
}
