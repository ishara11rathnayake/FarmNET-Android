package com.industrialmaster.farmnet.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.views.activities.ArticleReaderActivity;

import java.util.List;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "AdvertisementRVAdapter";

    private Context mContext;
    private List<Article> mArticle;

    public ArticleRecyclerViewAdapter(Context mContext, List<Article> mArticle) {
        this.mContext = mContext;
        this.mArticle = mArticle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_article_thumbnail_view, viewGroup,false);
        ArticleRecyclerViewAdapter.ViewHolder holder = new ArticleRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Article article = mArticle.get(i);

        Glide.with(mContext)
                .asBitmap()
                .load(article.getThumbnailUrl())
                .centerInside()
                .into(viewHolder.imgv_article_thumbnail);

        viewHolder.tv_article_title.setText(article.getArticleTitle());
        viewHolder.tv_author_name.setText(article.getUser().getName());

        viewHolder.cardview_abstract_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleReaderActivity.class);
                Gson gson = new Gson();
                String articleString = gson.toJson(mArticle.get(i));
                intent.putExtra("article", articleString);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardview_abstract_article;
        ImageView imgv_article_thumbnail;
        TextView tv_author_name, tv_article_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview_abstract_article = itemView.findViewById(R.id.cardview_abstract_article);
            imgv_article_thumbnail = itemView.findViewById(R.id.imgv_article_thumbnail);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            tv_article_title = itemView.findViewById(R.id.tv_article_title);
        }
    }
}
