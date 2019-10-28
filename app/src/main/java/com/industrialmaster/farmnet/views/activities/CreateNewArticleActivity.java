package com.industrialmaster.farmnet.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.request.CreateNewArticleRequest;
import com.industrialmaster.farmnet.presenters.ArticlePresenter;
import com.industrialmaster.farmnet.presenters.ArticlePresenterImpl;
import com.industrialmaster.farmnet.utils.ErrorMessageHelper;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CreateArticleView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.defaults.colorpicker.ColorPickerPopup;

public class CreateNewArticleActivity extends BaseActivity implements CreateArticleView {

    private static final String TAG = "NewArticleActivity";
    ImageButton mCloseButton;
    Button mNewArticleButton;
    Editor editor;
    EditText mArticleTitleEditText;

    ArticlePresenter articlePresenter;

    String realFilePath;
    String mUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_article);

        articlePresenter = new ArticlePresenterImpl(this, this);

        editor = findViewById(R.id.editor);
        mNewArticleButton = findViewById(R.id.btn_create_new_article);
        mArticleTitleEditText = findViewById(R.id.et_article_title);

        mCloseButton = findViewById(R.id.img_btn_close);
        mCloseButton.setOnClickListener(v -> {
            String message = ErrorMessageHelper.DISCARD_CONFIRMATION;
            showSweetAlert(SweetAlertDialog.WARNING_TYPE, message,null,false, FarmnetConstants.OK ,
                    sDialog -> finish(),FarmnetConstants.CANCEL, SweetAlertDialog::dismissWithAnimation);
        });

        findViewById(R.id.action_h1).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.H1));

        findViewById(R.id.action_h2).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.H2));

        findViewById(R.id.action_h3).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.H3));

        findViewById(R.id.action_bold).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.BOLD));

        findViewById(R.id.action_Italic).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.ITALIC));

        findViewById(R.id.action_indent).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.INDENT));

        findViewById(R.id.action_outdent).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.OUTDENT));

        findViewById(R.id.action_bulleted).setOnClickListener(v -> editor.insertList(false));

        findViewById(R.id.action_color).setOnClickListener(v -> new ColorPickerPopup.Builder(CreateNewArticleActivity.this)
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        Toast.makeText(CreateNewArticleActivity.this, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                        editor.updateTextColor(colorHex(color));
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                }));

        findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> editor.insertList(true));

        findViewById(R.id.action_hr).setOnClickListener(v -> editor.insertDivider());

        findViewById(R.id.action_insert_image).setOnClickListener(v -> editor.openImagePicker());

        findViewById(R.id.action_insert_link).setOnClickListener(v -> editor.insertLink());


        findViewById(R.id.action_erase).setOnClickListener(v -> editor.clearAllContents());

        findViewById(R.id.action_blockquote).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));

        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);

        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                /**
                 * functions when onTextChanged
                 * **/
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(CreateNewArticleActivity.this, uuid, Toast.LENGTH_LONG).show();

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), image, "Title", null);
                Uri uri = Uri.parse(path);

                realFilePath = convertMediaUriToPath(uri);

                mUuid = uuid;
                articlePresenter.getThumbnailUrl(realFilePath);

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return getLayoutInflater().inflate(R.layout.layout_authored_by, null);
            }

        });

        editor.render();

        mNewArticleButton.setOnClickListener(v -> {
            String content = editor.getContentAsSerialized();
            String articleTitle = mArticleTitleEditText.getText().toString();

            CreateNewArticleRequest createNewArticleRequest = new CreateNewArticleRequest();
            createNewArticleRequest.setContent(content);
            createNewArticleRequest.setArticleTitle(articleTitle);

            setLoading(true);
            articlePresenter.createNewArticle(createNewArticleRequest);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
//            realFilePath = uri.getPath();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d(TAG, String.valueOf(bitmap));
                editor.insertImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.toString());
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
//             editor.RestoreState();
        }
    }

    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }

    private View insertMacro(String autherName) {
        View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
        Map<String, Object> map = new HashMap<>();
        map.put("author-name", autherName);
        map.put("date", new Date());
        editor.insertMacro("author-tag",view, map);
        return view;
    }

    @Override
    public void onSuccess(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Great!" ,message,false, FarmnetConstants.OK ,
                sDialog -> finish(), null, null);
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , message,false, FarmnetConstants.OK ,
                SweetAlertDialog::dismissWithAnimation, null, null);
    }

    @Override
    public void onThumbnailUploadComplete(String url) {
        editor.onImageUploadComplete(url, mUuid);
    }

    @Override
    public void showMessage(String message) {

    }

}
