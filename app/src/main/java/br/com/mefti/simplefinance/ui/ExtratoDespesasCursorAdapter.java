package br.com.mefti.simplefinance.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;

/**
 * Created by a_med on 9/11/2016.
 */

public class ExtratoDespesasCursorAdapter extends CursorAdapter{
    public BaseDadosSF dados;

    public ExtratoDespesasCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        dados = new BaseDadosSF(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_extrato_despesas, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView descricaoText = (TextView) view.findViewById(R.id.descricao_despesa);
        TextView valorText = (TextView) view.findViewById(R.id.valor_despesa);
        TextView categoriaText = (TextView) view.findViewById(R.id.categoria_despesa);
        TextView dataText = (TextView) view.findViewById(R.id.data_despesa);
        //final ImageView avatarImage = (ImageView) view.findViewById(R.id.iv_avatar);

        // Get valores.
        String descricao = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.DESCRICAO));
        String valor = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.VALOR));
        String categoria = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.COD_CATEGORIA));
        String data = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.DATA));



        //Obtendo nome categorio pelo cod
        String nCategoria = "";
        Cursor cursor1 = dados.ObterCategoriaPorCodCategoria(categoria);
        if (cursor1.moveToFirst()){
            nCategoria = cursor1.getString(3);
        }


        //Convertendo data
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        DateFormat targetFormat = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = null;
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(data);
            formattedDate = targetFormat.format(convertedDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //String avatarUri = cursor.getString(cursor.getColumnIndex(LawyerEntry.AVATAR_URI));

        // Setup.
        descricaoText.setText(descricao);
        valorText.setText("Valor: R$ " + valor);
        categoriaText.setText("Categoria: " + nCategoria);
        dataText.setText("Data: " + formattedDate);
        /*
        Glide
                .with(context)
                .load(Uri.parse("file:///android_asset/" + avatarUri))
                .asBitmap()
                .error(R.drawable.ic_account_circle)
                .centerCrop()
                .into(new BitmapImageViewTarget(avatarImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable
                                = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        drawable.setCircular(true);
                        avatarImage.setImageDrawable(drawable);
                    }
                });
                */
    }
}
