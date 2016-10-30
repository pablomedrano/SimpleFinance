package br.com.mefti.simplefinance.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.modelo.Categorias;
import br.com.mefti.simplefinance.modelo.Usuarios;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class CategoriaRegActivity extends AppCompatActivity {

    BaseDadosSF dados = new BaseDadosSF(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_reg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categoria_reg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String cod_usuario="";
        //noinspection SimplifiableIfStatement
        if (id == R.id.cat_reg_save) {
            //Consultando estado dos radiobuttons
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita);
            boolean receita = radioButtonReceita.isChecked();
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa);
            boolean despesa = radioButtonDespesa.isChecked();

            //extraindo nome da categoria
            EditText nomeCategoria = (EditText) findViewById(R.id.nome_categoria);
            String nCategoria = nomeCategoria.getText().toString();

            //extraindo cod_usuario
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }

            //salvando nova categoria
            Categorias categorias = new Categorias();
            categorias.setCod_usuario(cod_usuario);
            categorias.setNome(nCategoria);
            if (receita){
                categorias.setTp_lancamento("r");
            }else if(despesa){
                categorias.setTp_lancamento("d");
            }
            dados.inserirCategoria(categorias);

            Intent extrato = new Intent(CategoriaRegActivity.this, ExtratoActivity.class);
            startActivity(extrato);
        }
        return super.onOptionsItemSelected(item);
    }

}
