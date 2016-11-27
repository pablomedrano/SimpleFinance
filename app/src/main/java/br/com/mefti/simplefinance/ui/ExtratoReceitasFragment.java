package br.com.mefti.simplefinance.ui;


import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;


public class ExtratoReceitasFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_RECEITA = 2;
    private BaseDadosSF dados;

    private ListView mReceitaList;
    private ExtratoReceitasCursorAdapter mReceitaAdapter;

    public ExtratoReceitasFragment() {
        // Required empty public constructor
    }

    public static ExtratoReceitasFragment newInstance() {
        return new ExtratoReceitasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_extrato_receitas, container, false);

        // Referencias UI
        mReceitaList = (ListView) root.findViewById(R.id.extrato_receitas_list);
        mReceitaAdapter = new ExtratoReceitasCursorAdapter(getActivity(), null);
        //mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mReceitaList.setAdapter(mReceitaAdapter);

        //Eventos
        mReceitaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mReceitaAdapter.getItem(i);
                Log.d("Lancamento", "Lancamento");
                DatabaseUtils.dumpCursor(currentItem);
                String currentLancamentoId = currentItem.getString(currentItem.getColumnIndex(ContratoSF.Lancamento.COD_LANCAMENTO));
                showDetailScreen(currentLancamentoId);
            }
        });

        // Instancia da base de dados
        dados = new BaseDadosSF(getActivity());

        //Total receitas
        String c_usuario = "";
        double sumReceitas = 0.0;
        Cursor usuario = dados.ObterUsuarioConectado();
        if(usuario.moveToFirst()){
            c_usuario = usuario.getString(1);
        }
        Cursor cursor3 = dados.ObterTodasAsReceitasPorUsuario(c_usuario);
        while (cursor3.moveToNext()){
            sumReceitas += cursor3.getDouble(6);
        }
        TextView totalReceitas = (TextView) root.findViewById(R.id.monto_total_receitas);
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedValor = format.format(sumReceitas);
        totalReceitas.setText("R$ " + formattedValor);

        // Carga de datos
        loadReceitas();


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void loadReceitas(){
        new ExtratoReceitasLoadTask().execute();
    }

    private class ExtratoReceitasLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            String cod_usuario = "";
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }
            return dados.ObterTodasAsReceitasPorUsuario(cod_usuario);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mReceitaAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }

    private void showDetailScreen(String cod_lancamento) {
        Intent intent = new Intent(getActivity(), LancamentoEditarRemoverActivity.class);
        intent.putExtra("cod_lancamento", cod_lancamento);
        startActivityForResult(intent,REQUEST_UPDATE_DELETE_RECEITA);
    }

}
