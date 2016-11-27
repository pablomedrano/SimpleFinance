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
 * Created by a_med on 12/11/2016.
 */

public class ExtratoReceitasCursorAdapter extends CursorAdapter {
    public BaseDadosSF dados;

    public ExtratoReceitasCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        dados = new BaseDadosSF(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_extrato_receitas, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView descricaoText = (TextView) view.findViewById(R.id.descricao_receita);
        TextView valorText = (TextView) view.findViewById(R.id.valor_receita);
        TextView categoriaText = (TextView) view.findViewById(R.id.categoria_receita);
        TextView dataText = (TextView) view.findViewById(R.id.data_receita);
        //final ImageView avatarImage = (ImageView) view.findViewById(R.id.iv_avatar);

        // Get valores.
        String descricao = cursor.getString(cursor.getColumnIndex(ContratoSF.Lancamento.DESCRICAO));
        Double valor = cursor.getDouble(cursor.getColumnIndex(ContratoSF.Lancamento.VALOR));
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
        String formattedValor = format.format(valor);
        valorText.setText("Valor: R$ " + formattedValor);
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
