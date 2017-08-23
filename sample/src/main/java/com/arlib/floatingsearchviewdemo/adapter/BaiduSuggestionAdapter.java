package com.arlib.floatingsearchviewdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchviewdemo.data.StringSuggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eleven on 2017/2/10.
 */

public class BaiduSuggestionAdapter {
    private final static String TAG = BaiduSuggestionAdapter.class.getSimpleName();

    private Context mContext;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mURL = "https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su?wd=";
    private OnBaiduSuggestionListener mOnBaiduSuggestionListener;

    public BaiduSuggestionAdapter(Context context) {
        mContext = context;
    }

    public interface OnBaiduSuggestionListener {
        void onGetSuggestion(String q, List<StringSuggestion> s);
    }

    public void query(final String q, final OnBaiduSuggestionListener onBaiduSuggestionListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(mURL + q).build();
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    String content = response.body().string();
                    Log.d(TAG, content);

                    List<StringSuggestion> suggestions = new ArrayList<StringSuggestion>();

                    String begin = "(";
                    String end = ");";
                    int startIndex = content.indexOf(begin);
                    int endIndex = content.indexOf(end);
                    String jsonString = content.substring(startIndex + 1, endIndex);
                    Log.d(TAG, jsonString);
                    try {
                        JSONObject json = new JSONObject(jsonString);
                        JSONArray s = json.optJSONArray("s");
                        if (s != null) {
                            suggestions = new ArrayList<StringSuggestion>();
                            for (int i = 0; i < s.length(); i++) {
                                suggestions.add(new StringSuggestion(s.getString(i)));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final List<StringSuggestion> arr = suggestions;

                    if (onBaiduSuggestionListener != null) {
                        Activity activity = (Activity)mContext;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBaiduSuggestionListener.onGetSuggestion(q, arr);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public OnBaiduSuggestionListener getOnBaiduSuggestionListener() {
        return mOnBaiduSuggestionListener;
    }

    public void setOnBaiduSuggestionListener(OnBaiduSuggestionListener onBaiduSuggestionListener) {
        mOnBaiduSuggestionListener = onBaiduSuggestionListener;
    }
}
