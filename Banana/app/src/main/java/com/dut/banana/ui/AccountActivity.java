package com.dut.banana.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.banana.Account;
import com.dut.banana.R;
import com.dut.banana.lib.Config;
import com.dut.banana.lib.SharedObject;
import com.dut.banana.net.ServerConnection;
import com.dut.banana.ui.helper.AccountHelper;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, AccountHelper.IWorker {
    private void autoSwitchMode() {
        View haveAccountLayout = findViewById(R.id.account_ll_exists);
        View notHaveAccountLayout = findViewById(R.id.account_ll_notexists);
        TextView tvUsername = (TextView) findViewById(R.id.account_tv_username);

        Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);

        if (account != null) {
            haveAccountLayout.setVisibility(View.VISIBLE);
            notHaveAccountLayout.setVisibility(View.GONE);
            tvUsername.setText(account.getUserName());
        } else {
            haveAccountLayout.setVisibility(View.GONE);
            notHaveAccountLayout.setVisibility(View.VISIBLE);
            tvUsername.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);
        autoSwitchMode();

        Button btnLogin = (Button) findViewById(R.id.account_btn_login);
        Button btnSignup = (Button) findViewById(R.id.account_btn_signup);
        Button btnLogout = (Button) findViewById(R.id.account_btn_logout);
        Button btnPassword = (Button) findViewById(R.id.account_btn_changepassword);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AccountHelper accountHelper = (AccountHelper) SharedObject.getInstance().get(Config.SHARED_ACCOUNT_HELPER);
        switch (v.getId()) {
            case R.id.account_btn_login:
                accountHelper.login(this, this);
                break;
            case R.id.account_btn_signup:
                accountHelper.createNewAccount(this, this);
                break;
            case R.id.account_btn_logout:
                requestLogout();
                break;
            case R.id.account_btn_changepassword:
                changePassowrd();
                break;
        }
    }

    private void changePassowrd() {
        final AccountHelper accountHelper = (AccountHelper) SharedObject.getInstance().get(Config.SHARED_ACCOUNT_HELPER);
        final Account account = accountHelper.getAccount();
        if (account == null)
            return;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_change_password);
        dialog.setTitle(R.string.app_name);
        final EditText oldpassword = (EditText) dialog.findViewById(R.id.change_password_et_oldpassword);
        final EditText newpassword = (EditText) dialog.findViewById(R.id.change_password_et_newpassword);
        final EditText confirm = (EditText) dialog.findViewById(R.id.change_password_et_confirm);

        Button btnOK = (Button) dialog.findViewById(R.id.change_password_btn_get);
        Button btnCancel = (Button) dialog.findViewById(R.id.change_password_btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _oldpassword = oldpassword.getText().toString();
                final String _newpassword = newpassword.getText().toString();
                String _confirm = confirm.getText().toString();
                if (!_oldpassword.equals(account.getPassword())) {
                    Toast.makeText(AccountActivity.this, "Old password do not match", Toast.LENGTH_SHORT).show();
                } else if (_newpassword.length() < 6) {
                    Toast.makeText(AccountActivity.this, "New password length must have least 6 characters", Toast.LENGTH_SHORT).show();
                } else if (!_newpassword.equals(_confirm)) {
                    Toast.makeText(AccountActivity.this, "Confirm password do not match with new password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccountActivity.this, "Sending new password to server...", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean ok = ServerConnection.getInstance().changeAccountPassword(account, _newpassword);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ok) {
                                        Toast.makeText(AccountActivity.this, "Changed your password", Toast.LENGTH_SHORT).show();
                                        account.setPassword(_newpassword);
                                        accountHelper.updateAccount(account);
                                    }
                                    else
                                        Toast.makeText(AccountActivity.this, "Something wrong! maybe your network broken down or server have some fault", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Are you sure? do you want to logout? you'll regret it!");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Logout now!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccountHelper accountHelper = (AccountHelper) SharedObject.getInstance().get(Config.SHARED_ACCOUNT_HELPER);
                accountHelper.clearAccountCache();
                Toast.makeText(AccountActivity.this, "Now! you are logout!", Toast.LENGTH_SHORT).show();
                autoSwitchMode();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onAccountEnter(Account account) {
        autoSwitchMode();
    }

    @Override
    public void onAccountRefuse() {
    }
}
