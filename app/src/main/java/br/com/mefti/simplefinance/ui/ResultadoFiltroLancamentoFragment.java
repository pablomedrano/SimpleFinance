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
import android.widget.Toast;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;


public class ResultadoFiltroLancamentoFragment extends Fragment {

    public static final int REQUEST_UPDATE_DELETE_DESPESA = 2;
    private BaseDadosSF dados;

    private ListView mRFLList;
    private ResultadoFiltroLancamentoCursorAdapter mRFLAdapter;

    public ResultadoFiltroLancamentoFragment() {
        // Required empty public constructor
    }

    public static ResultadoFiltroLancamentoFragment newInstance(){
        return new ResultadoFiltroLancamentoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_resultado_filtro_lancamento, container, false);

        // Referencias UI
        mRFLList = (ListView) root.findViewById(R.id.resultado_filtro_list);
        mRFLAdapter = new ResultadoFiltroLancamentoCursorAdapter(getActivity(), null);
        //mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mRFLList.setAdapter(mRFLAdapter);

        //Eventos
        mRFLList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mRFLAdapter.getItem(i);
                Log.d("Lancamento", "Lancamento");
                DatabaseUtils.dumpCursor(currentItem);
                String currentLancamentoId = currentItem.getString(currentItem.getColumnIndex(ContratoSF.Lancamento.COD_LANCAMENTO));
                showDetailScreen(currentLancamentoId);
            }
        });

        // Instancia da base de dados
        dados = new BaseDadosSF(getActivity());

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
            Cursor filtroLancamentos = null;
            ResultadoFiltroLancamentoActivity activity = (ResultadoFiltroLancamentoActivity) getActivity();
            String cod_categoria = activity.getCodCategoriaRFL();
            String descricao = activity.getDescricaoRFL();
            long data1 = activity.getDataRFL1();
            long data2 = activity.getDataRFL2();

            if (descricao.equals("")){
                filtroLancamentos = dados.ObterLancamentosPeloFiltroSemDescricao(cod_categoria, data1, data2);
            }else{
                filtroLancamentos = dados.ObterLancamentosPeloFiltroComDescricao(cod_categoria, descricao, data1, data2);
            }

            return filtroLancamentos;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mRFLAdapter.swapCursor(cursor);
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
