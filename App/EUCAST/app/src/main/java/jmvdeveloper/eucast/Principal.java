package jmvdeveloper.eucast;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;
import jmvdeveloper.eucast.bd.BDHandler;
import jmvdeveloper.eucast.sincro.ConnectionHandler;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentQueries.OnFragmentInteractionListener,
        FragmentSearch.OnFragmentInteractionListener{

//    private RecyclerView recyclerView;
//    private SampleMaterialAdapter adapter;
//    private ArrayList<String> nombres;

    private static NavigationView nav_menu;
    private FloatingActionButton fab;

//    public MenuItem iGrid;
//    public MenuItem iOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(fab == null) {
            fab = (FloatingActionButton)findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav_menu = (NavigationView) findViewById(R.id.nav_view);
        nav_menu.setNavigationItemSelectedListener(this);

        //inicialziar preferencias
        Configuracion.inicializarPreferencias(this);
        //establecer las preferencias por defecto
        crearPreferenciasPorDefecto();

        BDHandler bd = new BDHandler(this);
        bd.abrir();
        //obtener los nombres de familias bacterianas y antibióticas
        if(!bd.thereIsFamilyNames())
            ConnectionHandler.getFamilies(this);
        bd.cerrar();
    }

    @Override
    public void onResume(){
        super.onResume();

        Util.vieneDeFragment = false;
        String anterior = Configuracion.getPreferenciaString("anterior");
        if(anterior != null){
            switch (anterior) {
                case "Queries":
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_query));
                    break;
                case "Search":
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_search));
                    break;
                default:
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_query));
                    break;
            }
        }else
            onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_query));
    }

    private void crearPreferenciasPorDefecto() {
        if(!Configuracion.isPreferencia("anterior"))
            Configuracion.setPreferencia("anterior", "Queries");
        if(!Configuracion.isPreferencia("url_analyzer"))
            Configuracion.setPreferencia("url_analyzer", "http://jmv-develop.no-ip.biz:8080/EUCAST/EUCAST_breakpoints/analyzer");
        if (!Configuracion.isPreferencia("order"))
            Configuracion.setPreferencia("order", "DESC");
        if(!Configuracion.isPreferencia("grid"))
            Configuracion.setPreferencia("grid", false);
        if (!Configuracion.isPreferencia("grid_columns"))
            Configuracion.setPreferencia("grid_columns", "2");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!Util.vieneDeFragment){
                Util.crearMensajeAlerta("Do you want to exit?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Configuracion.setPreferencia("anterior", "Queries");
                        finish();
                    }
                }, this);
            }else{
                super.onBackPressed();
                fab.setVisibility(View.VISIBLE);
                Util.vieneDeFragment = false;
                Configuracion.setPreferencia("anterior", "Queries");
                Util.iOrder.setVisible(true);
                Util.iGrid.setVisible(true);
            }
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            Util.crearMensajeAlerta("Do you want to exit?", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Configuracion.setPreferencia("anterior", "Queries");
//                    finish();
//                }
//            }, this);
//        }
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean("vieneDeFragment", Util.vieneDeFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Util.vieneDeFragment = state.getBoolean("vieneDeFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        Util.menuPrincipal = menu;
        getMenuInflater().inflate(R.menu.principal, menu);
        Util.iGrid = menu.findItem(R.id.action_change_view);
        Util.iOrder = menu.findItem(R.id.action_change_order);
        updateMenuState();
        return true;
    }

    private void updateMenuState() {
        if(Util.iGrid != null){
            if(Configuracion.getPreferenciaBoolean("grid")) {
                Util.iGrid.setIcon(R.drawable.ic_view_list_white_24dp);
            }else {
                Util.iGrid.setIcon(R.drawable.ic_view_module_white_24dp);
            }
            if(Util.iOrder != null){
                if(Configuracion.getPreferenciaString("anterior").equals("Queries")){
                    Util.iOrder.setVisible(true);
                    Util.iGrid.setVisible(true);
                }else{
                    Util.iOrder.setVisible(false);
                    Util.iGrid.setVisible(false);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_view) {
//            Util.gridSeted = !Util.gridSeted;
//            if(Util.gridSeted) {
//                Util.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//            }else {
//                Util.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            }
            Configuracion.setPreferencia("grid", !Configuracion.getPreferenciaBoolean("grid"));
            if(Configuracion.getPreferenciaBoolean("grid")) {
                item.setIcon(R.drawable.ic_view_list_white_24dp);
                Util.recyclerView.setLayoutManager(new GridLayoutManager(this, Integer.parseInt(Configuracion.getPreferenciaString("grid_columns"))));
            }else {
                item.setIcon(R.drawable.ic_view_module_white_24dp);
                Util.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            return true;
        }else if(id == R.id.action_change_order){
            Util.sensAdapter.reorderList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean fragTransact = false;
        Fragment fragmento = null;

        if (id == R.id.nav_query) {
            fragmento = new FragmentQueries();
            fragTransact = true;
            if(Util.iOrder != null && Util.iGrid != null) {
                Util.iOrder.setVisible(true);
                Util.iGrid.setVisible(true);
            }
        } else if (id == R.id.nav_search) {
            fragmento = new FragmentSearch();
            fragTransact = true;
            if(Util.iOrder != null && Util.iGrid != null) {
                Util.iOrder.setVisible(false);
                Util.iGrid.setVisible(false);
            }
        } else if (id == R.id.nav_config) {
            Intent i = new Intent(this, Options.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }

        if(fragTransact){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido_principal, fragmento).commit();
//            item.setChecked(true);
//            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
