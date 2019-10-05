package com.industrialmaster.farmnet.views.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Article;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleReaderActivity extends AppCompatActivity {

    ImageButton mCloseImageButton;

    TextView mArticleTitleTextView;
    TextView mAuthorNameTextView;
    TextView mDateTextView;
    CircleImageView mProfileImageCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_reader);

        mArticleTitleTextView = findViewById(R.id.tv_article_title);
        mAuthorNameTextView = findViewById(R.id.tv_name);
        mDateTextView= findViewById(R.id.tv_date);
        mProfileImageCircleImageView = findViewById(R.id.cimageview_profilepic);
        mCloseImageButton = findViewById(R.id.img_btn_close);

        Gson gson = new Gson();
        Article article = gson.fromJson(getIntent().getStringExtra("article"), Article.class);

        mArticleTitleTextView.setText(article.getArticleTitle());
        mAuthorNameTextView.setText(article.getUser().getName());

        Date date = article.getDate();
        DateFormat targetDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = targetDateFormat.format(date);
        mDateTextView.setText(formattedDate);

        if(!TextUtils.isEmpty(article.getUser().getProfilePicUrl())){
            Glide.with(this)
                    .asBitmap()
                    .load(article.getUser().getProfilePicUrl())
                    .centerCrop()
                    .into(mProfileImageCircleImageView);
        }

        Editor renderer= findViewById(R.id.renderer);

        String mSerialized = article.getContent();

        renderer.setDividerLayout(R.layout.tmpl_divider_layout);
        renderer.setEditorImageLayout(R.layout.tmpl_image_view);
        renderer.setListItemLayout(R.layout.tmpl_list_item);
        String content= mSerialized;
        EditorContent deserialized = renderer.getContentDeserialized(content);
        renderer.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                /**
                 * override methods
                 * **/
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                /**
                 * override methods
                 * **/
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });
        renderer.render(deserialized);

        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
