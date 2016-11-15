package br.com.mefti.simplefinance.ui;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class LancamentoEditarRemoverActivity extends AppCompatActivity {
    String cod_lancamento;
    Cursor cursor;
    BaseDadosSF dados = new BaseDadosSF(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento_editar_remover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.categoria_reg_lancamento_edit_delete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LancamentoEditarRemoverActivity.this, CategoriaRegActivity.class);
                startActivity(register);
            }
        });

        cod_lancamento = getIntent().getExtras().getString("cod_lancamento");
        //Toast.makeText(LancamentoEditarRemoverActivity.this, cod_lancamento, Toast.LENGTH_SHORT).show();
        cursor = dados.ObterLancamentoPorCodLancamento(cod_lancamento);
        final Spinner catLancamento;
        catLancamento = (Spinner)findViewById(R.id.categoria_lancamento_edit_delete);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.opcoes_lancamento_edit_delete1) ;
        if(cursor.moveToFirst()){
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita_edit_delete1);
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa_edit_delete1);
            if (cursor.getString(4).equals("r")){
                radioButtonReceita.setChecked(true);

                String cod_usuario = "";
                Cursor cursor1 = dados.ObterUsuarioConectado();
                if(cursor1.moveToFirst()){
                    cod_usuario = cursor1.getString(1);
                }
                ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
                catLancamento.setAdapter(adapter);

                String compareValue = dados.ObterNomeCategoriaPorCodCategoria(cursor.getString(3));
                if (!compareValue.equals(null)) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    catLancamento.setSelection(spinnerPosition);
                }

            }else if (cursor.getString(4).equals("d")){
                radioButtonDespesa.setChecked(true);

                String cod_usuario = "";
                Cursor cursor1 = dados.ObterUsuarioConectado();
                if(cursor1.moveToFirst()){
                    cod_usuario = cursor1.getString(1);
                }
                ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"d");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
                catLancamento.setAdapter(adapter);

                String compareValue = dados.ObterNomeCategoriaPorCodCategoria(cursor.getString(3));
                if (!compareValue.equals(null)) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    catLancamento.setSelection(spinnerPosition);
                }
            }

            //carregando spinner em funcao radio button e cod usuario quando e pulsado
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    String cod_usuario = "";

                    int posicao_lancamento_rb;

                    Cursor cursor = dados.ObterUsuarioConectado();
                    if(cursor.moveToFirst()){
                        cod_usuario = cursor.getString(1);
                    }

                    posicao_lancamento_rb = radioGroup.indexOfChild(findViewById(i));
                    switch (posicao_lancamento_rb){
                        case 0:
                            ArrayList<String> listR = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
                            ArrayAdapter<String> adpR = new ArrayAdapter<>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listR);
                            catLancamento.setAdapter(adpR);
                            break;
                        case 1:
                            ArrayList<String> listD = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"d");
                            ArrayAdapter<String> adpD = new ArrayAdapter<>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listD);
                            catLancamento.setAdapter(adpD);
                            break;
                    }
                }

            });

            EditText nomeCategoria = (EditText) findViewById(R.id.descricao_lancamento_edit_delete);
            nomeCategoria.setText(cursor.getString(5));
            nomeCategoria.setSelection(nomeCategoria.getText().length());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lancamento_edit_remove_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.lan_reg_save_update) {

        }
        if (id == R.id.lan_reg_delete) {

        }
        return super.onOptionsItemSelected(item);
    }

}
