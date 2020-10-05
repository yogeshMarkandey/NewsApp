package com.example.newsapp.util;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class NPALinerLayoutManager extends LinearLayoutManager {

    public NPALinerLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public NPALinerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
}
