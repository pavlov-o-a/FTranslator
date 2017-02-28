package com.app.karbit.ftranslator.Model;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Karbit on 23.02.2017.
 */

public interface YandexTranslatorApi {

    @POST("/api/v1.5/tr.json/translate")
    Call<TranslationEntity.GsonTranslationEntity> getTranslation(@Query("key") String key, @Query("text") String text, @Query("lang") String languages);

    @POST("/api/v1.5/tr.json/getLangs")
    Call<LanguagesAnswer> getLanguages(@Query("key") String key, @Query("ui") String ui);
}
