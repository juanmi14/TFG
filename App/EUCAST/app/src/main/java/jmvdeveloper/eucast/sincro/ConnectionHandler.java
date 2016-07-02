package jmvdeveloper.eucast.sincro;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.bd.BDHandler;
import jmvdeveloper.eucast.logic.Antibiotic;
import jmvdeveloper.eucast.logic.AntibioticFamily;
import jmvdeveloper.eucast.logic.Note;
import jmvdeveloper.eucast.logic.SensitivityQuery;
import jmvdeveloper.eucast.logic.jFamilies;

/**
 * Created by Juanmi on 14/06/2016.
 */
public class ConnectionHandler {
    private static String respuesta;

    private String componerJSON(){
        //gson-2.6.2.jar
        Gson gson = new GsonBuilder().create();
        JSONObject jo;
        return "hola";
    }

    public static void pruebaGsonGroup(TextView tv){
        SensitivityQuery rq = new SensitivityQuery();
        Antibiotic micro = new Antibiotic();
        micro.setName("Ampicillin");
        micro.setLink("www.comomolo.com");
        AntibioticFamily g = new AntibioticFamily();
        g.setName("Penicillins");
        List<Note> ownNotes = new ArrayList();
        ownNotes.add(new Note("1", "value1"));
        ownNotes.add(new Note("2", "value2"));
        ownNotes.add(new Note("3", "value3"));
        g.setOwnNotes(ownNotes);
        rq.setAntibioticFamily(g);
        rq.setAntibiotic(micro);
        rq.setSensitivity("Resistant");
        Gson gson = new GsonBuilder().create();
        String groupJson = gson.toJson(rq);
        Log.v("GroupJson", groupJson);
        tv.setText(groupJson);
        SensitivityQuery rq2 = gson.fromJson(groupJson, SensitivityQuery.class);
        Log.v("Pude convertirlo", rq2.getSensitivity());
//        try {
//            JSONArray ja = new JSONArray(groupJson);
//            int i;
//            for(i = 0; i < ja.length(); i++){
//                JSONObject obj = (JSONObject)ja.get(i);
//                if(obj.getString("resistance") != null)
//                    Log.v("Wiiiiiiiiiii", obj.getString("resistance"));
//            }
//        } catch (JSONException e) {
//            Log.e("MEEEEEEEE", "No he podido parsear");
//        }
    }

    public interface VolleyCallback{
        void onSuccess(boolean success);
    }

    public static void checkConnection(Context ctx, VolleyCallback callback){
        try {
            RequestQueue queue = Volley.newRequestQueue(ctx);
            StringRequest sr = new StringRequest(Request.Method.GET,
                    Configuracion.getPreferenciaString("url_analyzer"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.v("RESPUETA OK: ", response);
                                callback.onSuccess(true);
                            } catch (Exception e) {
                                Log.e("ErrorCheckingConn", e.getMessage());
                                callback.onSuccess(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorCheckingConn", "Connection not established");
                    callback.onSuccess(false);
                }
            }){

            };
            queue.add(sr);
//            callback.onSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onSuccess(false);
        }
    }

    public static void checkSensitivity(Context ctx, TextView tv, final String family,
                                        final String antibiotic, final String diameter){
        try {
            RequestQueue queue = Volley.newRequestQueue(ctx);
            StringRequest sr = new StringRequest(Request.Method.POST,
                    Configuracion.getPreferenciaString("url_analyzer"),
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.v("RESPUETA OK: ", response);
//                        JSONArray arr = new JSONArray(response);
//                        String resistance = "ND";
//                        for(int i=0; i<arr.length(); i++){
//                            JSONObject obj = (JSONObject) arr.get(i);
//                            resistance = obj.getString("resistance");
//                        }

//                        tv.setText(resistance);
                        String sensibility = "not found";
                        Gson gson = new Gson();
                        SensitivityQuery rq = gson.fromJson(response, SensitivityQuery.class);
                        if(rq.getSensitivity() == null || rq.getSensitivity().isEmpty())
                            rq.setSensitivity(sensibility);
                        Log.v("____SENSITIVITY", sensibility);
                        if(!rq.getSensitivity().equals("not found")) {
                            rq.setFamily(family);
                            rq.setDiameter(diameter);
                            rq.setAntibioticName(rq.getAntibiotic() != null ? rq.getAntibiotic().getName() : "Not Defined");
                            tv.setText(rq.getSensitivity());

                            BDHandler bd = new BDHandler(ctx);
                            bd.insertSensibilityQuery(rq);
                            bd.cerrar();
                            if(Util.sensAdapter != null)
                                Util.sensAdapter.addItem(rq);
                            Util.mostrarToast(ctx, "Query saved");
                        }else{
                            tv.setText("Not Found");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("ErrorParsingJSON", e.getMessage());
                        //tv.setText("Error en respuesta o proceso: " + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ConnectionError", "Connection FAIL");
                    tv.setText("Connection FAIL");
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("family",family);
                    params.put("antibiotic",antibiotic);
                    params.put("diameter",diameter);
//                    params.put("pass",userAccount.getPassword());
//                    params.put("comment", Uri.encode(comment));
//                    params.put("comment_post_ID",String.valueOf(postId));
//                    params.put("blogId",String.valueOf(blogId));

                    return params;
                }

//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String,String> params = new HashMap<String, String>();
////                    params.put("Content-Type","application/x-www-form-urlencoded");
//                    return params;
//                }
            };
            queue.add(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFamilies(Context cxt){
        try {
            RequestQueue queue = Volley.newRequestQueue(cxt);
            JsonObjectRequest jsObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    Configuracion.getPreferenciaString("url_analyzer") + "/families",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(!response.getString("families").equals("error")) {
                                    Gson gson = new Gson();
                                    Log.v("----RESPUESTA: ", response.toString());
                                    jFamilies jf = gson.fromJson(response.toString(), jFamilies.class);
                                    BDHandler bd = new BDHandler(cxt);
                                    Log.v(">>Insert Families: ", String.valueOf(bd.insertFamiliNames(jf.getFamilies())));
                                    Log.v(">>Insert Antimicro: ", String.valueOf(bd.insertAntibioticNames(jf.getAntibiotics())));
                                    Log.v("--Families--", String.valueOf(jf.getFamilies().size()));
                                    Log.v("--Antimicrobials--", String.valueOf(jf.getAntibiotics().size()));
                                    bd.cerrar();
                                }else{
                                    Log.e("ERROR_REQUEST", "Server returned error");
                                }
                            } catch (JSONException e) {
                                Log.e("----RESPUESTA: ", response.toString());
                                Log.e("ERROR_REQUEST", "Error Respuesta en JSON: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d("ERROR_REQUEST", "Error Respuesta en JSON: " + error.getMessage());

                        }
                    }
            );
            queue.add(jsObjectRequest);
            return respuesta;
        }catch(Exception e ){
            Log.e("ERROR_REQUEST", "ERROR DE APLICACIÓN: " + e.getMessage());
            return "Error";
        }
    }
}
