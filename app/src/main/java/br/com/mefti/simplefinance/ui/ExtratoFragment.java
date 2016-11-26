package br.com.mefti.simplefinance.ui;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;


public class ExtratoFragment extends Fragment {

    private BaseDadosSF dados;
    String cod_usuario;

    long dataAtual, dataBD;
    String dataF, dataF1, dataF2, dataF3, dataF4, dataBDF;

    public ExtratoFragment() {
        // Required empty public constructor
    }


    public static ExtratoFragment newInstance() {
        return new ExtratoFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_extrato, container, false);
        //Carregando data atual e sutraindo 1 dia de vez durante 5 dias para organizar lancamentos
        dataAtual = System.currentTimeMillis();
        dataF = FormatData(dataAtual);
        dataF1 = FormatData(dataAtual - 86400000);
        dataF2 = FormatData(dataAtual - 172800000);
        dataF3 = FormatData(dataAtual - 259200000);
        dataF4 = FormatData(dataAtual - 345600000);

        //Toast.makeText(getActivity(), dataF1, Toast.LENGTH_SHORT).show();




        // Instancia da base de dados
        dados = new BaseDadosSF(getActivity());
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            cod_usuario = cursor.getString(1);
        }
        Cursor lancamentos  = dados.ObterLancamentosPorCodUsuario(cod_usuario);

        TableLayout tl = (TableLayout) root.findViewById(R.id.tabla1);
        int cont = 0;
        int cont1 = 0;
        int cont2 = 0;
        int cont3 = 0;
        int cont4 = 0;
        double sumDespesas = 0.0;
        double sumReceitas = 0.0;

        try {
            while (lancamentos.moveToNext()){
                dataBD = lancamentos.getLong(lancamentos.getColumnIndex(ContratoSF.Lancamento.DATA));
                dataBDF = FormatData(dataBD);

                if(dataF.equals(dataBDF) && cont == 0){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    TextView data = new TextView(getActivity());
                    data.setText(dataBDF);
                    tr.addView(data);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    cont++;
                }
                if(dataF.equals(dataBDF)){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TextView detailstv = new TextView(getActivity());
                    detailstv.setText(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.DESCRICAO)));
                    tr.addView(detailstv);
                    TextView valstv = new TextView(getActivity());
                    if(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO)).equals("d")){
                        valstv.setText(" R$ - " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        valstv.setText(" R$ " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#808000"));
                    }
                    tr.addView(valstv);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }

                if(dataF1.equals(dataBDF) && cont1 == 0){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    TextView data = new TextView(getActivity());
                    data.setText(dataBDF);
                    tr.addView(data);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    cont1++;
                }
                if(dataF1.equals(dataBDF)){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TextView detailstv = new TextView(getActivity());
                    detailstv.setText(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.DESCRICAO)));
                    tr.addView(detailstv);

                    TextView valstv = new TextView(getActivity());
                    if(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO)).equals("d")){
                        valstv.setText(" R$ - " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        valstv.setText(" R$ " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#808000"));
                    }
                    tr.addView(valstv);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }

                if(dataF2.equals(dataBDF) && cont2 == 0){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    TextView data = new TextView(getActivity());
                    data.setText(dataBDF);
                    tr.addView(data);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    cont2++;
                }
                if(dataF2.equals(dataBDF)){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TextView detailstv = new TextView(getActivity());
                    detailstv.setText(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.DESCRICAO)));
                    tr.addView(detailstv);

                    TextView valstv = new TextView(getActivity());
                    if(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO)).equals("d")){
                        valstv.setText(" R$ - " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        valstv.setText(" R$ " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#808000"));
                    }
                    tr.addView(valstv);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }

                if(dataF3.equals(dataBDF) && cont3 == 0){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    TextView data = new TextView(getActivity());
                    data.setText(dataBDF);
                    tr.addView(data);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    cont3++;
                }
                if(dataF3.equals(dataBDF)){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TextView detailstv = new TextView(getActivity());
                    detailstv.setText(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.DESCRICAO)));
                    tr.addView(detailstv);

                    TextView valstv = new TextView(getActivity());
                    if(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO)).equals("d")){
                        valstv.setText(" R$ - " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        valstv.setText(" R$ " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#808000"));
                    }
                    tr.addView(valstv);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }

                if(dataF4.equals(dataBDF) && cont4 == 0){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    TextView data = new TextView(getActivity());
                    data.setText(dataBDF);
                    tr.addView(data);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    cont4++;
                }
                if(dataF4.equals(dataBDF)){
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TextView detailstv = new TextView(getActivity());
                    detailstv.setText(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.DESCRICAO)));
                    tr.addView(detailstv);

                    TextView valstv = new TextView(getActivity());
                    if(lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO)).equals("d")){
                        valstv.setText(" R$ - " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        valstv.setText(" R$ " + lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR)));
                        valstv.setTextColor(Color.parseColor("#808000"));
                    }
                    tr.addView(valstv);
                    tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }

                String tipoLancamento = lancamentos.getString(lancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO));
                if (tipoLancamento.equals("r")){
                    sumReceitas += lancamentos.getDouble(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR));
                }
                if (tipoLancamento.equals("d")){
                    sumDespesas += lancamentos.getDouble(lancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR));
                }
            }
            String saldo = String.valueOf(sumReceitas - sumDespesas);
            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            TextView detailstv = new TextView(getActivity());
            detailstv.setText("Total");
            detailstv.setTypeface(null, Typeface.BOLD);
            detailstv.setTextSize(20);
            tr.addView(detailstv);

            TextView valstv = new TextView(getActivity());
            valstv.setText(" R$ " + saldo);
            //valstv.setTextColor(Color.parseColor("#0000CC"));
            valstv.setTypeface(null, Typeface.BOLD);
            valstv.setTextSize(20);
            tr.addView(valstv);
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }finally {
            lancamentos.close();
        }

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private String FormatData(long data){
        DateFormat targetFormat = new SimpleDateFormat("dd/MMM/y");
        String formattedDate = null;
        try {
            formattedDate = targetFormat.format(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formattedDate;
    }

}