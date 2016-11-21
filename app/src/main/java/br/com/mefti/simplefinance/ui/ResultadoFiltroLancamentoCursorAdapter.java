package br.com.mefti.simplefinance.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;

/**
 * Created by a_med on 19/11/2016.
 */

public class ResultadoFiltroLancamentoCursorAdapter  extends CursorAdapter {
    public BaseDadosSF dados;

    public ResultadoFiltroLancamentoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        dados = new BaseDadosSF(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_resultado_filtro_lancamento, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView descricaoText = (TextView) view.findViewById(R.id.descricao_rfl);
        TextView valorText = (TextView) view.findViewById(R.id.valor_rfl);
        TextView categoriaText = (TextView) view.findViewById(R.id.categoria_rfl);
        TextView dataText = (TextView) view.findViewById(R.id.data_rfl);

        // Get valores.
        String descricao = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.DESCRICAO));
        String valor = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.VALOR));
        String categoria = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.COD_CATEGORIA));
        long data = cursor.getLong(cursor.getColumnIndex(ContratoSF.Lancamento.DATA));



        //Obtendo nome categorio pelo cod
        String nCategoria = "";
        Cursor cursor1 = dados.ObterCategoriaPorCodCategoria(categoria);
        if (cursor1.moveToFirst()){
            nCategoria = cursor1.getString(3);
        }


        //Convertendo data
        Date d = new Date (data);
        DateFormat targetFormat = new SimpleDateFormat("dd/MMM/y");
        String formattedDate = null;
        try {
            formattedDate = targetFormat.format(d);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //String avatarUri = cursor.getString(cursor.getColumnIndex(LawyerEntry.AVATAR_URI));

        // Setup.
        descricaoText.setText(descricao);
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedValor = format.format(Double.parseDouble(valor));
        valorText.setText("Valor: R$ " + formattedValor);
        categoriaText.setText("Categoria: " + nCategoria);
        dataText.setText("Data: " + formattedDate);

    }
}
