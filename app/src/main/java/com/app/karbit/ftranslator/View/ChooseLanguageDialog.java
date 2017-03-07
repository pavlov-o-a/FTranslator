package com.app.karbit.ftranslator.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.karbit.ftranslator.Model.Entities.LanguageEntity;
import com.app.karbit.ftranslator.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Karbit on 24.02.2017.
 */

public class ChooseLanguageDialog extends Dialog implements Observer{

    private String fromLanguage;
    private String toLanguage;
    private iService service;
    private ArrayList<LanguageEntity> languages;

    @BindView(R.id.language_from)
    Spinner spinnerFrom;

    @BindView(R.id.language_to)
    Spinner spinnerTo;

    @BindView(R.id.progress_bar_conteiner)
    View progressBarConteiner;

    @OnClick(R.id.language_ok)
    void setLanguages(){
        service.setLanguages(fromLanguage,toLanguage);
        this.dismiss();
    }

    public ChooseLanguageDialog(Context context, String fromLanguage, String toLanguage, iService service) {
        super(context);
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.service = service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language_dialog);
        View layoutView = this.findViewById(android.R.id.content);
        ButterKnife.bind(this,layoutView);

        initLangsArray();
    }

    private void initLangsArray() {
        service.getLanguages(this);
    }

    private void getFromResources() {
        ArrayList<LanguageEntity> languageEntities = new ArrayList<>();
        String[] fullNames = progressBarConteiner.getContext().getResources().getStringArray(R.array.languages_full);
        String[] shortNames = progressBarConteiner.getContext().getResources().getStringArray(R.array.languages);
        for (int i = 0; i < fullNames.length; i++){
            languageEntities.add(new LanguageEntity(fullNames[i],shortNames[i]));
        }
        update(null,languageEntities);
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof ArrayList){
            languages = (ArrayList<LanguageEntity>) data;
            setAdapters();
        } else {
            getFromResources();
        }
        progressBarConteiner.setVisibility(View.GONE);
    }

    private void setAdapters() {
        ArrayAdapter<LanguageEntity> langsArray = new LanguageAdapter(getContext(),languages);

        spinnerFrom.setAdapter(langsArray);
        spinnerTo.setAdapter(langsArray);
        for (LanguageEntity languageEntity:languages){
            if (languageEntity.getShortName().equals(fromLanguage))
                spinnerFrom.setSelection(languages.indexOf(languageEntity));
            if (languageEntity.getShortName().equals(toLanguage))
                spinnerTo.setSelection(languages.indexOf(languageEntity));
        }
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguage = ((LanguageEntity)parent.getAdapter().getItem(position)).getShortName();
                ArrayList<LanguageEntity> languageEntities = new ArrayList<>();
                if (languages.size() == 6)//offline mod
                    if (!fromLanguage.equals("ru") & !fromLanguage.equals("en")){
                        for (LanguageEntity entity : languages){
                            if (entity.getShortName().equals("ru") | entity.getShortName().equals("en")){
                                languageEntities.add(entity);
                            }
                        }
                        spinnerTo.setAdapter(new LanguageAdapter(getContext(),languageEntities));
                        setSelectedItem(languageEntities);
                    } else {
                        spinnerTo.setAdapter(new LanguageAdapter(getContext(),languages));
                        setSelectedItem(languages);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguage = ((LanguageEntity)parent.getAdapter().getItem(position)).getShortName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSelectedItem(ArrayList<LanguageEntity> languageEntities){
        for (LanguageEntity languageEntity:languageEntities){
            if (languageEntity.getShortName().equals(fromLanguage))
                spinnerFrom.setSelection(languageEntities.indexOf(languageEntity));
            if (languageEntity.getShortName().equals(toLanguage))
                spinnerTo.setSelection(languageEntities.indexOf(languageEntity));
        }
    }

    private class LanguageAdapter extends ArrayAdapter<LanguageEntity> {

        public LanguageAdapter(Context context, ArrayList<LanguageEntity> languages) {
            super(context, R.layout.spiner_item, languages);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) spinnerFrom.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.spiner_item,parent,false);
            ((TextView)convertView.findViewById(R.id.spinner_item_label)).setText(getItem(position).getFullName());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) spinnerFrom.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.spiner_item,parent,false);
            ((TextView)convertView.findViewById(R.id.spinner_item_label)).setText(getItem(position).getFullName());
            return convertView;
        }
    }
}
