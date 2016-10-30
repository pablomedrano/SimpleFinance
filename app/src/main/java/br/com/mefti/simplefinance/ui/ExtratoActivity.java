package br.com.mefti.simplefinance.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;


public class ExtratoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    BaseDadosSF dados = new BaseDadosSF(this);
    TextView navBartexto1;
    TextView navBartexto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Modificando texview cabecera nav bar
        String s1 = "";
        String s2 = "";
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            s1 = cursor.getString(2);
            s2 = cursor.getString(4);
        }
        View hView = navigationView.getHeaderView(0);
        navBartexto1=(TextView)hView.findViewById(R.id.navBartexto1);
        navBartexto2=(TextView)hView.findViewById(R.id.navBartexto2);
        navBartexto1.setText(s1);
        navBartexto2.setText(s2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Sai da aplicacao quando e pulsado o botao voltar
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.extrato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_RecDes) {
            Intent register = new Intent(ExtratoActivity.this, RecDesActivity.class);
            startActivity(register);
        } else if (id == R.id.nav_Categoria) {
            Intent register = new Intent(ExtratoActivity.this, CategoriaActivity.class);
            startActivity(register);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_Desconectar) {
            Cursor cursor = dados.ObterUsuarioConectado();
            if (cursor.moveToFirst()){
                //Toast.makeText(getApplicationContext(), cursor.getString(1), Toast.LENGTH_LONG).show();
                Cursor cursor1 = dados.DesconectarUsuarioPorCod(cursor.getString(1));
                Intent i = new Intent(ExtratoActivity.this, LoginActivity.class);
                startActivity(i);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
