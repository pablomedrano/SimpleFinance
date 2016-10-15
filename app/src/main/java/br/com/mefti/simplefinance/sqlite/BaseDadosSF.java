package br.com.mefti.simplefinance.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;


import br.com.mefti.simplefinance.sqlite.Tabelas.Usuario;
import br.com.mefti.simplefinance.sqlite.Tabelas.Lancamento;
import br.com.mefti.simplefinance.sqlite.Tabelas.Categoria;


/**
 * Created by a_med on 13/10/2016.
 * clase que administra a conexao da base de dados e a estrutura
 */

public class BaseDadosSF extends SQLiteOpenHelper {
    private static final String NOME_BASE_DADOS = "BDSimpleFinance.db";
    private static final int VERSAO_ATUAL = 1;
    private final Context contexto;

    interface Tabelas{
        String USUARIO = "usuario";
        String LANCAMENTO = "lancamento";
        String CATEGORIA = "categoria";
    }

    interface Referencias{
        String COD_USUARIO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",Tabelas.USUARIO, Usuario.COD_USUARIO);
        String COD_LANCAMENTO = String.format("REFERENCES %s(%s)",Tabelas.LANCAMENTO, Lancamento.COD_LANCAMENTO);
        String COD_CATEGORIA = String.format("REFERENCES %s(%s)",Tabelas.CATEGORIA, Categoria.COD_CATEGORIA);
    }

    public BaseDadosSF(Context contexto){
        super(contexto, NOME_BASE_DADOS, null, VERSAO_ATUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                db.setForeignKeyConstraintsEnabled(true);
            }else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s )",
                Tabelas.USUARIO, BaseColumns._ID,
                Usuario.COD_USUARIO, Usuario.NOME, Usuario.SENHA, Usuario.EMAIL
        ));


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tabelas.USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tabelas.LANCAMENTO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tabelas.CATEGORIA);

        onCreate(sqLiteDatabase);
    }
}
