package br.com.mefti.simplefinance.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class ExtratoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    BaseDadosSF dados = new BaseDadosSF(this);
    TextView navBartexto1;
    TextView navBartexto2;

    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    public static final String EXTRA_DESPESA_ID = "extra_despesa_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_lancamento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent extrato = new Intent(ExtratoActivity.this, LancamentoActivity.class);
                startActivity(extrato);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Modificando texview cabecera nav bar
        String s1 = "";
        String s2 = "";
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            s1 = cursor.getString(2);
            s2 = cursor.getString(4);
        }
        View hView = navigationView.getHeaderView(0);
        navBartexto1=(TextView)hView.findViewById(R.id.navBartexto1);
        navBartexto2=(TextView)hView.findViewById(R.id.navBartexto2);
        navBartexto1.setText(s1);
        navBartexto2.setText(s2);

        //Adicionando Tabs
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("extrato").setIndicator("Extrato"), ExtratoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("extrato_despesas").setIndicator("Despesas"), ExtratoDespesasFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("extrato_receitas").setIndicator("Receitas"), ExtratoReceitasFragment.class, null);

        //Insertando fragmentos
        ExtratoDespesasFragment despesasFragment = (ExtratoDespesasFragment) getSupportFragmentManager().findFragmentById(R.id.extrato_container);
        if (despesasFragment == null){
            despesasFragment = ExtratoDespesasFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.extrato_container, despesasFragment)
                    .commit();
        }
        ExtratoReceitasFragment receitasFragment = (ExtratoReceitasFragment) getSupportFragmentManager().findFragmentById(R.id.extrato_container);
        if (receitasFragment == null){
            receitasFragment = ExtratoReceitasFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.extrato_container, receitasFragment)
                    .commit();
        }
        ExtratoFragment extratoFragment = (ExtratoFragment) getSupportFragmentManager().findFragmentById(R.id.extrato_container);
        if (extratoFragment == null){
            extratoFragment = ExtratoFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.extrato_container, extratoFragment)
                    .commit();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Sai da aplicacao quando e pulsado o botao voltar
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.extrato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.extrato_filtrar) {
            Intent register = new Intent(ExtratoActivity.this, FiltrarLancamentoActivity.class);
            startActivity(register);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_RecDes) {
            Intent register = new Intent(ExtratoActivity.this, LancamentoActivity.class);
            startActivity(register);
        } else if (id == R.id.nav_Categoria) {
            Intent register = new Intent(ExtratoActivity.this, CategoriaActivity.class);
            startActivity(register);
        } else if (id == R.id.nav_exportar_extrato) {
            exportToExcel();
            Toast.makeText(getApplicationContext(), "Arquivo Armacenado em sdcard/SimpleFinance/SimpleFinance.xls", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_meus_dados) {
            Intent register = new Intent(ExtratoActivity.this, MeusDadosActivity.class);
            startActivity(register);
        } else if (id == R.id.nav_Desconectar) {
            Cursor cursor = dados.ObterUsuarioConectado();
            if (cursor.moveToFirst()){
                //Toast.makeText(getApplicationContext(), cursor.getString(1), Toast.LENGTH_LONG).show();
                Cursor cursor1 = dados.DesconectarUsuarioPorCod(cursor.getString(1));
                Intent i = new Intent(ExtratoActivity.this, LoginActivity.class);
                startActivity(i);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exportToExcel() {
        final String fileName = "SimpleFinance.xls";

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/SimpleFinance");

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        //consultando datos
        String cod_usuario = "";
        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            cod_usuario = cursor.getString(1);
        }
        cursor.close();
        Cursor cursorLancamentos = null;
        cursorLancamentos = dados.ObterLancamentosPorCodUsuario(cod_usuario);


        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Lancamentos", 0);

            try {
                sheet.addCell(new Label(0, 0, "TIPO DE LANÇAMENTO ")); // column and row
                sheet.addCell(new Label(1, 0, "CATEGORIA"));
                sheet.addCell(new Label(2, 0, "DESCRIÇÃO"));
                sheet.addCell(new Label(3, 0, "DATA"));
                sheet.addCell(new Label(4, 0, "VALOR"));
                sheet.addCell(new Label(5, 0, "DATA PREVISTA"));
                sheet.addCell(new Label(6, 0, "VALOR PREVISTO"));
                sheet.addCell(new Label(7, 0, "OBSERVAÇÃO"));


                if (cursorLancamentos.moveToFirst()) {
                    do {
                        String tipoLancamento = "";
                        if (cursorLancamentos.getString(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.TP_LANCAMENTO)).equals("r")) {
                            tipoLancamento = "Receita";
                        }
                        else{
                            tipoLancamento = "Despesa";
                        }
                        String categoria = dados.ObterNomeCategoriaPorCodCategoria(cursorLancamentos.getString(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.COD_CATEGORIA)));
                        String descricao = cursorLancamentos.getString(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.DESCRICAO));
                        long data = cursorLancamentos.getLong(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.DATA));
                        //Convertendo data
                        Date d = new Date (data);
                        DateFormat targetFormat = new SimpleDateFormat("dd/MMM/y");
                        String formattedDate = null;
                        try {
                            formattedDate = targetFormat.format(d);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        String valor = cursorLancamentos.getString(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.VALOR));
                        long dataP = cursorLancamentos.getLong(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.PREVISAO_DATA));
                        //Convertendo data
                        Date dP = new Date (dataP);
                        DateFormat targetFormatP = new SimpleDateFormat("dd/MMM/y");
                        String formattedDateP = null;
                        try {
                            formattedDateP = targetFormatP.format(dP);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        String valorP = cursorLancamentos.getString(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.PREVISAO_VALOR));
                        String observacao = cursorLancamentos.getString(cursorLancamentos.getColumnIndex(ContratoSF.Lancamento.OBSERVACAO));

                        int i = cursorLancamentos.getPosition() + 1;
                        sheet.addCell(new Label(0, i, tipoLancamento));
                        sheet.addCell(new Label(1, i, categoria));
                        sheet.addCell(new Label(2, i, descricao));
                        sheet.addCell(new Label(3, i, formattedDate));
                        sheet.addCell(new Label(4, i, valor));
                        sheet.addCell(new Label(5, i, formattedDateP));
                        sheet.addCell(new Label(6, i, valorP));
                        sheet.addCell(new Label(7, i, observacao));

                    } while (cursorLancamentos.moveToNext());
                }
                //closing cursor
                cursorLancamentos.close();
                cursor.close();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
