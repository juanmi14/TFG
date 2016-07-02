package jmvdeveloper.eucast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import jmvdeveloper.eucast.Utils.Configuracion;
import jmvdeveloper.eucast.Utils.Util;

public class Options extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static NavigationView nav_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav_menu = (NavigationView) findViewById(R.id.nav_view);
        nav_menu.setNavigationItemSelectedListener(this);

        nav_menu.getMenu().getItem(2).setChecked(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new OptionsFragment())
                .commit();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            Util.crearMensajeAlerta("Do you want to exit?", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Configuracion.setPreferencia("anterior", "Queries");
//                    finish();
//                }
//            }, this);
            startActivity(new Intent(this, Principal.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id != R.id.nav_config) {
            Intent i = new Intent(this, Principal.class);

            if (id == R.id.nav_query) {
                Configuracion.setPreferencia("anterior", "Queries");
            } else if (id == R.id.nav_search) {
                Configuracion.setPreferencia("anterior", "Search");
            }
            //
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            startActivity(i);
            overridePendingTransition(0, 0);

            finish();
        }else{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
        return true;
    }


}
