package com.industrialmaster.farmnet.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Article;
import com.industrialmaster.farmnet.presenters.ArticlePresenter;
import com.industrialmaster.farmnet.presenters.ArticlePresenterImpl;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.ArticleView;
import com.industrialmaster.farmnet.views.adapters.ArticleRecyclerViewAdapter;
import com.industrialmaster.farmnet.views.adapters.DealsPostRecyclerViewAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends BaseFragment implements ArticleView {

    View rootView;

    ArticlePresenter presenter;

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_article, container, false);

        presenter = new ArticlePresenterImpl(getActivity(), ArticleFragment.this);
        presenter.getAllArticles();
        setLoading(true);

        return  rootView;
    }

    @Override
    public void showArticles(List<Article> articles) {
        setLoading(false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_articles);
        ArticleRecyclerViewAdapter adapter = new ArticleRecyclerViewAdapter(getActivity(), articles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onError(String message) {
        setLoading(false);
        showSweetAlert(SweetAlertDialog.ERROR_TYPE, "Oops..." , message,false, FarmnetConstants.OK ,
                SweetAlertDialog::dismissWithAnimation, null, null);
    }

    @Override
    public void showMessage(String message) {

    }

}
