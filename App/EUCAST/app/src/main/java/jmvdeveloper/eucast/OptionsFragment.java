package jmvdeveloper.eucast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;

import java.util.Locale;

import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.sincro.ConnectionHandler;


public class OptionsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Options");

        addPreferencesFromResource(R.xml.options);

        Preference checkConn = findPreference("test_connection");
        checkConn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ConnectionHandler.checkConnection(getActivity().getApplicationContext(),
                        new ConnectionHandler.VolleyCallback() {
                            @Override
                            public void onSuccess(boolean success) {
                                if(success){
                                    Util.mostrarToast(getActivity().getApplicationContext(), "OK. Service is up");
                                }else{
                                    Util.mostrarToast(getActivity().getApplicationContext(), "FAIL. Could not connect with the service");
                                }
                            }
                        });
                return true;
            }
        });

//        Preference tutorial = findPreference("activar_tutoriales");
//        tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
//
//                sharedPreferences.edit().putBoolean("tutoListas", false).apply();
//                sharedPreferences.edit().putBoolean("tutoStock", false).apply();
//                sharedPreferences.edit().putBoolean("tutoArticulo", false).apply();
//
//                Util.mostrarToast(getActivity().getApplicationContext(), getResources().getString(R.string.Tutoriales_reset));
//
//                return true;
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals("idioma")) {
//            String lang = sharedPreferences.getString(key, "es");
//            setLocale(lang);
//        }
        if(key.equals("url_analyzer")){
            Preference pref = findPreference(key);
            pref.setSummary(Configuracion.getPreferenciaString("url_analyzer"));
        }
    }

//    public void setLocale(String lang) {
//        Activity activity = getActivity();
//
//        Locale myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//
//        Intent refresh = new Intent(activity.getApplicationContext(), Preferencias.class);
//        startActivity(refresh);
//        activity.finish();
//        activity.overridePendingTransition(0, 0);
//    }
}
