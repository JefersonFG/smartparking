package com.ihc.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfigsActivity extends AppCompatActivity {
    Button confirmar_button;
    Button cancelar_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);

        TextView tvusername = (TextView)findViewById(R.id.username_logged);
        tvusername.setText(this.getIntent().getStringExtra("username"));

        confirmar_button = (Button) findViewById(R.id.confirmar);
        confirmar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_confirmar_dialog();
            }
        });

        cancelar_button = (Button) findViewById(R.id.cancelar);
        cancelar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void open_confirmar_dialog() {
        NewAlertDialog dialog = new NewAlertDialog("Dados alterados com sucesso!");
        dialog.show(getSupportFragmentManager(), "example dialog");
    }
}
