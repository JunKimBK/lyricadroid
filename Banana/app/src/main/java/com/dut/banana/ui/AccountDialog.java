package com.dut.banana.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dut.banana.Account;
import com.dut.banana.R;

/**
 * Copyright by NE 2015.
 * Created by noem on 17/11/2015.
 */
public class AccountDialog {
    private Dialog dialog;
    private EditText username;
    private EditText password;
    private View.OnClickListener mOnClickListener;

    public Account getAccount() {
        Account account = new Account();
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        if (username.length() < 3) {
            Toast.makeText(dialog.getContext(), "User name is too short, it must greater 3", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (password.length() < 6) {
            Toast.makeText(dialog.getContext(), "Password is too short, it must greater 6", Toast.LENGTH_SHORT).show();
            return null;
        }
        account.setUserName(username);
        account.setPassword(password);
        return account;
    }

    public void setOnOKListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void show() {
        dialog.show();
    }

    public AccountDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_account);
        dialog.setTitle(R.string.app_name);
        username = (EditText) dialog.findViewById(R.id.account_et_username);
        password = (EditText) dialog.findViewById(R.id.account_et_password);

        Button btnOK = (Button) dialog.findViewById(R.id.account_btn_get);
        Button btnCancel = (Button) dialog.findViewById(R.id.account_btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(null);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
