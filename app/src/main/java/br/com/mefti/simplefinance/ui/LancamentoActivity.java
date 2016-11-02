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
import br.com.mefti.simplefinance.modelo.Categorias;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class LancamentoActivity extends AppCompatActivity {

    BaseDadosSF dados = new BaseDadosSF(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.categoria_reg_lancamento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LancamentoActivity.this, CategoriaRegActivity.class);
                startActivity(register);
            }
        });

        //carregando spinner em funcao radio button e cod usuario
        final Spinner catLancamento;
        catLancamento = (Spinner)findViewById(R.id.categoria_lancamento);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.opcoes_lancamento1) ;
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
                        ArrayAdapter<String> adpR = new ArrayAdapter<String>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listR);
                        catLancamento.setAdapter(adpR);
                        //Toast.makeText(getBaseContext(), "Receita", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        ArrayList<String> listD = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"d");
                        ArrayAdapter<String> adpD = new ArrayAdapter<String>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listD);
                        catLancamento.setAdapter(adpD);
                        //Toast.makeText(getBaseContext(), "Despesa", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        });

        //carregando spinner e extraindo valor selecionado

        String cod_usuario = "";
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            cod_usuario = cursor.getString(1);
        }

        ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
        catLancamento.setAdapter(adapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lancamento_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String cod_usuario;
        //noinspection SimplifiableIfStatement
        if (id == R.id.lancamento_save) {
            //extraindo cod_usuario
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }

            //Consultando estado dos radiobuttons
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita1);
            boolean receita = radioButtonReceita.isChecked();
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa1);
            boolean despesa = radioButtonDespesa.isChecked();



            //extraindo descricao
            EditText descricaoLancamento = (EditText) findViewById(R.id.descricao_lancamento);
            String dLancamento = descricaoLancamento.getText().toString();

            //extraindo valor
            double vLancamento;
            EditText valorLancamento = (EditText) findViewById(R.id.valor_lancamento);
            if (valorLancamento == null){
                vLancamento = 0.0;
            }else {
                vLancamento = Double.parseDouble(valorLancamento.getText().toString());
            }

            //extraindo valor previsto
            double vPLancamento;
            EditText valorPrevistoLancamento = (EditText) findViewById(R.id.valor_previsto_lancamento);
            if (valorPrevistoLancamento == null){
                vPLancamento = 0.0;
            }else {
                vPLancamento = Double.parseDouble(valorPrevistoLancamento.getText().toString());
            }

            //extraindo observacao
            EditText observacaoLancamento = (EditText) findViewById(R.id.observacao_lancamento);
            String oLancamento = observacaoLancamento.getText().toString();


            /*
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

            */
            Intent extrato = new Intent(LancamentoActivity.this, ExtratoActivity.class);
            startActivity(extrato);
        }
        return super.onOptionsItemSelected(item);
    }


}
