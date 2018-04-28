package com.example.jg.footballstats;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.ChartView;

public class InnerActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);
        toolbar = findViewById(R.id.inner_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("caption"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChartView chartView = findViewById(R.id.linechart);
        chartView.addData(new LineSet(new String[] {"midery", "bratan", "gera", "zhopp"}, new float[] {0.0f,2.0f,18.0f,-20.0f}));
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        chartView.setValueThreshold(0, 0, p);
        chartView.show();

        ChartView barChartView = findViewById(R.id.barchart);
        BarSet barSet = new BarSet();
        barSet.addBar(new Bar("midery", 10.0f));
        barSet.addBar(new Bar("bratan", 20.0f));
        barSet.addBar(new Bar("gera", 40.0f));
        barSet.addBar(new Bar("zhopp", -20.0f));
        barSet.getEntry(0).setColor(Color.RED);
        barSet.getEntry(1).setColor(Color.GREEN);
        barSet.getEntry(2).setColor(Color.BLUE);
        barSet.getEntry(3).setColor(Color.GRAY);
        barChartView.addData(barSet);
        barChartView.show();

        BarSet stackedBarSet = new BarSet();
        stackedBarSet.addBar(new Bar("midery", 10.0f));
        stackedBarSet.addBar(new Bar("bratan", 20.0f));
        stackedBarSet.addBar(new Bar("gera", 40.0f));
        stackedBarSet.addBar(new Bar("zhopp", -20.0f));
        ChartView stackedBarChartView = findViewById(R.id.stackedbarchart);
        stackedBarChartView.addData(stackedBarSet);
        stackedBarChartView.show();

    }
}
