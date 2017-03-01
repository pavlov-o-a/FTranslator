package com.app.karbit.ftranslator.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.karbit.ftranslator.Model.TranslationEntity;
import com.app.karbit.ftranslator.R;

import java.util.List;

/**
 * Created by Karbit on 01.03.2017.
 */

public class HistoryDialog extends Dialog {
    private ListView layoutView;

    public HistoryDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_dialog);
        layoutView = (ListView) this.findViewById(R.id.history_main_layout);
    }

    public void setData(List<TranslationEntity> entities){
        TranslationEntitiesAdapter tea = new TranslationEntitiesAdapter(layoutView.getContext(),entities);
        layoutView.setAdapter(tea);
    }

    private class TranslationEntitiesAdapter extends ArrayAdapter<TranslationEntity> {

        public TranslationEntitiesAdapter(Context context, List<TranslationEntity> languages) {
            super(context, R.layout.history_item, languages);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) layoutView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.history_item,parent,false);
            ((TextView)convertView.findViewById(R.id.history_text_from)).setText(getItem(position).getFromText());
            ((TextView)convertView.findViewById(R.id.history_text_to)).setText(getItem(position).getToText());
            ((TextView)convertView.findViewById(R.id.history_languages)).setText(getItem(position).getFromLanguage() + "-" + getItem(position).getToLanguage());
            return convertView;
        }
    }
}
