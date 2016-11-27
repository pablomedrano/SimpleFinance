package br.com.mefti.simplefinance.ui;


import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;

public class ExtratoDespesasFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_DESPESA = 2;
    private BaseDadosSF dados;

    private ListView mDespesaList;
    private ExtratoDespesasCursorAdapter mDespesaAdapter;

    public ExtratoDespesasFragment() {
        // Required empty public constructor
    }

    public static ExtratoDespesasFragment newInstance(){
        return new ExtratoDespesasFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_extrato_despesas, container, false);

        // Referencias UI
        mDespesaList = (ListView) root.findViewById(R.id.extrato_despesas_list);
        mDespesaAdapter = new ExtratoDespesasCursorAdapter(getActivity(), null);
        //mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mDespesaList.setAdapter(mDespesaAdapter);

        //Eventos
        mDespesaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mDespesaAdapter.getItem(i);
                Log.d("Lancamento", "Lancamento");
                DatabaseUtils.dumpCursor(currentItem);
                String currentLancamentoId = currentItem.getString(currentItem.getColumnIndex(ContratoSF.Lancamento.COD_LANCAMENTO));
                showDetailScreen(currentLancamentoId);
            }
        });

        // Instancia da base de dados
        dados = new BaseDadosSF(getActivity());

        //Total despesas
        String c_usuario = "";
        double sumDespesas = 0.0;
        Cursor usuario = dados.ObterUsuarioConectado();
        if(usuario.moveToFirst()){
            c_usuario = usuario.getString(1);
        }
        Cursor cursor3 = dados.ObterTodasAsDespesasPorUsuario(c_usuario);
        while (cursor3.moveToNext()){
            sumDespesas += cursor3.getDouble(6);
        }
        TextView totalDespesas = (TextView) root.findViewById(R.id.monto_total_despesas);
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedValor = format.format(sumDespesas);
        totalDespesas.setText("R$ " + formattedValor);

        // Carga de datos
        loadDespesas();




        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void loadDespesas(){
        new ExtratoDespesasLoadTask().execute();
    }

    private class ExtratoDespesasLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            String cod_usuario = "";
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }
            return dados.ObterTodasAsDespesasPorUsuario(cod_usuario);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mDespesaAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }

    private void showDetailScreen(String cod_lancamento) {
        Intent intent = new Intent(getActivity(), LancamentoEditarRemoverActivity.class);
        intent.putExtra("cod_lancamento", cod_lancamento);
        startActivityForResult(intent,REQUEST_UPDATE_DELETE_DESPESA);
    }

}
