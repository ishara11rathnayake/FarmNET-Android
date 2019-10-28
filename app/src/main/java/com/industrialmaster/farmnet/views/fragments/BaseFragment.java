package com.industrialmaster.farmnet.views.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public abstract class BaseFragment extends Fragment {

    private android.app.AlertDialog alertDiallog = null;


    public void setLoading(boolean flag){
        if(flag) {
            if(alertDiallog != null ){
                alertDiallog.show();
            }
            else {
                alertDiallog =  new SpotsDialog.Builder().setContext(getActivity()).
                        setMessage("Loading")
                        .setCancelable(false).build();

                alertDiallog.show();
            }

        } else {
            if(alertDiallog != null) alertDiallog.dismiss();
        }
    }

    public void showAlertDialog(String title, String message, boolean setCanceble, String positiveButtonText,
                                DialogInterface.OnClickListener positiveListener, String negativeButtonText,
                                DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(setCanceble)
                .setPositiveButton(positiveButtonText, positiveListener)
                .setNegativeButton(negativeButtonText, negativeListener);

        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    public void showSweetAlert(int alertType, String title, String message, boolean setCanceble, String positiveButtonText,
                               SweetAlertDialog.OnSweetClickListener positiveListener, String negativeButtonText,
                               SweetAlertDialog.OnSweetClickListener negativeListener){

        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), alertType);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setCancelable(setCanceble);
        pDialog.setConfirmButton(positiveButtonText, positiveListener);
        pDialog.setCancelButton(negativeButtonText, negativeListener);
        pDialog.show();
    }

}
