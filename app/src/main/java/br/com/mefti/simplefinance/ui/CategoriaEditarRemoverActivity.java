package br.com.mefti.simplefinance.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class CategoriaEditarRemoverActivity extends AppCompatActivity {
    String cod_categoria;
    BaseDadosSF dados = new BaseDadosSF(this);
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_editar_remover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cod_categoria = getIntent().getExtras().getString("cod_categoria");
        //Toast.makeText(CategoriaEditarRemoverActivity.this, cod_categoria, Toast.LENGTH_SHORT).show();
        cursor = dados.ObterCategoriaPorCodCategoria(cod_categoria);
        if (cursor.moveToFirst()){
            //Toast.makeText(CategoriaEditarRemoverActivity.this, cursor.getString(3), Toast.LENGTH_SHORT).show();
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita_edit_delete);
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa_edit_delete);
            if (cursor.getString(4) == "r"){
                radioButtonReceita.setChecked(true);
            }else if (cursor.getString(4) == "d"){
                radioButtonDespesa.setChecked(true);
            }

            EditText nomeCategoria = (EditText) findViewById(R.id.nome_categoria_edit_delete);
            nomeCategoria.setText(cursor.getString(3));
            nomeCategoria.setSelection(nomeCategoria.getText().length());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categoria_edit_remove_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.cat_reg_save_update) {
            //Consultando estado dos radiobuttons
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita_edit_delete);
            boolean receita = radioButtonReceita.isChecked();
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa_edit_delete);
            boolean despesa = radioButtonDespesa.isChecked();

            //extraindo nome da categoria
            EditText nomeCategoria = (EditText) findViewById(R.id.nome_categoria_edit_delete);
            String nCategoria = nomeCategoria.getText().toString();

            //atualizando categoria
            String tipo="";
            if (receita){
                tipo = "r";
            }else if(despesa){
                tipo = "d";
            }
            dados.UpdateCategoriaPorCodCategoria(cod_categoria, nCategoria, tipo);
            Intent extrato = new Intent(CategoriaEditarRemoverActivity.this, CategoriaActivity.class);
            startActivity(extrato);
        }
        if (id == R.id.cat_reg_delete) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("Os registros dos lancamentos assim como da categoria seram apagados, deseja confirmar?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    EliminarCategoriaELancamentos();
                    Intent extrato = new Intent(CategoriaEditarRemoverActivity.this, CategoriaActivity.class);
                    startActivity(extrato);
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Intent extrato = new Intent(CategoriaEditarRemoverActivity.this, CategoriaActivity.class);
                    startActivity(extrato);
                }
            });
            dialogo1.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void EliminarCategoriaELancamentos(){
        dados.EliminarLancamentoPorCodCategoria(cod_categoria);
        dados.EliminarCategoriaPorCodCategoria(cod_categoria);
    }

}
