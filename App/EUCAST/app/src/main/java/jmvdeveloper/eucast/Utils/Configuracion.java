package jmvdeveloper.eucast.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//esta clase permite usar las preferencias compartidas sin tener que crear un objeto para ello en cada
//clase en la que se quieran utilizar
public class Configuracion {
    private static SharedPreferences preferencias;
    private static SharedPreferences.Editor editor;

    public Configuracion() {
    }

    //requeire inicializar las preferencias en la primera activity de la aplicación para obtener el contexto
//    public Configuracion(Context ctx){
//        this.preferencias = ctx.getSharedPreferences("StockMePref", Context.MODE_PRIVATE);
//    }
    public static void inicializarPreferencias(Context ctx) {
        preferencias = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferencias.edit();
    }

    public static void addPreferencia(String nombre, int valor) {
        editor.putInt(nombre, valor);
        editor.commit();
    }

    public static void addPreferencia(String nombre, float valor) {
        editor.putFloat(nombre, valor);
        editor.commit();
    }

    public static void addPreferencia(String nombre, String valor) {
        editor.putString(nombre, valor);
        editor.commit();
    }

    public static void addPreferencia(String nombre, boolean valor) {
        editor.putBoolean(nombre, valor);
        editor.commit();
    }

    //si existe la modifica, si no, la crea
    public static void setPreferencia(String nombre, int valor) {
        if (preferencias.contains(nombre))
            editor.remove(nombre);
        editor.putInt(nombre, valor);
        editor.commit();
    }

    public static void setPreferencia(String nombre, float valor) {
        if (preferencias.contains(nombre))
            editor.remove(nombre);
        editor.putFloat(nombre, valor);
        editor.commit();
    }

    public static void setPreferencia(String nombre, String valor) {
        if (preferencias.contains(nombre))
            editor.remove(nombre);
        editor.putString(nombre, valor);
        editor.commit();
    }

    public static void setPreferencia(String nombre, boolean valor) {
        if (preferencias.contains(nombre))
            editor.remove(nombre);
        editor.putBoolean(nombre, valor);
        editor.commit();
    }

    public static int getPreferenciaInt(String nombre) {
        return preferencias.getInt(nombre, 0);
    }

    public static float getPreferenciaFloat(String nombre) {
        return preferencias.getFloat(nombre, 0.0f);
    }

    public static String getPreferenciaString(String nombre) {
        return preferencias.getString(nombre, null);
    }

    public static boolean getPreferenciaBoolean(String nombre) {
        return preferencias.getBoolean(nombre, false);
    }

    public static void delPreferencia(String nombre) {
        editor.remove(nombre);
        editor.commit();
    }

    public static boolean isPreferencia(String nombre) {
        return preferencias.contains(nombre);
    }
}
