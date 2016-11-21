package br.com.mefti.simplefinance.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class FiltrarLancamentoActivity extends AppCompatActivity {

    BaseDadosSF dados = new BaseDadosSF(this);

    String cod_usuario;
    Date date_vl,date_vlp;
    long date_vl_long, date_vlp_long;

    //variables para calendario
    private TextView editText_vl;
    private TextView editText_vlp;
    private ImageButton imageButton_vl;
    private ImageButton imageButton_vlp;
    private Calendar calendar_vl;
    private Calendar calendar_vlp;
    static final int DATE_DIALOG_ID = 0;
    private TextView activeDateDisplay;
    private Calendar activeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_lancamento);

        //carregando spinner em funcao radio button e cod usuario
        final Spinner catLancamento;
        catLancamento = (Spinner)findViewById(R.id.categoria_lancamento1);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.opcoes_lancamento2) ;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int posicao_lancamento_rb;

                Cursor cursor = dados.ObterUsuarioConectado();
                if(cursor.moveToFirst()){
                    cod_usuario = cursor.getString(1);
                }

                posicao_lancamento_rb = radioGroup.indexOfChild(findViewById(i));
                switch (posicao_lancamento_rb){
                    case 0:
                        ArrayList<String> listR = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
                        ArrayAdapter<String> adpR = new ArrayAdapter<String>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listR);
                        catLancamento.setAdapter(adpR);
                        //Toast.makeText(getBaseContext(), "Receita", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        ArrayList<String> listD = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"d");
                        ArrayAdapter<String> adpD = new ArrayAdapter<String>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listD);
                        catLancamento.setAdapter(adpD);
                        //Toast.makeText(getBaseContext(), "Despesa", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        });

        //carregando spinner e extraindo valor selecionado
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            cod_usuario = cursor.getString(1);
        }

        ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
        catLancamento.setAdapter(adapter);

        //calendario
        editText_vl = (TextView) findViewById(R.id.texto_data_periodo);
        imageButton_vl = (ImageButton) findViewById(R.id.data_periodo);
        calendar_vl = Calendar.getInstance();
        imageButton_vl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(editText_vl, calendar_vl);
            }
        });

        editText_vlp = (TextView) findViewById(R.id.texto_data_periodo1);
        imageButton_vlp = (ImageButton) findViewById(R.id.data_periodo1);
        calendar_vlp = Calendar.getInstance();
        imageButton_vlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(editText_vlp, calendar_vlp);
            }
        });
        updateDisplay(editText_vl, calendar_vl);
        updateDisplay(editText_vlp, calendar_vlp);

        final Button button = (Button) findViewById(R.id.button_filtrar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Consultando estado dos radiobuttons
                RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita2);
                boolean receita = radioButtonReceita.isChecked();
                RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa2);
                boolean despesa = radioButtonDespesa.isChecked();

                //consultando String spinner categoria lanzamento
                Spinner spinnerCategoria = (Spinner) findViewById(R.id.categoria_lancamento1);
                String sCategoriaText = dados.ObterCodCategoriaPorNome(spinnerCategoria.getSelectedItem().toString());

                //extraindo descricao
                EditText descricaoLancamento = (EditText) findViewById(R.id.descricao_lancamento1);
                String dLancamento = descricaoLancamento.getText().toString();

                //extraindo data
                String data_vl = editText_vl.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                try {
                    date_vl = format.parse(data_vl);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date_vl_long = date_vl.getTime();

                //extraindo data 2
                String data_vlp = editText_vlp.getText().toString();
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                try {
                    date_vlp = format1.parse(data_vlp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date_vlp_long = date_vlp.getTime();

                Intent dadosFiltro = new Intent(FiltrarLancamentoActivity.this, ResultadoFiltroLancamentoActivity.class);
                dadosFiltro.putExtra("receita", receita);
                dadosFiltro.putExtra("despesa", despesa);
                dadosFiltro.putExtra("cod_categoria", sCategoriaText);
                dadosFiltro.putExtra("descricao", dLancamento);
                dadosFiltro.putExtra("data1", date_vl_long);
                dadosFiltro.putExtra("data2", date_vlp_long);
                startActivity(dadosFiltro);

            }
        });
    }

    //Inicio metodos para calendario
    public void updateDisplay(TextView dateDisplay, Calendar date){
        dateDisplay.setText(
                new StringBuffer()
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("/")
                        .append(date.get(Calendar.MONTH)+1).append("/")
                        .append(date.get(Calendar.YEAR)).append("")
        );
    }
    public void showDateDialog (TextView dateDisplay, Calendar date){
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };
    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
    //Fin metodos para calendario
}
