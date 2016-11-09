package br.com.mefti.simplefinance.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.mefti.simplefinance.R;

public class CategoriaActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORIA_ID = "extra_categoria_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(CategoriaActivity.this, CategoriaRegActivity.class);
                startActivity(register);
            }
        });

        CategoriaFragment fragment = (CategoriaFragment) getSupportFragmentManager().findFragmentById(R.id.categoria_container);
        if (fragment == null){
            fragment = CategoriaFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.categoria_container, fragment)
                    .commit();

        }
    }

    @Override
    public void onBackPressed() {
        Intent extrato = new Intent(CategoriaActivity.this, ExtratoActivity.class);
        startActivity(extrato);
    }

}