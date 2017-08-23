package com.arlib.floatingsearchviewdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchviewdemo.R;
import com.arlib.floatingsearchviewdemo.adapter.BaiduSuggestionAdapter;
import com.arlib.floatingsearchviewdemo.data.StringSuggestion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionSearchFragment extends BaseExampleFragment implements AppBarLayout.OnOffsetChangedListener{

    private FloatingSearchView mSearchView;
    private AppBarLayout mAppBar;
    private OkHttpClient mHttpClient = new OkHttpClient();
    private BaiduSuggestionAdapter mSuggestionAdapter;

    public SuggestionSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mAppBar = (AppBarLayout) view.findViewById(R.id.appbar);

        mAppBar.addOnOffsetChangedListener(this);

        setupSearchBar();

        mSuggestionAdapter = new BaiduSuggestionAdapter(getActivity());
    }

    private void setupSearchBar() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    getSuggestion(newQuery, new BaiduSuggestionAdapter.OnBaiduSuggestionListener() {
                        @Override
                        public void onGetSuggestion(String q, List<StringSuggestion> s) {
                            mSearchView.swapSuggestions(s);
                            mSearchView.hideProgress();
                        }
                    });
                }
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onFocusCleared() {
                mSearchView.hideProgress();
            }
        });
    }

    private void getSuggestion(String query, BaiduSuggestionAdapter.OnBaiduSuggestionListener listener) {
        mSuggestionAdapter.query(query, listener);
    }

    @Override
    public boolean onActivityBackPress() {
        if (!mSearchView.setSearchFocused(false)) {
            return false;
        }
        return true;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mSearchView.setTranslationY(verticalOffset);
    }
}
