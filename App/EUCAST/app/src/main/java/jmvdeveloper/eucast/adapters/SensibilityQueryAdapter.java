package jmvdeveloper.eucast.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import jmvdeveloper.eucast.Details;
import jmvdeveloper.eucast.R;
import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.bd.BDHandler;
import jmvdeveloper.eucast.logic.SensitivityQuery;

/**
 * Created by Juanmi on 20/06/2016.
 */
public class SensibilityQueryAdapter extends RecyclerView.Adapter<SensibilityQueryAdapter.ViewHolder>{
    public Context context;
    public List<SensitivityQuery> sensQueries;

    public  SensibilityQueryAdapter(Context context, List<SensitivityQuery> sensibilityQueries){
        this.context = context;
        this.sensQueries = sensibilityQueries;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //aquí se construyen los elementos
        String sensibility = sensQueries.get(position).getSensitivity();
        String family = sensQueries.get(position).getFamily();
        String antibiotic = sensQueries.get(position).getAntibioticName();
        String diameter = sensQueries.get(position).getDiameter();

        holder.sensibility.setText(sensibility);
        int color;
        switch (sensibility){
            case "Sensitive":
                color = ContextCompat.getColor(context, R.color.c_sensitive);
                break;
            case "Resistant":
                color = ContextCompat.getColor(context, R.color.c_resistant);
                break;
            case "Intermedial":
                color = ContextCompat.getColor(context, R.color.c_intermedial);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.c_not_defined);
        }
        holder.sensibility.setBackgroundColor(color);
        holder.family.setText("Antibiotic Family: " + family);
        holder.antibiotic.setText("Antibiotic: " + antibiotic);
        holder.diameter.setText("Diameter: " + diameter + "mm");
//        TextView sensTextView = holder.sensibility;
//        sensTextView.setText(sensibility);
    }

    public void addItem(SensitivityQuery sq){
        if(Configuracion.getPreferenciaString("order").equals("ASC"))
            sensQueries.add(sq);
        else
            sensQueries.add(0, sq);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.sensQueries.size();
    }

//    @Override
//    public long getItemId(int position) {
//        return sensQueries.get(position).get;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.vh_sens_query, parent, false);
        return new ViewHolder(v);
    }

    public void reorderList(){
//        Util.reverted = !Util.reverted;
        if(Configuracion.getPreferenciaString("order").equals("ASC"))
            Configuracion.setPreferencia("order", "DESC");
        else
            Configuracion.setPreferencia("order", "ASC");
        Collections.reverse(this.sensQueries);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sensibility;
        private TextView family;
        private TextView antibiotic;
        private TextView diameter;

        public ViewHolder(View v) {
            super(v);
            sensibility = (TextView) v.findViewById(R.id.q_tv_sensibility);
            family = (TextView) v.findViewById(R.id.q_tv_family);
            antibiotic = (TextView) v.findViewById(R.id.q_tv_antibiotic);
            diameter = (TextView) v.findViewById(R.id.q_tv_diameter);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Util.crearMensajeAlerta("Do you want to delete de query?", "Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SensitivityQuery sq = sensQueries.get(getAdapterPosition());
                            BDHandler bd = new BDHandler(v.getContext());
                            bd.deleteSensibilityQuery(sq, bd.obtenerManejadorEscritura(), true);
                            bd.cerrar();
                            sensQueries.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            Util.mostrarToast(view.getContext(), "Query deleted correctly");
                        }
                    }, v.getContext());
                    return false;
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Details.class);
                    i.putExtra("SensQuery", sensQueries.get(getAdapterPosition()));
                    context.startActivity(i);
                }
            });
        }
    }
}
