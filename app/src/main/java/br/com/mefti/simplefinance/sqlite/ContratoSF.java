package br.com.mefti.simplefinance.sqlite;

import java.util.UUID;

/**
 * Created by a_med on 13/10/2016.
 * Clase que estabelece os nomes que vao ser usados na base de dados
 */

public class ContratoSF {

    interface ColunasUsuario{
        String COD_USUARIO = "cod_usuario";
        String NOME = "nome";
        String SENHA = "senha";
        String EMAIL = "email";
        String ESTADO = "estado";
    }

    interface ColunasLancamento{
        String COD_LANCAMENTO = "cod_lancamento";
        String COD_USUARIO = "cod_usuario";
        String COD_CATEGORIA = "cod_categoria";
        String TP_LANCAMENTO = "tp_lancamento";
        String DESCRICAO = "descricao";
        String VALOR = "valor";
        String DATA = "data";
        String REPETIR = "repetir";
        String PREVISAO_DATA = "previsao_data";
        String PREVISAO_VALOR = "previsao_valor";
        String OBSERVACAO = "observacao";
    }

    interface ColunasCategoria{
        String COD_CATEGORIA = "cod_categoria";
        String COD_USUARIO = "cod_usuario";
        String NOME = "nome";
        String TP_LANCAMENTO = "tp_lancamento";
    }

    public static class Usuario implements ColunasUsuario{
        public static String gerarCodigoUsuario(){
            return "CU-" + UUID.randomUUID().toString();
        }
    }

    public static class Lancamento implements ColunasLancamento{
        public static String gerarCodigoLancamento(){
            return "CL-" + UUID.randomUUID().toString();
        }
    }

    public static class Categoria implements ColunasCategoria{
        public static String gerarCodigoCategoria(){
            return "CC-" + UUID.randomUUID().toString();
        }
    }

    private ContratoSF(){

    }
}
