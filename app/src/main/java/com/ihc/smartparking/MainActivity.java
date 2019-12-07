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

    public final int qr_request_code = 0;
    private final OkHttpClient client = new OkHttpClient();
    private String user_id;

    // interface buttons:
    private Button login_button;
    private Button new_user_button;
    private Button qrcode_test;
    // ---------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO Overly simple id gereration, implement user sign up and sign in
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SmartParking", MODE_PRIVATE);
        user_id = pref.getString("user_id", "");

        if (user_id.equals("")) {
            user_id = UUID.randomUUID().toString();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("user_id", user_id);
            editor.apply();
        }

        // TODO Overly simple permission request, should be checked and called when used, deal with not granted
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);

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
        qrcode_test = (Button) findViewById(R.id.qrcode_test);
        qrcode_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQRCodeClick(this);
            }
        });
        // -----------------------------

    }

    public void readQRCodeClick(View.OnClickListener view) {
        Intent i = new Intent(this, SimpleScannerActivity.class);
        startActivityForResult(i, qr_request_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == qr_request_code) {
            if (resultCode == RESULT_OK) {
                // Checks the QR Code data
                String returnedResult = data.getDataString();

                if (returnedResult.equals("parking_test")) {
                    // Get current time in ISO 8601 format
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    df.setTimeZone(tz);
                    String current_time = df.format(new Date());

                    // Sends the parking time to the server
                    Request request = new Request.Builder()
                            .url(String.format("https://salty-cliffs-90817.herokuapp.com/park/%s/%s", user_id, current_time))
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, final Response response) throws IOException {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try (ResponseBody responseBody = response.body()) {
                                        String responseString = responseBody.string();

                                        if (responseString.startsWith(user_id)) {
                                            int separatorPosition = responseString.lastIndexOf('-');
                                            String serverMessage = responseString.substring(separatorPosition + 1);
                                            new AlertDialog.Builder(MainActivity.this)
                                                    .setTitle("Server response")
                                                    .setMessage(serverMessage)
                                                    .show();
                                        }
                                    }
                                    catch (IOException e) {
                                        // TODO Review this thread code, at least treat the exceptions
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }

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
    public void applyTexts(String username, String password) {
        //openDialog(password);

        if (!DatabaseRequisition.is_valid_user(username, password)) {
            NewAlertDialog dialog = new NewAlertDialog("Usuário ou senha inválidos!");
            dialog.show(getSupportFragmentManager(), "example dialog");
        }
        else {
            // faz login do usuário.
        }
    }

    // - abre dialog com mensagem.
    public void openDialog(String message) {
        NewAlertDialog dialog = new NewAlertDialog(message);
        dialog.show(getSupportFragmentManager(), "example dialog");
    }
    // --------------------------------------------------------------------------------

}
