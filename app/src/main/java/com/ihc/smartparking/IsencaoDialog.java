package com.ihc.smartparking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class IsencaoDialog extends AppCompatDialogFragment {
    private EditText password_isencao_text;
    private IsencaoDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.isencao_dialog, null);
        builder.setView(view)
                .setTitle("Isenção")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String codigo_isencao = password_isencao_text.getText().toString();
                        listener.applyText(codigo_isencao);
                    }
                });

        password_isencao_text = view.findViewById(R.id.codigo_isencao_text);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IsencaoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement IsencaoDialogListener");
        }

    }

    public interface IsencaoDialogListener {
        void applyText(String codigo_isencao);
    }
}
