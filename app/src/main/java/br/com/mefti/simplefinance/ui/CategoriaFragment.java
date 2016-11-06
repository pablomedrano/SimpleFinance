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
import android.widget.Toast;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.modelo.Lancamentos;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;


public class CategoriaFragment extends Fragment {
    private BaseDadosSF dados;

    private ListView mCategoriaList;
    private CategoriaCursorAdapter mCategoriaAdapter;
    private FloatingActionButton mAddButton;


    public CategoriaFragment() {
        // Required empty public constructor
    }

    public static CategoriaFragment newInstance(){
        return new CategoriaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categoria, container, false);

        // Referencias UI
        mCategoriaList = (ListView) root.findViewById(R.id.categoria_list);
        mCategoriaAdapter = new CategoriaCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mCategoriaList.setAdapter(mCategoriaAdapter);

        // Eventos
        mCategoriaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mCategoriaAdapter.getItem(i);
                Log.d("Cursor", "Cursor");
                DatabaseUtils.dumpCursor(currentItem);
                String currentCategoriaId = currentItem.getString(currentItem.getColumnIndex(ContratoSF.Categoria.COD_CATEGORIA));
                Toast.makeText(getActivity(), currentCategoriaId, Toast.LENGTH_SHORT).show();


                //showDetailScreen(currentLawyerId);
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getActivity(), CategoriaRegActivity.class);
                startActivity(register);
            }
        });


        //getActivity().deleteDatabase(BaseDadosSF.NOME_BASE_DADOS);

        // Instancia de helper
        dados = new BaseDadosSF(getActivity());

        // Carga de datos
        loadCategorias();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    private void loadCategorias(){
        new CategoriasLoadTask().execute();
    }

    private class CategoriasLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            String cod_usuario = "";
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }
            return dados.ObterTodasAsCategoriasPorUsuario(cod_usuario);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mCategoriaAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }

}
