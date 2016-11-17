package br.com.mefti.simplefinance.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class LancamentoEditarRemoverActivity extends AppCompatActivity {
    String cod_lancamento;
    Cursor cursor;
    BaseDadosSF dados = new BaseDadosSF(this);

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
        setContentView(R.layout.activity_lancamento_editar_remover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //butao para adicionar categoria
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.categoria_reg_lancamento_edit_delete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LancamentoEditarRemoverActivity.this, CategoriaRegActivity.class);
                startActivity(register);
            }
        });

        cod_lancamento = getIntent().getExtras().getString("cod_lancamento");
        //Toast.makeText(LancamentoEditarRemoverActivity.this, cod_lancamento, Toast.LENGTH_SHORT).show();
        cursor = dados.ObterLancamentoPorCodLancamento(cod_lancamento);

        //carregando valores dos radiobuttons e a categoria do lancamento
        final Spinner catLancamento;
        catLancamento = (Spinner)findViewById(R.id.categoria_lancamento_edit_delete);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.opcoes_lancamento_edit_delete1) ;
        if(cursor.moveToFirst()){
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita_edit_delete1);
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa_edit_delete1);
            if (cursor.getString(4).equals("r")){
                radioButtonReceita.setChecked(true);

                String cod_usuario = "";
                Cursor cursor1 = dados.ObterUsuarioConectado();
                if(cursor1.moveToFirst()){
                    cod_usuario = cursor1.getString(1);
                }
                ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"r");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
                catLancamento.setAdapter(adapter);

                String compareValue = dados.ObterNomeCategoriaPorCodCategoria(cursor.getString(3));
                if (!compareValue.equals(null)) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    catLancamento.setSelection(spinnerPosition);
                }

            }else if (cursor.getString(4).equals("d")){
                radioButtonDespesa.setChecked(true);

                String cod_usuario = "";
                Cursor cursor1 = dados.ObterUsuarioConectado();
                if(cursor1.moveToFirst()){
                    cod_usuario = cursor1.getString(1);
                }
                ArrayList<String> list = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"d");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categoria_lancamento, R.id.text_lancamento, list);
                catLancamento.setAdapter(adapter);

                String compareValue = dados.ObterNomeCategoriaPorCodCategoria(cursor.getString(3));
                if (!compareValue.equals(null)) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    catLancamento.setSelection(spinnerPosition);
                }
            }

            //carregando spinner quando o radiobutton cambia de estado
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
                            ArrayAdapter<String> adpR = new ArrayAdapter<>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listR);
                            catLancamento.setAdapter(adpR);
                            break;
                        case 1:
                            ArrayList<String> listD = dados.ObterNomeCategoriaPorCodUsuarioETipo(cod_usuario,"d");
                            ArrayAdapter<String> adpD = new ArrayAdapter<>(getApplicationContext(), R.layout.categoria_lancamento, R.id.text_lancamento, listD);
                            catLancamento.setAdapter(adpD);
                            break;
                    }
                }

            });

            //carregando descricao
            EditText descricaoLancamento = (EditText) findViewById(R.id.descricao_lancamento_edit_delete);
            descricaoLancamento.setText(cursor.getString(5));
            descricaoLancamento.setSelection(descricaoLancamento.getText().length());

            //carregando valor
            EditText valorLancamento = (EditText) findViewById(R.id.valor_lancamento_edit_delete);
            DecimalFormat formatValor = new DecimalFormat("0.00");
            String formattedValor = formatValor.format(Double.parseDouble(cursor.getString(6)));
            valorLancamento.setText(formattedValor);

            //Carregando calendario
            editText_vl = (TextView) findViewById(R.id.texto_data_lancamento_edit_delete);
            imageButton_vl = (ImageButton) findViewById(R.id.data_lancamento_edit_delete);
            calendar_vl = Calendar.getInstance();
            calendar_vl.setTimeInMillis(Long.parseLong(cursor.getString(7)));
            imageButton_vl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDateDialog(editText_vl, calendar_vl);
                }
            });

            editText_vlp = (TextView) findViewById(R.id.texto_data_prevista_lancamento_edit_delete);
            imageButton_vlp = (ImageButton) findViewById(R.id.data_prevista_lancamento_edit_delete);
            calendar_vlp = Calendar.getInstance();
            calendar_vlp.setTimeInMillis(Long.parseLong(cursor.getString(9)));
            imageButton_vlp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDateDialog(editText_vlp, calendar_vlp);
                }
            });
            updateDisplay(editText_vl, calendar_vl);
            updateDisplay(editText_vlp, calendar_vlp);

            //carregando valor previsto
            EditText valorPLancamento = (EditText) findViewById(R.id.valor_previsto_lancamento_edit_delete);
            DecimalFormat formatValorP = new DecimalFormat("0.00");
            String formattedValorP = formatValorP.format(Double.parseDouble(cursor.getString(10)));
            valorPLancamento.setText(formattedValorP);

            //carregando Observacao
            EditText observacaoLancamento = (EditText) findViewById(R.id.observacao_lancamento_edit_delete);
            observacaoLancamento.setText(cursor.getString(11));
        }


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
        getMenuInflater().inflate(R.menu.lancamento_edit_remove_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.lan_reg_save_update) {
            //Consultando estado dos radiobuttons
            RadioButton radioButtonReceita = (RadioButton) findViewById(R.id.radio_receita_edit_delete1);
            boolean receita = radioButtonReceita.isChecked();
            RadioButton radioButtonDespesa = (RadioButton) findViewById(R.id.radio_despesa_edit_delete1);
            boolean despesa = radioButtonDespesa.isChecked();
            //atualizando categoria
            String tipoButtons="";
            if (receita){
                tipoButtons = "r";
            }else if(despesa){
                tipoButtons = "d";
            }

            //consultando String spinner categoria lanzamento
            Spinner spinnerCategoria = (Spinner) findViewById(R.id.categoria_lancamento_edit_delete);
            String sCategoriaText = dados.ObterCodCategoriaPorNome(spinnerCategoria.getSelectedItem().toString());

            //extraindo descricao
            EditText descricaoLancamento = (EditText) findViewById(R.id.descricao_lancamento_edit_delete);
            String dLancamento = descricaoLancamento.getText().toString();

            //extraindo valor
            double vLancamento;
            EditText valorLancamento = (EditText) findViewById(R.id.valor_lancamento_edit_delete);
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

            //extraindo valor previsto
            double vPLancamento;
            EditText valorPrevistoLancamento = (EditText) findViewById(R.id.valor_previsto_lancamento_edit_delete);
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
            EditText observacaoLancamento = (EditText) findViewById(R.id.observacao_lancamento_edit_delete);
            String oLancamento = observacaoLancamento.getText().toString();

            dados.UpdateLancamentoPorCodLancamento(cod_lancamento, sCategoriaText, tipoButtons, dLancamento, vLancamento, date_vl_long, date_vlp_long, vPLancamento, oLancamento);
            Intent extrato = new Intent(LancamentoEditarRemoverActivity.this, ExtratoActivity.class);
            startActivity(extrato);

        }
        if (id == R.id.lan_reg_delete) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("O registros do lancamento sera apagado, deseja confirmar?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    EliminarLancamento();
                    Intent extrato = new Intent(LancamentoEditarRemoverActivity.this, ExtratoActivity.class);
                    startActivity(extrato);
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Intent extrato = new Intent(LancamentoEditarRemoverActivity.this, ExtratoActivity.class);
                    startActivity(extrato);
                }
            });
            dialogo1.show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void EliminarLancamento(){
        dados.EliminarLancamentoPorCodLancamento(cod_lancamento);
    }

}
