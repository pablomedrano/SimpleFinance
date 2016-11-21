package br.com.mefti.simplefinance.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;

public class MeusDadosActivity extends AppCompatActivity {
    BaseDadosSF dados = new BaseDadosSF(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_dados);

        Cursor cursor = dados.ObterUsuarioConectado();
        if(cursor.moveToFirst()){
            EditText userName = (EditText) findViewById(R.id.user_name_update);
            EditText userPass = (EditText) findViewById(R.id.password_update);
            userName.setText(cursor.getString(2));
            userName.setSelection(userName.getText().length());

            userPass.setText(cursor.getString(3));
        }

        final Button button = (Button) findViewById(R.id.button_update);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userName = (EditText) findViewById(R.id.user_name_update);
                EditText userPass = (EditText) findViewById(R.id.password_update);
                String nome = userName.getText().toString();
                String senha = userPass.getText().toString();

                String cod_usuario = "";
                Cursor cursor = dados.ObterUsuarioConectado();
                if(cursor.moveToFirst()){
                    cod_usuario = cursor.getString(1);
                }

                if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(senha)) {
                    Toast.makeText(MeusDadosActivity.this, "Nome e Senha são Obrigatórios!", Toast.LENGTH_SHORT).show();
                }else if (senha.length() < 5){
                    Toast.makeText(MeusDadosActivity.this, "A senha tem que ter mais de quatro dígitos!", Toast.LENGTH_SHORT).show();
                }else{
                    dados.UpdateUsuarioPorCodUsuario(cod_usuario, nome, senha);
                    Intent extrato = new Intent(MeusDadosActivity.this, ExtratoActivity.class);
                    startActivity(extrato);
                }
            }
        });

    }


}
