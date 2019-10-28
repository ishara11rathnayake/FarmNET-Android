package com.industrialmaster.farmnet.views.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.industrialmaster.farmnet.presenters.Presenter;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public abstract class BaseActivity extends AppCompatActivity {

    private  android.app.AlertDialog alertDiallog = null;

    protected Presenter presenter;

    Uri image_uri;
    Uri imageFilePath;

    protected static final int IMAGE_PIK_CODE = 1000;
    protected static final int GALLERY_PERMISSION_CODE = 1001;
    protected static final int CAMERA_PERMISSION_CODE = 1002;
    protected static final int IMAGE_CAPTURE_CODE = 1003;


    public void setLoading(boolean flag){
        if(flag) {
            if(alertDiallog != null ){
                alertDiallog.show();
            }
            else {
                alertDiallog =  new SpotsDialog.Builder().setContext(this).
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
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

        SweetAlertDialog pDialog = new SweetAlertDialog(this, alertType);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setCancelable(setCanceble);
        pDialog.setConfirmButton(positiveButtonText, positiveListener);
        pDialog.setCancelButton(negativeButtonText, negativeListener);
        pDialog.show();
    }

    protected void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,  "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //camera intent
        Intent cameraIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    public void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent((Intent.ACTION_PICK));
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PIK_CODE);
    }

    //handle results of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called when user presses Allow or Deny from permission request popup
        switch (requestCode) {
            case GALLERY_PERMISSION_CODE: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            case CAMERA_PERMISSION_CODE: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    openCamera();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //get real image path from uri
    public String convertMediaUriToPath(Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
        int columnIndex = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }



}
