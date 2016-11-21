package br.com.mefti.simplefinance.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import br.com.mefti.simplefinance.R;

public class ResultadoFiltroLancamentoActivity extends AppCompatActivity {
    boolean receita, despesa;
    String cod_categoria, descricao;
    long data1, data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_filtro_lancamento);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            receita = (boolean)extras.get("receita");
            despesa = (boolean)extras.get("despesa");
            cod_categoria = (String)extras.get("cod_categoria");
            descricao = (String)extras.get("descricao");
            data1 = (long)extras.get("data1");
            data2 = (long)extras.get("data2");
        }

        ResultadoFiltroLancamentoFragment fragment = (ResultadoFiltroLancamentoFragment) getSupportFragmentManager().findFragmentById(R.id.resultado_filtro_container);
        if (fragment == null){
            fragment = ResultadoFiltroLancamentoFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.resultado_filtro_container, fragment)
                    .commit();

        }
    }

    @Override
    public void onBackPressed() {
        Intent extrato = new Intent(ResultadoFiltroLancamentoActivity.this, ExtratoActivity.class);
        startActivity(extrato);
    }


    public String getCodCategoriaRFL(){
        return cod_categoria;
    }
    public String getDescricaoRFL(){
        return descricao;
    }
    public long getDataRFL1(){
        return data1;
    }
    public long getDataRFL2(){
        return data2;
    }


}
