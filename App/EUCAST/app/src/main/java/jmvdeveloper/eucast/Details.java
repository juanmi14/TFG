package jmvdeveloper.eucast;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.bd.BDHandler;
import jmvdeveloper.eucast.logic.Note;
import jmvdeveloper.eucast.logic.SensitivityQuery;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Query details");

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tv_sensibility = (TextView)findViewById(R.id.details_tv_sensibility);
        TextView tv_family = (TextView)findViewById(R.id.details_tv_family);
        TextView tv_antibiotic = (TextView)findViewById(R.id.details_tv_antibiotic);
        TextView tv_diameter = (TextView)findViewById(R.id.details_tv_diameter);

        TextView tv_antibiotic_title = (TextView)findViewById(R.id.details_tv_antibiotic_title);
        TextView tv_antibiotic_name = (TextView)findViewById(R.id.details_tv_antibiotic_name);
        TextView tv_antibiotic_link = (TextView)findViewById(R.id.details_tv_antibiotic_link);
        TextView tv_antibiotic_link_viewer = (TextView)findViewById(R.id.details_tv_antibiotic_link_viewer);
        TextView tv_antibiotic_s = (TextView)findViewById(R.id.details_tv_antibiotic_s);
        TextView tv_antibiotic_r = (TextView)findViewById(R.id.details_tv_antibiotic_r);
        LinearLayout ly_antibiotic_notes = (LinearLayout)findViewById(R.id.details_ly_antibiotic_notes);

        CardView cd_ntibiotic = (CardView)findViewById(R.id.details_cl_antibiotic);
        Spinner sp_notes = (Spinner)findViewById(R.id.details_sp_antibiotic_notes);
        TextView tv_antibFam_notes_viewer = (TextView)findViewById(R.id.details_tv_antibiotic_notes_viewer);

        TextView tv_antibiotic_family_title = (TextView)findViewById(R.id.details_tv_antibiotic_family_title);
        TextView tv_antibiotic_family_name = (TextView)findViewById(R.id.details_tv_antibiotic_family_name);
        LinearLayout ly_antibiotic_family_notes = (LinearLayout)findViewById(R.id.details_ly_antibiotic_family_notes);
        Spinner sp_family_notes = (Spinner)findViewById(R.id.details_sp_antibiotic_family_notes);
        TextView tv_antibFam_family_notes_viewer = (TextView)findViewById(R.id.details_tv_antibiotic_family_notes_viewer);

        Intent i = getIntent();
        if (i != null){
            if (i.getExtras() != null){
                SensitivityQuery sq = i.getExtras().getParcelable("SensQuery");
                BDHandler bd = new BDHandler(this);
                sq = bd.getSensibilityQueryComplex(sq.getFamily(), sq.getAntibioticName(), sq.getDiameter());
                if(sq != null){
                    tv_sensibility.setText(sq.getSensitivity());
                    int color;
                    switch (sq.getSensitivity()){
                        case "Sensitive":
                            color = ContextCompat.getColor(this, R.color.c_sensitive);
                            break;
                        case "Resistant":
                            color = ContextCompat.getColor(this, R.color.c_resistant);
                            break;
                        case "Intermedial":
                            color = ContextCompat.getColor(this, R.color.c_intermedial);
                            break;
                        default:
                            color = ContextCompat.getColor(this, R.color.c_not_defined);
                    }
                    //main
                    tv_sensibility.setBackgroundColor(color);
                    tv_family.setText("Antibiotic Family: " + sq.getFamily());
                    tv_antibiotic.setText("Antibiotic: " + sq.getAntibioticName());
                    tv_diameter.setText("Diameter: " + sq.getDiameter() + "mm");

                    //antibiotic cd
                    tv_antibiotic_title.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                    tv_antibiotic_name.setText(sq.getAntibiotic().getName());
                    String link = sq.getAntibiotic().getLink();
                    tv_antibiotic_link_viewer.setText((link != null ? link : "-"));
                    tv_antibiotic_s.setText("S: " + sq.getAntibiotic().getZoneDiameterBreakpoint().getS().getValue());
                    tv_antibiotic_r.setText("R: " + sq.getAntibiotic().getZoneDiameterBreakpoint().getR().getValue());

                    if(link != null){
                        cd_ntibiotic.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Antibiotic Family Link", link);
                                clipboard.setPrimaryClip(clip);
                                Util.mostrarToast(view.getContext(), "Link copied to clipboard");
                                return false;
                            }
                        });
                    }else{
                        tv_antibiotic_link.setVisibility(View.GONE);
                        tv_antibiotic_link_viewer.setVisibility(View.GONE);
                    }


                    List<String> antibNotes = new ArrayList<>();
                    for(Note n: bd.getAntibioticNotes(sq)){
                        antibNotes.add(n.getId() + ", " + n.getValue());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            this, R.layout.spiner_notes_item, antibNotes);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_notes.setAdapter(adapter);

                    if(!antibNotes.isEmpty()){
                        ly_antibiotic_notes.setVisibility(View.VISIBLE);
                        sp_notes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                tv_antibFam_notes_viewer.setText(sp_notes.getItemAtPosition(i).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else{
                        ly_antibiotic_notes.setVisibility(View.GONE);
                    }

                    //antibiotic family cd
                    tv_antibiotic_family_title.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
                    tv_antibiotic_family_name.setText(sq.getAntibioticFamily().getName());

                    List<String> antibNotes_family = new ArrayList<>();
                    for(Note n: bd.getAntibioticFamilyNotes(sq)){
                        antibNotes_family.add(n.getId() + ", " + n.getValue());
                    }
                    ArrayAdapter<String> adapter_family = new ArrayAdapter<String>(
                            this, R.layout.spiner_notes_item, antibNotes_family);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_family_notes.setAdapter(adapter_family);

                    if(!antibNotes_family.isEmpty()){
                        ly_antibiotic_family_notes.setVisibility(View.VISIBLE);
                        sp_family_notes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                tv_antibFam_family_notes_viewer.setText(sp_family_notes.getItemAtPosition(i).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }else{
                        ly_antibiotic_family_notes.setVisibility(View.GONE);
                    }
                    bd.cerrar();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            //overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return false;
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//        overridePendingTransition(R.anim.right_in, R.anim.right_out);
//    }
}
