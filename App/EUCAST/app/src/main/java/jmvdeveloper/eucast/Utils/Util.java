package jmvdeveloper.eucast.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import jmvdeveloper.eucast.R;
import jmvdeveloper.eucast.adapters.SampleMaterialAdapter;
import jmvdeveloper.eucast.adapters.SensibilityQueryAdapter;

/**
 * Created by Juanmi on 20/06/2016.
 */
public class Util {

    public static boolean vieneDeFragment = false;
    public static boolean reverted = true;
    public static boolean gridSeted = false;

    public static SensibilityQueryAdapter sensAdapter;
    public static RecyclerView recyclerView;

    public static MenuItem iGrid;
    public static MenuItem iOrder;

    public static void crearMensajeAlerta(String mensaje, String titulo, String msgConfirm, String msgCancel,
                                          DialogInterface.OnClickListener listenerConfirm, DialogInterface.OnClickListener listenerCancel, Context ctx){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ctx);
        dlgAlert.setMessage(mensaje);
        dlgAlert.setTitle(titulo);
        dlgAlert.setPositiveButton(msgConfirm, listenerConfirm);
        dlgAlert.setNegativeButton(msgCancel, listenerCancel);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    //esta sobrecarga del método es para usarlo de la forma más habitual, sin un evento para el botón de cancelar y
    //con valores por defecto titulo=Confirmación, acp=Sí, cancl=No
    public static void crearMensajeAlerta(String mensaje, DialogInterface.OnClickListener listenerConfirm, Context ctx){
        crearMensajeAlerta(mensaje, "Confirm", "Yes", "No", listenerConfirm, null, ctx);
    }
    //esta sobrecarga permite además establecer el título del diálogo
    public static void crearMensajeAlerta(String mensaje, String titulo, DialogInterface.OnClickListener listenerConfirm, Context ctx){
        crearMensajeAlerta(mensaje, titulo, "Yes", "No", listenerConfirm, null, ctx);
    }

    public static void mostrarToast(Context context, String mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
