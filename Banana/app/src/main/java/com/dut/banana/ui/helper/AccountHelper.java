package com.dut.banana.ui.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.dut.banana.Account;
import com.dut.banana.R;
import com.dut.banana.lib.Config;
import com.dut.banana.lib.SharedObject;
import com.dut.banana.model.IDataAccess;
import com.dut.banana.net.ServerConnection;
import com.dut.banana.ui.AccountDialog;
import com.dut.banana.ui.SignupDialog;

/**
 * Copyright by NE 2015.
 * Created by noem on 19/11/2015.
 */
public final class AccountHelper {

    public Account getAccount() {
        Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);
        return account;
    }

    public void clearAccountCache() {
        SharedObject.getInstance().remove(Config.SHARED_ACCOUNT);
        IDataAccess database = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
        database.deleteAccount();
    }

    public void updateAccount(Account account) {
        cacheAccount(account, false);
        IDataAccess database = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
        database.updateAccount(account);
    }

    private void cacheAccount(Account account, boolean isNew) {
        SharedObject.getInstance().set(Config.SHARED_ACCOUNT, account);
        if (isNew) {
            IDataAccess database = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
            database.addAccount(account);
        }
    }

    public void createNewAccount(final Activity activity, final IWorker worker) {
        final SignupDialog dialog = new SignupDialog(activity);
        dialog.setOnOKListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Account account = dialog.getAccount();
                if (account != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean ok = ServerConnection.getInstance().signup(account);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ok) {
                                        Toast.makeText(activity, "Yolooo! now, you are a member of Banana system!", Toast.LENGTH_LONG).show();
                                        cacheAccount(account, true);
                                        worker.onAccountEnter(account);
                                    } else {
                                        Toast.makeText(activity, "Sorry! something wrong, can network broken down or user name is exists", Toast.LENGTH_LONG).show();
                                        worker.onAccountRefuse();
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    worker.onAccountRefuse();
                }
            }
        });
        dialog.show();
    }

    public void login(final Activity activity, final IWorker worker) {
        final AccountDialog dialog = new AccountDialog(activity);
        dialog.setOnOKListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Account account = dialog.getAccount();
                if (account != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean ok = ServerConnection.getInstance().login(account);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ok) {
                                        Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show();
                                        cacheAccount(account, true);
                                        worker.onAccountEnter(account);
                                    } else {
                                        Toast.makeText(activity, "Login fail! wrong username or password!", Toast.LENGTH_SHORT).show();
                                        worker.onAccountRefuse();
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    worker.onAccountRefuse();
                }
            }
        });
        dialog.show();
    }

    private void requestUserSelectMethod(final Activity activity, final IWorker worker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name);
        builder.setMessage("You do not have an account yet!");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Create new!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewAccount(activity, worker);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                login(activity, worker);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void requestAccount(Activity activity, IWorker worker) {
        Account account = getAccount();
        if (account != null) {
            worker.onAccountEnter(account);
        } else {
            requestUserSelectMethod(activity, worker);
        }
    }

    public void cacheAccount() {
        IDataAccess database = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
        Account account = database.getAccount();
        if (account != null)
            cacheAccount(account, false);
    }

    public interface IWorker {
        void onAccountEnter(Account account);

        void onAccountRefuse();
    }
}
