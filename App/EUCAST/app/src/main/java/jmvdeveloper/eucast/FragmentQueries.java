package jmvdeveloper.eucast;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.adapters.SensibilityQueryAdapter;
import jmvdeveloper.eucast.bd.BDHandler;
import jmvdeveloper.eucast.logic.SensitivityQuery;

/**
 * Created by Juanmi on 16/06/2016.
 */
public class FragmentQueries extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;
//    private SensibilityQueryAdapter sensAdapter;
//    private RecyclerView rv;

    public FragmentQueries() {
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
        return inflater.inflate(R.layout.fragment_queries, container, false);
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
        NavigationView nav_menu = (NavigationView) getActivity().findViewById(R.id.nav_view);
        nav_menu.getMenu().getItem(0).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Queries");
//        setRetainInstance(true);//esto mantiene el scroll cuando paso de fragments

        if(fab == null) {
            fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contenido_principal, new FragmentSearch());
                    ft.addToBackStack(null);//para poder volver atrás al pulsar el botón de back
                    Util.vieneDeFragment = true;
                    if(Util.iOrder != null )
                        Util.iOrder.setVisible(false);
                    if(Util.iGrid != null )
                        Util.iGrid.setVisible(false);
                    ft.commit();
                }
            });
            fab.setVisibility(View.VISIBLE);
        }

        Configuracion.setPreferencia("anterior", "Queries");

        Util.vieneDeFragment = false;

        Util.recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        BDHandler bd = new BDHandler(getContext());
        if(Util.sensAdapter == null) {
            List<SensitivityQuery> sqs = bd.getSensibilityQueries();
//            if(Util.reverted)
            if(Configuracion.getPreferenciaString("order").equals("DESC"))
                Collections.reverse(sqs);
            Util.sensAdapter = new SensibilityQueryAdapter(getContext(), sqs);
        }
        bd.cerrar();
        Util.recyclerView.setAdapter(Util.sensAdapter);
        if(Configuracion.getPreferenciaBoolean("grid"))
            Util.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), Integer.parseInt(Configuracion.getPreferenciaString("grid_columns"))));
        else
            Util.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        Util.recyclerView.setHasFixedSize(true);
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        BDHandler bd = new BDHandler(getContext());
//        if(sensAdapter == null)
//            sensAdapter = new SensibilityQueryAdapter(getContext(), bd.getSensibilityQueries());
//        bd.cerrar();
//        rv.setAdapter(sensAdapter);
//        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        rv.setHasFixedSize(true);
//    }

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
