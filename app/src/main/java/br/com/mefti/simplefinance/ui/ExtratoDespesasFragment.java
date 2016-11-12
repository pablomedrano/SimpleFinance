package br.com.mefti.simplefinance.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class ExtratoDespesasFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_DESPESA = 2;
    private BaseDadosSF dados;

    private ListView mDespesaList;
    private ExtratoDespesasCursorAdapter mDespesaAdapter;
    private FloatingActionButton mAddButton;

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

}
