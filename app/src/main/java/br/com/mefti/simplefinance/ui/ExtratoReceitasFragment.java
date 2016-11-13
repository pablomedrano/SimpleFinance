package br.com.mefti.simplefinance.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;


public class ExtratoReceitasFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_DESPESA = 2;
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

        // Instancia da base de dados
        dados = new BaseDadosSF(getActivity());

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

}
