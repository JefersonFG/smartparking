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
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoggedActivity extends AppCompatActivity implements IsencaoDialog.IsencaoDialogListener {

    // qr code:
    public final int qr_request_code = 0;
    private final OkHttpClient client = new OkHttpClient();
    private String user_id;
    // -------------------

    private Button button_isencao;
    private Button button_sair;
    private Button button_configs;
    private Button button_qr_code;
    private Button button_historico;

    // user:
    private String username = "defaultuser";
    private String password = "defaultpassword";

    private ArrayList<Registro> registros;

    private final double price = 5.0;  // preco do estacionamento cada meia hora.

    private boolean isento = false;
    // ---------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        registros = new ArrayList<>();


        // qr code:
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
        // -------------


        TextView tvusername = (TextView)findViewById(R.id.username_logged);
        username = this.getIntent().getStringExtra("username");
        password = this.getIntent().getStringExtra("password");
        tvusername.setText(this.getIntent().getStringExtra("username"));

        button_isencao = (Button) findViewById(R.id.isencao);
        button_isencao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_isencao_dialog();
            }
        });
        button_sair = (Button) findViewById(R.id.sair);
        button_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
        button_configs = (Button) findViewById(R.id.configuracoes);
        button_configs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_configuracoes();
            }

        });

        button_qr_code = (Button) findViewById(R.id.qrcode);
        button_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readQRCodeClick(this);
            }
        });

        button_historico = (Button) findViewById(R.id.historico);
        button_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_historico();
            }

        });

    }

    public void open_isencao_dialog() {
        IsencaoDialog dialog = new IsencaoDialog();
        dialog.show(getSupportFragmentManager(), "isencao dialog");
    }

    public void open_configuracoes() {
        Intent intent = new Intent(this, ConfigsActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    @Override
    public void applyText(String codigo_isencao) {
        if (DatabaseRequisition.isencao(username, codigo_isencao)) {
            isento = true;
            NewAlertDialog dialog = new NewAlertDialog("Isenção realizada com sucesso!");
            dialog.show(getSupportFragmentManager(), "example dialog");
        }
        else {
            NewAlertDialog dialog = new NewAlertDialog("Isenção não pode ser realizada!");
            dialog.show(getSupportFragmentManager(), "example dialog");
        }
    }


    public void open_historico() {
        HistoricoDialog historico_dialog = new HistoricoDialog(registros);
        historico_dialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void registra_entrada() {
        Registro entrada = new Registro("Entrada");
        registros.add(entrada);
        new AlertDialog.Builder(LoggedActivity.this)
                .setTitle("Informação")
                .setMessage("Entrada registrada com sucesso!")
                .show();
    }

    public void registra_saida() {
        Registro saida = new Registro("Saida");
        Registro last = registros.get(registros.size() - 1);
        double tempo = saida.time - last.time;
        if (!isento)
            saida.valor = Math.ceil(tempo / (60 * 30)) * price;
        else
            saida.valor = -1;
        registros.add(saida);

        new AlertDialog.Builder(LoggedActivity.this)
                .setTitle("Informação")
                .setMessage("Saida registrada com sucesso!")
                .show();
    }

    // ----------------------------------------------------------------------------------------
    // qr code:
    public void readQRCodeClick(View.OnClickListener view) {
        Intent i = new Intent(this, SimpleScannerActivity.class);
        startActivityForResult(i, qr_request_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == qr_request_code) {
            if (resultCode == RESULT_OK) {

                // Checks the QR Code data
                String returnedResult = data.getDataString();

                // Teste de entrada ou saida no estacionamento:
                if (returnedResult.equals("entrada")) {
                    registra_entrada();
                }
                else if (returnedResult.equals("saida")) {
                    registra_saida();
                }
                // ----------------------------------

                else if (returnedResult.equals("parking_test")) {
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
                            LoggedActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try (ResponseBody responseBody = response.body()) {
                                        String responseString = responseBody.string();

                                        if (responseString.startsWith(user_id)) {
                                            int separatorPosition = responseString.lastIndexOf('-');
                                            String serverMessage = responseString.substring(separatorPosition + 1);
                                            new AlertDialog.Builder(LoggedActivity.this)
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


}
