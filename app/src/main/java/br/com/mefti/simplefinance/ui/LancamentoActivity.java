package br.com.mefti.simplefinance.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.mefti.simplefinance.R;

import br.com.mefti.simplefinance.modelo.Lancamentos;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;;

public class LancamentoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_lancamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.categoria_reg_lancamento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LancamentoActivity.this, CategoriaRegActivity.class);
                startActivity(register);
            }
        });

        //carregando spinner em funcao radio button e cod usuario
        final Spinner catLancamento;
        catLancamento = (Spinner)findViewById(R.id.categoria_lancamento);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.opcoes_lancamento1) ;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String cod_usuario = "";

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

        String cod_usuario = "";
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            cod_usuario = cursor.getString(1);
        }

        ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
        catLancamento.setAdapter(adapter);

        //calendario
        editText_vl = (TextView) findViewById(R.id.texto_data_lancamento);
        imageButton_vl = (ImageButton) findViewById(R.id.data_lancamento);
        calendar_vl = Calendar.getInstance();
        imageButton_vl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(editText_vl, calendar_vl);
            }
        });

        editText_vlp = (TextView) findViewById(R.id.texto_data_prevista_lancamento);
        imageButton_vlp = (ImageButton) findViewById(R.id.data_prevista_lancamento);
        calendar_vlp = Calendar.getInstance();
        imageButton_vlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(editText_vlp, calendar_vlp);
            }
        });
        updateDisplay(editText_vl, calendar_vl);
        updateDisplay(editText_vlp, calendar_vlp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lancamento_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.lancamento_save) {
            //extraindo cod_usuario
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }

            //Consultando estado dos radiobuttons
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita1);
            boolean receita = radioButtonReceita.isChecked();
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa1);
            boolean despesa = radioButtonDespesa.isChecked();

            //consultando String spinner categoria lanzamento
            Spinner spinnerCategoria = (Spinner) findViewById(R.id.categoria_lancamento);
            String sCategoriaText = dados.ObterCodCategoriaPorNome(spinnerCategoria.getSelectedItem().toString());

            //extraindo descricao
            EditText descricaoLancamento = (EditText) findViewById(R.id.descricao_lancamento);
            String dLancamento = descricaoLancamento.getText().toString();

            //extraindo valor
            double vLancamento;
            EditText valorLancamento = (EditText) findViewById(R.id.valor_lancamento);
            try {
                vLancamento = Double.valueOf(valorLancamento.getText().toString());
            }catch (NumberFormatException e){
                vLancamento = 0;
            }

            //extraindo data
            String data_vl = editText_vl.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                date_vl = format.parse(data_vl);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date_vl_long = date_vl.getTime();

            //extraindo posicion do spinner repetir
            Spinner repetir = (Spinner)findViewById(R.id.repetir_lancamento);
            int posicao_repetir = repetir.getSelectedItemPosition();
            String mensaje = Integer.toString(posicao_repetir);
            //Toast.makeText(LancamentoActivity.this, mensaje, Toast.LENGTH_SHORT).show();

            //extraindo valor previsto
            double vPLancamento;
            EditText valorPrevistoLancamento = (EditText) findViewById(R.id.valor_previsto_lancamento);
            try {
                vPLancamento = Double.valueOf(valorPrevistoLancamento.getText().toString());
            }catch (NumberFormatException e){
                vPLancamento = 0;
            }

            //extraindo data prevista
            String data_vlp = editText_vlp.getText().toString();
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                date_vlp = format1.parse(data_vlp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date_vlp_long = date_vlp.getTime();

            //extraindo observacao
            EditText observacaoLancamento = (EditText) findViewById(R.id.observacao_lancamento);
            String oLancamento = observacaoLancamento.getText().toString();

            //Salvando Lancamento
            if (!dLancamento.equals("")){
                if (posicao_repetir == 0){
                    Lancamentos lancamentos = new Lancamentos();
                    lancamentos.setCod_usuario(cod_usuario);
                    lancamentos.setCod_categoria(sCategoriaText);
                    if(receita)
                        lancamentos.setTp_lancamento("r");
                    else if (despesa)
                        lancamentos.setTp_lancamento("d");
                    lancamentos.setDescricao(dLancamento);
                    lancamentos.setValor(vLancamento);
                    lancamentos.setData(date_vl_long);
                    lancamentos.setRepetir(Integer.toString(posicao_repetir));
                    lancamentos.setPrevisao_data(date_vlp_long);
                    lancamentos.setPrevisao_valor(vPLancamento);
                    lancamentos.setObservacao(oLancamento);
                    dados.inserirLancamento(lancamentos);
                }
                if(posicao_repetir == 1){ //mensualmente durante 2 anos
                    for (int i = 0 ; i<24 ; i++) {
                        Lancamentos lancamentos = new Lancamentos();
                        lancamentos.setCod_usuario(cod_usuario);
                        lancamentos.setCod_categoria(sCategoriaText);
                        if(receita)
                            lancamentos.setTp_lancamento("r");
                        else if (despesa)
                            lancamentos.setTp_lancamento("d");
                        lancamentos.setDescricao(dLancamento);
                        lancamentos.setValor(vLancamento);
                        lancamentos.setData(date_vl_long);
                        lancamentos.setRepetir(Integer.toString(posicao_repetir));
                        lancamentos.setPrevisao_data(date_vlp_long);
                        lancamentos.setPrevisao_valor(vPLancamento);
                        lancamentos.setObservacao(oLancamento);
                        dados.inserirLancamento(lancamentos);

                        Calendar c = Calendar.getInstance();
                        c.setTime(date_vl);
                        c.add(Calendar.MONTH,+1);
                        date_vl = c.getTime();
                        date_vl_long = date_vl.getTime();
                    }
                }
                if(posicao_repetir == 2){ //cada 3 meses durante 2 anos
                    for (int i = 0 ; i<8 ; i++) {
                        Lancamentos lancamentos = new Lancamentos();
                        lancamentos.setCod_usuario(cod_usuario);
                        lancamentos.setCod_categoria(sCategoriaText);
                        if(receita)
                            lancamentos.setTp_lancamento("r");
                        else if (despesa)
                            lancamentos.setTp_lancamento("d");
                        lancamentos.setDescricao(dLancamento);
                        lancamentos.setValor(vLancamento);
                        lancamentos.setData(date_vl_long);
                        lancamentos.setRepetir(Integer.toString(posicao_repetir));
                        lancamentos.setPrevisao_data(date_vlp_long);
                        lancamentos.setPrevisao_valor(vPLancamento);
                        lancamentos.setObservacao(oLancamento);
                        dados.inserirLancamento(lancamentos);

                        Calendar c = Calendar.getInstance();
                        c.setTime(date_vl);
                        c.add(Calendar.MONTH,+3);
                        date_vl = c.getTime();
                        date_vl_long = date_vl.getTime();
                    }
                }

                if(posicao_repetir == 3){ //semestral durante 5 anos
                    for (int i = 0 ; i<10 ; i++) {
                        Lancamentos lancamentos = new Lancamentos();
                        lancamentos.setCod_usuario(cod_usuario);
                        lancamentos.setCod_categoria(sCategoriaText);
                        if(receita)
                            lancamentos.setTp_lancamento("r");
                        else if (despesa)
                            lancamentos.setTp_lancamento("d");
                        lancamentos.setDescricao(dLancamento);
                        lancamentos.setValor(vLancamento);
                        lancamentos.setData(date_vl_long);
                        lancamentos.setRepetir(Integer.toString(posicao_repetir));
                        lancamentos.setPrevisao_data(date_vlp_long);
                        lancamentos.setPrevisao_valor(vPLancamento);
                        lancamentos.setObservacao(oLancamento);
                        dados.inserirLancamento(lancamentos);

                        Calendar c = Calendar.getInstance();
                        c.setTime(date_vl);
                        c.add(Calendar.MONTH,+6);
                        date_vl = c.getTime();
                        date_vl_long = date_vl.getTime();
                    }
                }

                if(posicao_repetir == 4){ //anual durante 5 anos
                    for (int i = 0 ; i<5 ; i++) {
                        Lancamentos lancamentos = new Lancamentos();
                        lancamentos.setCod_usuario(cod_usuario);
                        lancamentos.setCod_categoria(sCategoriaText);
                        if(receita)
                            lancamentos.setTp_lancamento("r");
                        else if (despesa)
                            lancamentos.setTp_lancamento("d");
                        lancamentos.setDescricao(dLancamento);
                        lancamentos.setValor(vLancamento);
                        lancamentos.setData(date_vl_long);
                        lancamentos.setRepetir(Integer.toString(posicao_repetir));
                        lancamentos.setPrevisao_data(date_vlp_long);
                        lancamentos.setPrevisao_valor(vPLancamento);
                        lancamentos.setObservacao(oLancamento);
                        dados.inserirLancamento(lancamentos);

                        Calendar c = Calendar.getInstance();
                        c.setTime(date_vl);
                        c.add(Calendar.MONTH,+12);
                        date_vl = c.getTime();
                        date_vl_long = date_vl.getTime();
                    }
                }
                Intent extrato = new Intent(LancamentoActivity.this, ExtratoActivity.class);
                startActivity(extrato);

            }else{
                Toast.makeText(LancamentoActivity.this, "Descrição e valor sao dados obrigratórios", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
