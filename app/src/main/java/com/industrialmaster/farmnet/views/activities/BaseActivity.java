package com.industrialmaster.farmnet.views.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog = null;

    public void setLoading(boolean flag){
        if(flag) {
            if(progressDialog != null )progressDialog.show();
            else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Its loading.......");
                progressDialog.setTitle("Please Wait....");
                progressDialog.show();
            }
        } else {
            if(progressDialog != null) progressDialog.dismiss();
        }
    }

    public void showAlertDialog(String title, String message, boolean setCanceble, String positiveButtonText,
                                DialogInterface.OnClickListener positiveListener, String negativeButtonText,
                                DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(setCanceble)
                .setPositiveButton(positiveButtonText, positiveListener)
                .setNegativeButton(negativeButtonText, negativeListener);

                alertDialogBuilder.create();
                alertDialogBuilder.show();

    }

}
