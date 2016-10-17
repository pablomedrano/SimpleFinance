package br.com.mefti.simplefinance.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.mefti.simplefinance.modelo.*;
import br.com.mefti.simplefinance.sqlite.Tabelas;

/**
 * Created by a_med on 14/10/2016.
 */

public final class OperacoesBaseDados {
    private static BaseDadosSF baseDadosSF;
    private static OperacoesBaseDados instancia = new OperacoesBaseDados();
    private OperacoesBaseDados(){

    }

    public static OperacoesBaseDados obterInstancia (Context context){
        if(baseDadosSF == null){
            baseDadosSF = new BaseDadosSF(context);
        }
        return instancia;
    }

    //Operacoes Usuario
    public Cursor obterUsuario(){
        SQLiteDatabase db = baseDadosSF.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", BaseDadosSF.Tabelas.USUARIO);
        return db.rawQuery(sql, null);
    }

    public String inserirUsuario(Usuario usuario){
        SQLiteDatabase db = baseDadosSF.getWritableDatabase();

        String cod_usuario = Tabelas.Usuario.gerarCodigoUsuario();
        ContentValues valores = new ContentValues();
        valores.put(Tabelas.Usuario.COD_USUARIO, cod_usuario);
        valores.put(Tabelas.Usuario.NOME, usuario.nome);
        valores.put(Tabelas.Usuario.SENHA, usuario.senha);
        valores.put(Tabelas.Usuario.EMAIL, usuario.email);
        valores.put(Tabelas.Usuario.ESTADO, usuario.estado);

        return db.insertOrThrow(BaseDadosSF.Tabelas.USUARIO, null, valores) > 0 ? cod_usuario : null;
    }

    public SQLiteDatabase getDb(){
        return baseDadosSF.getWritableDatabase();
    }
}
