package com.app.karbit.ftranslator.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Karbit on 28.02.2017.
 */

public class LanguagesAnswer {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs;

    @SerializedName("langs")
    @Expose
    private Map<String, String> langs;

    public ArrayList<LanguageEntity> getList() {
        ArrayList<LanguageEntity> languageEntities = new ArrayList<>();
        if (langs != null)
            for (String key : langs.keySet()){
                languageEntities.add(new LanguageEntity(langs.get(key),key));
            }
        return languageEntities;
    }
}
