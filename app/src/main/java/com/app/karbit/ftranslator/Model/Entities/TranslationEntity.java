package com.app.karbit.ftranslator.Model.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * Created by Karbit on 17.02.2017.
 */

@Entity
public class TranslationEntity {

    @Id(autoincrement = true)
    private Long id;

    private String fromText;
    private String toText;
    private String fromLanguage;
    private String toLanguage;

    public String getFromText() {
        return fromText;
    }

    public String getToText() {
        return toText;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToText(String toText) {
        this.toText = toText;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFromText(String fromText) {
        this.fromText = fromText;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public TranslationEntity(String fromText, String fromLanguage, String toLanguage){
        this.fromText = fromText;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
    }

    @Generated(hash = 1420869241)
    public TranslationEntity(Long id, String fromText, String toText, String fromLanguage,
            String toLanguage) {
        this.id = id;
        this.fromText = fromText;
        this.toText = toText;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
    }

    @Generated(hash = 1300368422)
    public TranslationEntity() {
    }

    public static class GsonTranslationEntity{

        @SerializedName("text")
        @Expose
        private List<String> text;
        @SerializedName("lang")
        @Expose
        private String lang;
        @SerializedName("code")
        @Expose
        private String code;

        public List<String> getText() {
            return text;
        }

        public String getLang() {
            return lang;
        }

        public String getCode() {
            return code;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public void setText(List<String> text) {
            this.text = text;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
