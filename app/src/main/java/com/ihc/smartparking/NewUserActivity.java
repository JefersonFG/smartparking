package com.ihc.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewUserActivity extends AppCompatActivity {
    private Button confirmar;
    private EditText email_text;
    private EditText username_text;
    private EditText password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Novo usuário");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        confirmar = (Button) findViewById(R.id.confirmar_button);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmar_new_user();
            }
        });

        email_text = findViewById(R.id.email_input);
        username_text = findViewById(R.id.username_input);
        password_text = findViewById(R.id.password_input);
    }

    public void confirmar_new_user() {
        // ler os dados do usuario:
        String email = email_text.getText().toString();
        String username = username_text.getText().toString();
        String password = password_text.getText().toString();

        // leu dados do novo usuario.
        if (DatabaseRequisition.user_exist(username)) {
            NewAlertDialog dialog = new NewAlertDialog("Usuário já existente!");
            dialog.show(getSupportFragmentManager(), "example dialog");
        } else {
            // registra novo usuário no banco de dados.
            if (DatabaseRequisition.new_user(email, username, password)) {
                NewAlertDialog dialog = new NewAlertDialog("Usuário registrado com sucesso!");
                dialog.show(getSupportFragmentManager(), "example dialog");
            }
            else {
                NewAlertDialog dialog = new NewAlertDialog("Erro ao registrar novo usuário.");
                dialog.show(getSupportFragmentManager(), "example dialog");
            }
        }

    }
}
