package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.presenters.Presenter;
import com.industrialmaster.farmnet.presenters.ProfilePresenter;
import com.industrialmaster.farmnet.presenters.ProfilePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.UpdateUserView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity implements UpdateUserView {

    ProfilePresenter presenter;

    boolean hasImage = false;

    CircleImageView circle_image_view_profile_pic;
    ImageButton img_btn_close;
    EditText et_dob, et_name, et_nic, et_phone, et_address;
    DatePickerDialog picker;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        presenter = new ProfilePresenterImpl(this, EditProfileActivity.this);

        circle_image_view_profile_pic = findViewById(R.id.cimageview_profilepic);
        et_dob = findViewById(R.id.et_dob);
        et_name = findViewById(R.id.et_name);
        et_nic = findViewById(R.id.et_nic);
        et_phone = findViewById(R.id.et_contact_number);
        et_address = findViewById(R.id.et_address);
        img_btn_close = findViewById(R.id.img_btn_close);
        btn_save = findViewById(R.id.btn_save);

        et_dob = findViewById(R.id.et_dob);

        setLoading(true);
        presenter.getUserDetails();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String realFilePath;

                User user = new User();
                user.setName(et_name.getText().toString());
                user.setAddress(et_address.getText().toString());
                user.setNic(et_nic.getText().toString());
                user.setContactNumber(et_phone.getText().toString());

                String date = et_dob.getText().toString();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date dob;
                try {
                    dob = format.parse(date);
                    user.setDob(dob);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(hasImage == true) {
                    realFilePath = convertMediaUriToPath(imageFilePath);
                    user.setProfilePicUrl(realFilePath);
                }

                presenter.updateUserDetails(user);

            }
        });

        //pick image from gallery clicking image view
        circle_image_view_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, GALLERY_PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system os less than marshmallow
                    pickImageFromGallery();
                }
            }
        });

        //close create new deal activity
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
                showAlertDialog("Warning", message,false, FarmnetConstants.OK , (dialog, which) -> {
                    finish();
                },FarmnetConstants.CANCEL, (dialog, which) -> dialog.dismiss());
            }
        });

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIK_CODE){
            //set image to image view
            circle_image_view_profile_pic.setImageURI(data.getData());
            imageFilePath = data.getData();
            hasImage = true;
        }
    }

    @Override
    public void showUserDetails(User user) {
        setLoading(false);

        if(!TextUtils.isEmpty(user.getProfilePicUrl())){
            Glide.with(this)
                    .asBitmap()
                    .load(user.getProfilePicUrl())
                    .centerInside()
                    .into(circle_image_view_profile_pic);
        }

        et_name.setText(user.getName());
        et_address.setText(user.getAddress());
        et_phone.setText(user.getContactNumber());
        et_nic.setText(user.getNic());

        Date date = user.getDob();
        DateFormat targetDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = targetDateFormat.format(date);
        et_dob.setText(formattedDate);
    }

    @Override
    public void onError(String message) {
        showAlertDialog("Error", message,false, FarmnetConstants.OK , (dialog, which) -> {},
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void onSuccess(String message) {
        showAlertDialog("Success", message,false, FarmnetConstants.OK ,
                (dialog, which) -> {
                    finish();
                },
                "", (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }
}
