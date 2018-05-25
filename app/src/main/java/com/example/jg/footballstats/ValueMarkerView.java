package com.example.jg.footballstats;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class ValueMarkerView extends MarkerView {
    private TextView mTextView;
    private MPPointF mOffset = null;

    public ValueMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        mTextView = findViewById(R.id.marker_text_view);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mTextView.setText("" + e.getY());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return mOffset == null ? new MPPointF(-getX() - 6, -getHeight()) : mOffset;
    }
}
