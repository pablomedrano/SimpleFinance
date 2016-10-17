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
                        "%s TEXT NOT NULL UNIQUE,%s VARCHAR(50) NOT NULL,%s VARCHAR(20) NOT NULL,%s VARCHAR(30) NOT NULL, %s CHAR(1) NOT NULL )",
                Tabelas.USUARIO, BaseColumns._ID,
                Usuario.COD_USUARIO, Usuario.NOME, Usuario.SENHA, Usuario.EMAIL, Usuario.ESTADO
        ));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL %s, %s TEXT NOT NULL %s, %s CHAR(1) NOT NULL, " +
                "%s VARCHAR(100) NOT NULL, %s DOUBLE NOT NULL, %s DATETIME NOT NULL, %s CHAR(1) NOT NULL, %s DATETIME, " +
                "%s DOUBLE, %s VARCHAR(400))",
                Tabelas.LANCAMENTO, BaseColumns._ID,
                Lancamento.COD_LANCAMENTO, Lancamento.COD_USUARIO, Referencias.COD_USUARIO, Lancamento.COD_CATEGORIA, Referencias.COD_CATEGORIA, Lancamento.TP_LANCAMENTO,
                Lancamento.DESCRICAO, Lancamento.VALOR, Lancamento.DATA, Lancamento.REPETIR, Lancamento.PREVISAO_DATA,
                Lancamento.PREVISAO_VALOR, Lancamento.OBSERVACAO
        ));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL %s, %s VARCHAR(20) NOT NULL, %s CHAR(1) NOT NULL)",
                Tabelas.CATEGORIA, BaseColumns._ID,
                Categoria.COD_CATEGORIA, Categoria.COD_USUARIO, Referencias.COD_USUARIO, Categoria.NOME, Categoria.TP_LANCAMENTO
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
