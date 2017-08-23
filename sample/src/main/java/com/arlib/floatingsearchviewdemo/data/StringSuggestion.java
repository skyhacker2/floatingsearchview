package com.arlib.floatingsearchviewdemo.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by eleven on 2017/2/10.
 */

public class StringSuggestion implements SearchSuggestion {

    private String mString;

    public StringSuggestion(String string) {
        mString = string;
    }

    public StringSuggestion(Parcel source) {
        mString = source.readString();
    }

    public static final Creator<StringSuggestion> CREATOR = new Creator<StringSuggestion>() {
        @Override
        public StringSuggestion createFromParcel(Parcel in) {
            return new StringSuggestion(in);
        }

        @Override
        public StringSuggestion[] newArray(int size) {
            return new StringSuggestion[size];
        }
    };

    @Override
    public String getBody() {
        return mString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mString);
    }
}
