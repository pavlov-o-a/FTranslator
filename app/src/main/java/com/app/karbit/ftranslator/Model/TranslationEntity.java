package com.app.karbit.ftranslator.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Karbit on 17.02.2017.
 */

public class TranslationEntity {
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

    private String fromText;

    public void setToText(String toText) {
        this.toText = toText;
    }

    private String toText;
    private String fromLanguage;
    private String toLanguage;

    public TranslationEntity(String fromText, String fromLanguage, String toLanguage){
        this.fromText = fromText;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
    }

    public TranslationEntity(String fromText, String fromLanguage, String toLanguage, String toText){
        this.fromText = fromText;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.toText = toText;
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
