package com.example.exam;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class languageManager {

    private Context ct;
    private SharedPreferences preferences;

    public languageManager(Context ctx) {
        ct = ctx;
        preferences = ct.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    public void updateResources(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);


    }
    public String getLang(){
        return preferences.getString("lang","en");
    }

    public void setLang(String code) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lang", code);
        editor.commit();
    }


}
