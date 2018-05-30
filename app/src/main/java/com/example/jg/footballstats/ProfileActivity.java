package com.example.jg.footballstats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private class RefreshingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initializeStatistics();
                    initializePieChart(mStatistics.getWin(),mStatistics.getLoss(),mStatistics.calculateWPRatio());
                    initializeCoefficientChart();
                    initializeIncomeChart();
                    break;
                default:
                    break;
            }
        }
    }

    private Toolbar mToolbar;
    private PieChart mPieChart;
    private LineChart mCoefficientChart, mIncomeChart;
    private TextView mProfileUsername;
    private View mProgressView;
    private ScrollView mProfileFormView;
    private ProfileRefreshAsyncTask mProfileRefreshAsyncTask;
    private RefreshingHandler mRefreshingHandler = new RefreshingHandler();
    private Statistics mStatistics;
    private TextView mInPlayView, mRoiView, mYieldView, mTotalBets, mReturnedBets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Constants.IS_THEME_DARK ? R.style.AppTheme : R.style.AppTheme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mToolbar = findViewById(R.id.inner_activity_toolbar);
        mToolbar.setBackgroundColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryColorDark : R.color.primaryLightColorLight));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("User stats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(Constants.IS_THEME_DARK ? R.drawable.ic_arrow_left_white : R.drawable.ic_arrow_left_black);

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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void initializeStatistics() {
        mReturnedBets.setText(Long.toString(mStatistics.getReturned()));
        mTotalBets.setText(Integer.toString(mStatistics.getTotal()));
        mInPlayView.setText(Long.toString(mStatistics.getInPlay()));
        mRoiView.setText(Double.toString(mStatistics.calculateROI(100))+ '%');
        mYieldView.setText(Double.toString(mStatistics.calculateYield(100))+ '%');
    }

    private void initializePieChart(double win, double loss, double ratio) {
        mPieChart = findViewById(R.id.profile_pie_wl_ratio);
        mPieChart.setCenterText("W/L ratio:\n" + Double.toString(ratio));
        mPieChart.setCenterTextSize(16f);
        mPieChart.setDragDecelerationFrictionCoef(0.2f);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setCenterTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
        mPieChart.setHoleColor(Color.parseColor("#00FFFFFF"));
        //mPieChart.setHoleColor(getColor(R.color.primaryLightColorDark));
        mPieChart.setHoleRadius(44f);
        mPieChart.setTransparentCircleColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorLight : R.color.primaryTextColorDark));
        mPieChart.setTransparentCircleRadius(47f);
        mPieChart.setRotationAngle(25);
        mPieChart.setDrawCenterText(true);
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.setEntryLabelTextSize(14f);
        mPieChart.setDrawEntryLabels(true);
        mPieChart.setEntryLabelColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
        mPieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float)win,"Won"));
        entries.add(new PieEntry((float)loss,"Lost"));
        PieDataSet dataSet1 = new PieDataSet(entries,"W/L ratio");
        dataSet1.setSliceSpace(2f);
        dataSet1.setSelectionShift(5f);
        dataSet1.setColors(getColor(R.color.winColor), getColor(R.color.lossColor));
        dataSet1.setValueLinePart1OffsetPercentage(70f);
        dataSet1.setValueLineColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
        dataSet1.setValueLinePart1Length(0.3f);
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
        ArrayList<Entry> e1 = new ArrayList<>();

        List<Double> coefs = mStatistics.getCoefficients();
        if (coefs != null) {
            if (coefs.size() == 0) {
                coefs.add(0.0);
                e1.add(new Entry(0, 0));
            }
            else
                for (int i = 0; i < coefs.size(); i++)
                    e1.add(new Entry(i + 1, (float) ((double) coefs.get(i))));

            LineDataSet d1 = new LineDataSet(e1, "Coefficients");
            d1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            d1.setLineWidth(1.5f);
            d1.setColor(Constants.IS_THEME_DARK ? ColorTemplate.VORDIPLOM_COLORS[0] : getColor(R.color.chartCoeffsColorLight));
            d1.setCircleColor(Constants.IS_THEME_DARK ? ColorTemplate.VORDIPLOM_COLORS[0] : getColor(R.color.chartCoeffsColorLight));
            d1.setCircleRadius(2.0f);
            d1.setHighLightColor(Color.rgb(244, 117, 117));
            d1.setDrawValues(false);
            d1.setDrawHighlightIndicators(false);

            ArrayList<ILineDataSet> sets = new ArrayList<>();
            sets.add(d1);

            LineData cd = new LineData(sets);

            mCoefficientChart = findViewById(R.id.profile_coefficient_chart);

            mCoefficientChart.setData(cd);

            ValueMarkerView mv = new ValueMarkerView(this, R.layout.value_marker_layout);
            mv.setChartView(mCoefficientChart);

            mCoefficientChart.setMarker(mv);

            mCoefficientChart.getXAxis().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mCoefficientChart.getAxisLeft().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mCoefficientChart.getAxisRight().setEnabled(false);

            mCoefficientChart.getDescription().setText("Mean coefficient: " + Double.toString(mStatistics.calculateAverageCoefficient()));
            mCoefficientChart.getDescription().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mCoefficientChart.getDescription().setTextSize(10f);

            float maxCoef = (float) (double) Collections.max(coefs);
            LimitLine maxLimit = new LimitLine(maxCoef, "Max coefficient: " + Float.toString(maxCoef));
            maxLimit.setLineWidth(2f);
            maxLimit.setLineColor(getColor(R.color.winColor));
            maxLimit.enableDashedLine(10f, 10f, 0f);
            maxLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            maxLimit.setTextSize(10f);
            maxLimit.setTextStyle(Paint.Style.FILL);
            maxLimit.setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));

            mCoefficientChart.getAxisLeft().addLimitLine(maxLimit);
            mCoefficientChart.getLegend().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mCoefficientChart.getLegend().setTextSize(10f);

            if (!Constants.IS_THEME_DARK) {
                mCoefficientChart.setDrawGridBackground(true);
                mCoefficientChart.setGridBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.primaryColorDark), 40));
            }

            if (coefs.size() > 10) {
                mCoefficientChart.zoom(8f, 1f, e1.get(e1.size() - 5).getX(), e1.get(e1.size() - 5).getY());
                mCoefficientChart.moveViewToX(e1.size() - 11);
            }

            mCoefficientChart.animateX(1400, Easing.EasingOption.EaseInOutQuad);
            mCoefficientChart.invalidate();
        }
    }

    private void initializeIncomeChart() {
        ArrayList<Entry> e1 = new ArrayList<>();

        List<Double> income = mStatistics.calculateIncome(10000,100);
        if (income.size() > 0) {
            for (int i = 0; i < income.size(); i++) {
                e1.add(new Entry(i, (float) ((double) income.get(i))));
            }

            LineDataSet d1 = new LineDataSet(e1, "Income");
            d1.setMode(LineDataSet.Mode.LINEAR);
            d1.setColor(Constants.IS_THEME_DARK ? ColorTemplate.VORDIPLOM_COLORS[3] : getColor(R.color.chartIncomeColorLight));
            d1.setLineWidth(1.5f);
            d1.setDrawCircles(false);
            d1.setCircleRadius(2.0f);
            d1.setHighLightColor(Color.rgb(244, 117, 117));
            d1.setDrawValues(false);
            d1.setDrawHighlightIndicators(false);

            ArrayList<ILineDataSet> sets = new ArrayList<>();
            sets.add(d1);

            LineData cd = new LineData(sets);

            mIncomeChart = findViewById(R.id.profile_income_chart);

            mIncomeChart.setData(cd);

            ValueMarkerView mv = new ValueMarkerView(this, R.layout.value_marker_layout);
            mv.setChartView(mIncomeChart);

            mIncomeChart.setMarker(mv);

            mIncomeChart.getXAxis().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mIncomeChart.getAxisLeft().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mIncomeChart.getAxisRight().setEnabled(false);

            float currentBalance = (float) (double) income.get(income.size()-1);

            mIncomeChart.getDescription().setEnabled(true);
            mIncomeChart.getDescription().setText("Current balance: " + Float.toString(currentBalance));
            mIncomeChart.getDescription().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mIncomeChart.getDescription().setTextSize(10f);

            float maxIncome = (float) (double) Collections.max(income);
            LimitLine maxLimit = new LimitLine(maxIncome, Float.toString(maxIncome));
            maxLimit.setLineWidth(1f);
            maxLimit.setLineColor(getColor(R.color.winColor));
            maxLimit.enableDashedLine(10f, 10f, 0f);
            maxLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            maxLimit.setTextSize(10f);
            maxLimit.setTextStyle(Paint.Style.FILL);
            maxLimit.setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));

            float minIncome = (float) (double) Collections.min(income);
            LimitLine minLimit = new LimitLine(minIncome, Float.toString(minIncome));
            minLimit.setLineWidth(1f);
            minLimit.setLineColor(getColor(R.color.lossColor));
            minLimit.enableDashedLine(10f, 10f, 0f);
            minLimit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            minLimit.setTextSize(10f);
            minLimit.setTextStyle(Paint.Style.FILL);
            minLimit.setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));

            float startBalance = (float) (double) income.get(0);
            LimitLine startLimit = new LimitLine(startBalance);
            startLimit.setLineWidth(1f);
            startLimit.setLineColor(Color.parseColor("#44FFFFFF"));
            //startLimit.enableDashedLine(10f, 10f, 0f);
            startLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            startLimit.setTextSize(10f);
            startLimit.setTextStyle(Paint.Style.FILL);
            startLimit.setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));

            LimitLine currentLimit = new LimitLine(currentBalance);
            currentLimit.setLineWidth(1f);
            currentLimit.setLineColor(ColorUtils.setAlphaComponent(ColorTemplate.VORDIPLOM_COLORS[1],100));
            //startLimit.enableDashedLine(10f, 10f, 0f);
            currentLimit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            currentLimit.setTextSize(10f);
            currentLimit.setTextStyle(Paint.Style.FILL);
            currentLimit.setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));

            mIncomeChart.getAxisLeft().addLimitLine(maxLimit);
            mIncomeChart.getAxisLeft().addLimitLine(minLimit);
            mIncomeChart.getAxisLeft().addLimitLine(startLimit);
            mIncomeChart.getAxisLeft().addLimitLine(currentLimit);

            mIncomeChart.getLegend().setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
            mIncomeChart.getLegend().setTextSize(10f);

            if (!Constants.IS_THEME_DARK) {
                mIncomeChart.setDrawGridBackground(true);
                mIncomeChart.setGridBackgroundColor(ColorUtils.setAlphaComponent(getColor(R.color.primaryColorDark), 40));
            }


            mIncomeChart.animateX(1000, Easing.EasingOption.EaseInOutQuad);
            mIncomeChart.invalidate();
        }
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
                Snackbar.make(mProfileFormView,mMessage, Snackbar.LENGTH_LONG).show();
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
