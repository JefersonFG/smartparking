package com.ihc.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements LoginDialog.LoginDialogListener {

    // interface buttons:
    private Button login_button;
    private Button new_user_button;
    // ---------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // -----------------------------
        // buttons config:
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginDialog();
            }
        });
        new_user_button = (Button) findViewById(R.id.new_user_button);
        new_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewUserActivity();
            }
        });
        // -----------------------------

    }

    // --------------------------------------------------------------------------------
    // - abre dialog do login:
    public void openLoginDialog() {
        LoginDialog exampleDialog = new LoginDialog();
        exampleDialog.show(getSupportFragmentManager(),
                "example dialog");
    }

    // - abre activity de new user:
    public void openNewUserActivity() {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void applyTexts(String login_username, String login_password) {
        //openDialog(password);

        if (!DatabaseRequisition.is_valid_user(login_username, login_password)) {
            NewAlertDialog dialog = new NewAlertDialog("Usuário ou senha inválidos!");
            dialog.show(getSupportFragmentManager(), "example dialog");
        }
        else {
            // faz login do usuário.
            Intent intent = new Intent(this, LoggedActivity.class);
            intent.putExtra("username", login_username);
            intent.putExtra("password", login_password);
            startActivity(intent);
        }
    }

    // - abre dialog com mensagem.
    public void openDialog(String message) {
        NewAlertDialog dialog = new NewAlertDialog(message);
        dialog.show(getSupportFragmentManager(), "example dialog");
    }
    // --------------------------------------------------------------------------------

}
