package com.example.jg.footballstats;

import android.view.View;

public interface ITapListener {
    void onTap(View view, int position);
    void onLongTap(View view, int position);
}
