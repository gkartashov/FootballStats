package com.example.jg.footballstats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jg.footballstats.stats.Statistics;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private class RefreshingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initializePieChart(mStatistics.getWin(),mStatistics.getLoss(),mStatistics.calculateWPRatio());
                    initializeCoefficientChart();
                    mReturnedBets.setText(Long.toString(mStatistics.getReturned()));
                    mTotalBets.setText(Integer.toString(mStatistics.getTotal()));
                    mInPlayView.setText(Long.toString(mStatistics.getInPlay()));
                    mRoiView.setText(Double.toString(mStatistics.calculateROI(100))+ '%');
                    mYieldView.setText(Double.toString(mStatistics.calculateYield(100))+ '%');
                    break;
                default:
                    break;
            }
        }
    }
    private Toolbar mToolbar;
    private PieChart mPieChart;
    private LineChart mCoefficientChart;
    private TextView mProfileUsername;
    private View mProgressView;
    private ScrollView mProfileFormView;
    private ProfileRefreshAsyncTask mProfileRefreshAsyncTask;
    private RefreshingHandler mRefreshingHandler = new RefreshingHandler();
    private Statistics mStatistics;
    private TextView mInPlayView, mRoiView, mYieldView, mTotalBets, mReturnedBets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mToolbar = findViewById(R.id.inner_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("User stats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReturnedBets = findViewById(R.id.profile_returned);
        mTotalBets = findViewById(R.id.profile_total_bets);
        mInPlayView = findViewById(R.id.profile_in_play);
        mRoiView = findViewById(R.id.profile_roi);
        mYieldView = findViewById(R.id.profile_yield);

        mProfileFormView = findViewById(R.id.profile_stats_view);
        mProgressView = findViewById(R.id.profile_progress_bar);

        mProfileUsername = findViewById(R.id.profile_username);
        mProfileUsername.setText(Constants.USER.getUsername());

        refreshProfile();
    }

    private void refreshProfile() {
        showProgress(true);
        mProfileRefreshAsyncTask = new ProfileRefreshAsyncTask();
        mProfileRefreshAsyncTask.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mProfileFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void initializePieChart(double win, double loss, double ratio) {
        mPieChart = findViewById(R.id.profile_pie_wl_ratio);
        mPieChart.setCenterText("W/L ratio:\n" + Double.toString(ratio));
        mPieChart.setCenterTextSize(16f);
        mPieChart.setDragDecelerationFrictionCoef(0.2f);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setCenterTextColor(getColor(android.R.color.primary_text_dark));
        mPieChart.setHoleColor(getColor(R.color.primaryLightColor));
        mPieChart.setHoleRadius(44f);
        mPieChart.setTransparentCircleColor(Color.BLACK);
        mPieChart.setTransparentCircleRadius(47f);
        mPieChart.setRotationAngle(25);
        mPieChart.setDrawCenterText(true);
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.setEntryLabelTextSize(14f);
        mPieChart.setDrawEntryLabels(true);
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float)win,"Won"));
        entries.add(new PieEntry((float)loss,"Lost"));
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
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mPieChart.invalidate();
    }


    private void initializeCoefficientChart() {
        mCoefficientChart = findViewById(R.id.profile_coefficient_chart);
        ArrayList<Entry> e1 = new ArrayList<>();

        List<Double> coefs = mStatistics.getCoefficients();
        for (int i = 0; i < coefs.size(); i++) {
            e1.add(new Entry(i, (float)((double)coefs.get(i))));
        }

        LineDataSet d1 = new LineDataSet(e1, "Coefficients");
        d1.setLineWidth(1.5f);
        d1.setCircleRadius(2.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        List<Double> realCoefs = mStatistics.getRealCoefficients();
        for (int i = 0; i < realCoefs.size(); i++) {

            e2.add(new Entry(i, (float)((double)realCoefs.get(i))));
        }

        LineDataSet d2 = new LineDataSet(e2, "Real coefficients");
        d2.setLineWidth(1.5f);
        d2.setCircleRadius(2.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);

        mCoefficientChart.getDescription().setText("Mean coefficient: ");
        mCoefficientChart.setData(cd);
        mCoefficientChart.setVisibleXRange(15f,15f);
        mCoefficientChart.moveViewToX(e2.size()-15);
        mCoefficientChart.animateX(1400, Easing.EasingOption.EaseInOutQuad);
        mCoefficientChart.getData().setHighlightEnabled(!mCoefficientChart.getData().isHighlightEnabled());
        mCoefficientChart.invalidate();

    }
    private class ProfileRefreshAsyncTask extends AsyncTask<Void, Void, Void> {
        private String mMessage = "";

        @Override
        protected Void doInBackground(Void... voids) {
            Response response = null;
            try {
                response = DatabaseAPIController.getInstance().getAPI().getStats(Constants.USER.getUsername()).execute();
            } catch (IOException e) {
                mMessage += e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1);
            }
            if (response != null)
                if(response.body() != null) {
                    mStatistics = (Statistics) response.body();
                }
                else if (response.code() == 403)
                    mMessage = getString(R.string.error_invalid_credentials);
                else
                    mMessage = getString(R.string.error_server_unreachable);

            return null;
        }

        @Override
        protected void onPostExecute(final Void nothing) {
            if (mStatistics != null) {
                mRefreshingHandler.sendEmptyMessage(0);
            } else
                Snackbar.make(mProfileFormView,mMessage,Snackbar.LENGTH_LONG).show();
            mProfileRefreshAsyncTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mProfileRefreshAsyncTask = null;
            mStatistics = null;
            showProgress(false);
        }
    }
}
