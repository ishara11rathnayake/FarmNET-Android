package com.industrialmaster.farmnet.views.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.industrialmaster.farmnet.R;

import java.util.Map;

public class ArticleReaderActivity extends AppCompatActivity {

    private String mSerialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_reader);

        Editor renderer= findViewById(R.id.renderer);

        mSerialized = getIntent().getStringExtra("article");

        renderer.setDividerLayout(R.layout.tmpl_divider_layout);
        renderer.setEditorImageLayout(R.layout.tmpl_image_view);
        renderer.setListItemLayout(R.layout.tmpl_list_item);
        String content= mSerialized;
        EditorContent Deserialized= renderer.getContentDeserialized(content);
        renderer.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {

            }

            @Override
            public void onUpload(Bitmap image, String uuid) {

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });
        renderer.render(Deserialized);
    }
}
