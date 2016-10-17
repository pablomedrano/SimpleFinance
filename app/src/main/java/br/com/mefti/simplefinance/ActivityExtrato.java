package br.com.mefti.simplefinance;

import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import br.com.mefti.simplefinance.sqlite.OperacoesBaseDados;
import br.com.mefti.simplefinance.sqlite.Tabelas;
import br.com.mefti.simplefinance.modelo.Usuario;
import br.com.mefti.simplefinance.modelo.Categoria;
import br.com.mefti.simplefinance.modelo.Lancamento;

public class ActivityExtrato extends AppCompatActivity {
    OperacoesBaseDados dados;

    public class ProvaDados extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                dados.getDb().beginTransaction();
                String usuario1 = dados.inserirUsuario(new Usuario(null, "Andres", "abcd", "asd@asd.com", "1"));
                String usuario2 = dados.inserirUsuario(new Usuario(null, "Juan", "qwe", "qwe@qwwe.com", "0"));

                dados.getDb().setTransactionSuccessful();
            } finally {
                dados.getDb().endTransaction();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);

        getApplicationContext().deleteDatabase("BDSimpleFinance.db");
        dados = OperacoesBaseDados.obterInstancia(getApplicationContext());
        new ProvaDados().execute();
    }
}
