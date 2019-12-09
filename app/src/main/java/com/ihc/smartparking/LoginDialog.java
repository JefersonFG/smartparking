package com.ihc.smartparking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class LoginDialog extends AppCompatDialogFragment {
    private EditText login_username_text;
    private EditText login_senha_text;
    private LoginDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_dialog, null);

        builder.setView(view).setTitle("Login").
                setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = login_username_text.getText().toString();
                        String password = login_senha_text.getText().toString();
                        listener.applyTexts(username, password);
                    }
                });
        login_username_text = view.findViewById(R.id.login_username);
        login_senha_text = view.findViewById(R.id.login_password);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (LoginDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement LoginDialogListener");
        }
    }

    public interface LoginDialogListener {
        void applyTexts(String username, String password);
    }
}
