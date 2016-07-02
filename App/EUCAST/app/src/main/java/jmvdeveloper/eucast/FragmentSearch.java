package jmvdeveloper.eucast;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.bd.BDHandler;
import jmvdeveloper.eucast.sincro.ConnectionHandler;

/**
 * Created by Juanmi on 16/06/2016.
 */
public class FragmentSearch extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;

    public FragmentSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //aquí va el contenido
        if(fab == null) {
            fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
        }


        Configuracion.setPreferencia("anterior", "Search");

        NavigationView nav_menu = (NavigationView) getActivity().findViewById(R.id.nav_view);
        nav_menu.getMenu().getItem(1).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");

        TextView tv_respuesa = (TextView)view.findViewById(R.id.tv_respuesta);
        Button btn_conectar = (Button)view.findViewById(R.id.btn_conectar);
        AutoCompleteTextView et_family = (AutoCompleteTextView)view.findViewById(R.id.et_family);
        AutoCompleteTextView et_antimicrobial = (AutoCompleteTextView)view.findViewById(R.id.et_antimicrobial);
        EditText et_diameter = (EditText)view.findViewById(R.id.et_diameter);

        et_family.setThreshold(3);
        et_antimicrobial.setThreshold(3);

        BDHandler bd = new BDHandler(getContext());
        List<String> families = bd.getFamilyNames();
        List<String> antibiotics = bd.getAntibioticNames();
        ArrayAdapter<String> adptFamilies = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, families);
        et_family.setAdapter(adptFamilies);
        ArrayAdapter<String> adptAntibiotics = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, antibiotics);
        et_antimicrobial.setAdapter(adptAntibiotics);

        btn_conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_respuesa.setText("...");
                String family = et_family.getText().toString().trim();
                String anbitiotic = et_antimicrobial.getText().toString().trim();
                String diameter = et_diameter.getText().toString().trim();
                if(!bd.isSensibilityQuery(family, anbitiotic, diameter))
                    ConnectionHandler.checkSensitivity(getContext(),tv_respuesa,family,anbitiotic, diameter);
//                ConnectionHandler.getFamilyNames(getContext());
                else {
                    Util.mostrarToast(getContext(), "The query already exists");
                    tv_respuesa.setText("Response");
                }
            }
        });

        bd.cerrar();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}