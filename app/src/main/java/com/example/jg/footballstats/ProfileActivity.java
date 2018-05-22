package com.example.jg.footballstats;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PieChart mPieChart;
    private TextView mProfileUsername, mProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.inner_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProfileUsername = findViewById(R.id.profile_username);
        mProfileUsername.setText(Constants.USER.getUsername());

        mProfileEmail = findViewById(R.id.profile_email);
        mProfileEmail.setText(Constants.USER.getEmail());

        mPieChart = findViewById(R.id.profile_pie_wl_ratio);
        mPieChart.setCenterText("W/L ratio");
        mPieChart.setCenterTextSize(16f);
        mPieChart.setDragDecelerationFrictionCoef(0.2f);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setCenterTextColor(getColor(android.R.color.primary_text_dark));
        mPieChart.setHoleColor(getColor(R.color.primaryLightColor));
        mPieChart.setHoleRadius(40f);
        mPieChart.setTransparentCircleColor(Color.BLACK);
        mPieChart.setTransparentCircleRadius(45f);
        mPieChart.setRotationAngle(25);
        mPieChart.setDrawCenterText(true);
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.setEntryLabelTextSize(14f);
        mPieChart.setDrawEntryLabels(true);
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(Constants.USER.getWin(),"Won"));
        entries.add(new PieEntry(Constants.USER.getLoss(),"Lost"));
        PieDataSet dataSet1 = new PieDataSet(entries,"W/L ratio");
        dataSet1.setSliceSpace(2f);
        dataSet1.setSelectionShift(5f);
        dataSet1.setColors(new int[] { getColor(R.color.winColor), getColor(R.color.lossColor)});
        dataSet1.setValueLinePart1OffsetPercentage(70f);
        dataSet1.setValueLinePart1Length(0.2f);
        dataSet1.setValueLinePart2Length(0.8f);
        dataSet1.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(dataSet1);
        pieData.setValueTextSize(14f);
        pieData.setValueTextColor(getColor(android.R.color.primary_text_dark));
        pieData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Integer.toString((int)value);
            }
        });
        mPieChart.setData(pieData);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextColor(getColor(android.R.color.primary_text_dark));
        l.setEnabled(false);

        mPieChart.invalidate();
    }
}
