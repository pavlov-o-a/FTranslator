package com.app.karbit.ftranslator.Model.Entities;

/**
 * Created by Karbit on 28.02.2017.
 */

public class LanguageEntity {
    private String fullName;
    private String shortName;

    public LanguageEntity(String fullName, String shortName){
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }
}
