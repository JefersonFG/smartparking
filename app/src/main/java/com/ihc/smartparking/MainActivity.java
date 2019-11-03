package com.ihc.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final int qr_request_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO Overly simple permission request, should be checked and called when used, deal with not granted
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
    }

    public void readQRCodeClick(View view) {
        Intent i = new Intent(this, SimpleScannerActivity.class);
        startActivityForResult(i, qr_request_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == qr_request_code) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getDataString();

                // Prints the qr code content returned
                // TODO Should send to backend, which should handle payments (maybe show them on the app?)
                new AlertDialog.Builder(this)
                        .setTitle("QR Code content")
                        .setMessage(returnedResult)
                        .show();
            }
        }
    }
}
