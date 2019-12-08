package com.ihc.smartparking;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class HistoricoDialog extends AppCompatDialogFragment {

    private String historico;

    HistoricoDialog(ArrayList<Registro> registros) {
        int i;
        historico = "";
        String valor;
        for (i = registros.size()-1; i >=0; i--) {
            historico += registros.get(i).tipo + "\t\t- " + registros.get(i).localTime;
            if (registros.get(i).tipo == "Saida") {
                if (registros.get(i).valor > 0)
                    historico += " - R$: " + Double.toString(registros.get(i).valor);
                else
                    historico += " - ISENTO";
            }
            historico += "\n";
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Histórico")
                .setMessage(historico)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
