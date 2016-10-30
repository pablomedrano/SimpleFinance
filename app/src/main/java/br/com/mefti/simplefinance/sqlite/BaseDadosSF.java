package br.com.mefti.simplefinance.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import br.com.mefti.simplefinance.sqlite.ContratoSF.*;
import br.com.mefti.simplefinance.modelo.*;


/**
 * Created by a_med on 13/10/2016.
 * clase que administra a conexao da base de dados e a estrutura
 */

public class BaseDadosSF extends SQLiteOpenHelper {
    private static final String NOME_BASE_DADOS = "BDSimpleFinance.db";
    private static final int VERSAO_ATUAL = 1;
    private final Context contexto;
    SQLiteDatabase db;

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
                        "%s TEXT NOT NULL UNIQUE,%s VARCHAR(50) NOT NULL,%s VARCHAR(20) NOT NULL,%s VARCHAR(30) NOT NULL UNIQUE, %s CHAR(1) NOT NULL )",
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

    //Inicio Operacoes Usuario
    public void inserirUsuario (Usuarios usuarios){
        db = this.getWritableDatabase();
        String cod_usuario = Usuario.gerarCodigoUsuario();
        ContentValues values = new ContentValues();
        values.put(Usuario.COD_USUARIO, cod_usuario);
        values.put(Usuario.NOME, usuarios.getNome());
        values.put(Usuario.SENHA, usuarios.getSenha());
        values.put(Usuario.EMAIL, usuarios.getEmail());
        values.put(Usuario.ESTADO, usuarios.getEstado());
        db.insert(Tabelas.USUARIO, null, values);
        db.close();
    }

    public String ObterUsuarioPorEmail (String email){
        db = this.getReadableDatabase();
        String result = "not found";
        String sql = String.format("SELECT %s FROM %s WHERE %s=?",
                Usuario.NOME, Tabelas.USUARIO, Usuario.EMAIL);
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Log.d("Email", email);
        DatabaseUtils.dumpCursor(cursor);
        if (cursor.moveToFirst()){
            result=cursor.getString(0);
        }
        return result;
    }

    public Cursor logUsuario (String email){
        db = this.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tabelas.USUARIO, Usuario.EMAIL);
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        DatabaseUtils.dumpCursor(cursor);
        return cursor;

    }

    public Cursor ObterUsuarioConectado(){
        db = this.getReadableDatabase();
        String estado = "1";
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tabelas.USUARIO, Usuario.ESTADO);
        String[] selectionArgs = {estado};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    public Cursor DesconectarUsuarioPorCod(String cod_usuario){
        db = this.getWritableDatabase();
        String nEstado = "0";
        String sql = String.format("UPDATE %s SET %s = %s WHERE %s=?",
                Tabelas.USUARIO, Usuario.ESTADO, nEstado, Usuario.COD_USUARIO);
        String[] selectionArgs = {cod_usuario};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Log.d("Usuario", "Usuario");
        DatabaseUtils.dumpCursor(cursor);
        return cursor;
    }

    public Cursor ConectarUsuarioPorCod(String cod_usuario){
        db = this.getWritableDatabase();
        String nEstado = "1";
        String sql = String.format("UPDATE %s SET %s = %s WHERE %s=?",
                Tabelas.USUARIO, Usuario.ESTADO, nEstado, Usuario.COD_USUARIO);
        String[] selectionArgs = {cod_usuario};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Log.d("Usuario", "Usuario");
        DatabaseUtils.dumpCursor(cursor);
        return cursor;
    }
    //fin Operacoes Usuario

    //Inicio Operacoes Categoria
    public void inserirCategoria (Categorias categorias){
        db = this.getWritableDatabase();
        String cod_categoria = Categoria.gerarCodigoCategoria();
        ContentValues values = new ContentValues();
        values.put(Categoria.COD_CATEGORIA, cod_categoria);
        values.put(Categoria.COD_USUARIO, categorias.getCod_usuario());
        values.put(Categoria.NOME, categorias.getNome());
        values.put(Categoria.TP_LANCAMENTO, categorias.getTp_lancamento());
        db.insert(Tabelas.CATEGORIA, null, values);
        db.close();
    }
    //Fin Operacoes Categoria


}
