package com.sumit.feedbackanalyzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sumit.feedbackanalyzer.api.MicrosoftCognitiveAPI;
import com.sumit.feedbackanalyzer.model.AnalysisResult;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFeedback;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private TextView textViewInputFeedback, textViewSentimentScore, textViewDetectedTopic, textViewKeyPhrases;
    private WebView webViewInputFeedback;

    private PieChart pieChart;
    private Context context;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    private AlertDialog aboutDialog;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initViews();
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        editTextFeedback = (EditText) findViewById(R.id.edit_feedback);
        bottomSheet = findViewById(R.id.result_bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        webViewInputFeedback = (WebView) findViewById(R.id.webview_input_feedback);
        pieChart = (PieChart) findViewById(R.id.sentiment_chart);
        setPieChartViewProperties();

        ratingBar = (RatingBar) findViewById(R.id.feedback_ratingbar);

        textViewInputFeedback = (TextView) findViewById(R.id.text_input_feedback);
        textViewSentimentScore = (TextView) findViewById(R.id.text_sentiment_score);
        textViewDetectedTopic = (TextView) findViewById(R.id.text_topic);
        textViewKeyPhrases = (TextView) findViewById(R.id.text_key_phrases);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Setup Event Handlers when UI is visible to user

        setUpEventHandlers();

    }

    private void setUpEventHandlers() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (StringUtils.isEmpty(editTextFeedback.getText().toString())) {
                    Snackbar.make(view, "Please enter feedback then tap on Analyze button.", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS", null).show();
                } else {
                    initFeedbackAnalysis(editTextFeedback.getText().toString());
                }

            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);
                    fab.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                fab.setVisibility(View.GONE);
            }
        });

    }

    // Enable/Disable progress dialog

    private void toggleProgressBar(boolean isShow) {

        if (progressDialog != null && isShow && !progressDialog.isShowing()) {
            progressDialog.show();
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

    }

    // Set Pie Chart view properties

    private void setPieChartViewProperties() {

        // configure pie chart
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(20);
        pieChart.setHoleColor(ContextCompat.getColor(context, R.color.transparent));
        pieChart.setMinimumHeight(600);
        pieChart.setMinimumWidth(600);

        pieChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setDrawSlicesUnderHole(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pieChart.setElevation(5);
        }

    }

    // Set Pie Chat data from AnalysisResult Model

    private void setPieChartData(AnalysisResult analysisResult) {

        float sentimentScore = analysisResult.getSentimentScore();

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(sentimentScore, "Sentiment Score"));
        entries.add(new PieEntry(100 - sentimentScore, ""));

        PieDataSet dataSet = new PieDataSet(entries, "Remaining");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(true);

        pieChart.setData(data);
        // Undo all highlights
        pieChart.highlightValues(null);
        // Update Pie Chart
        pieChart.invalidate();

        animatePieChart();
    }

    private void animatePieChart() {
        pieChart.clearAnimation();
        pieChart.animateXY(4000, 4000);
    }

    // Set results from AnalysisResult model

    private void setAnalysisResultDetails(AnalysisResult analysisResult) {

        textViewInputFeedback.setText("Entered Feedback : ");

        // Setup WebView

        String inputFeedback = editTextFeedback.getText().toString();
        ArrayList<String> keyPhrasesList = analysisResult.getKeyPhrases();

        // Prepare web body content and highlight key phrases by setting a background color
        // Replace the old feedback text with updated background tag and set it in webview

        if (keyPhrasesList != null && keyPhrasesList.size() > 0) {

            String webContentToLoad = "<html><body style=\"background:#795548;\"><p style=\"color:#ffffff;\">";

            for (String keyPhrase : keyPhrasesList) {
                inputFeedback = StringUtils.replace(inputFeedback, keyPhrase, "<span style=\"background:#FF5722;\">" + keyPhrase + "</span>");
            }

            webContentToLoad += inputFeedback;

            webContentToLoad += "</html></body></p>";

            webViewInputFeedback.loadData(webContentToLoad, "text/html", "UTF-8");

        }

        textViewSentimentScore.setText("Sentiment Score : " + analysisResult.getSentimentScore() + "%");

        float sentimentScore = analysisResult.getSentimentScore();

        // Prepare rating. Total stars = 5, 1 being least and 5 is max

        if (sentimentScore > 85) {
            ratingBar.setRating(5);
        } else if (sentimentScore <= 85 && sentimentScore > 60) {
            ratingBar.setRating(4);
        } else if (sentimentScore <= 60 && sentimentScore > 40) {
            ratingBar.setRating(3);
        } else if (sentimentScore <= 40 && sentimentScore > 20) {
            ratingBar.setRating(2);
        } else if (sentimentScore <= 20) {
            ratingBar.setRating(1);
        }

        if (StringUtils.isNotEmpty(analysisResult.getDetectedTopic())) {
            textViewDetectedTopic.setText("Detected Topic : " + analysisResult.getDetectedTopic());
        } else {
            textViewDetectedTopic.setVisibility(View.GONE);
        }

        String keyPhrases = null;

        if (keyPhrasesList != null && keyPhrasesList.size() > 0) {
            for (String phrase : analysisResult.getKeyPhrases()) {
                if (StringUtils.isEmpty(keyPhrases)) {
                    keyPhrases = phrase;
                } else {
                    keyPhrases += ", " + phrase;
                }
            }

            if (keyPhrasesList != null && keyPhrasesList.size() > 0)
                textViewKeyPhrases.setText("Key Phrases : " + keyPhrases);
            else textViewKeyPhrases.setVisibility(View.GONE);
        }

    }

    // Init Async API call and update UI if response is valid

    private void initFeedbackAnalysis(String feedbackQuery) {
        new AsyncTask<String, Void, AnalysisResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toggleProgressBar(true);
            }

            @Override
            protected AnalysisResult doInBackground(String... params) {

                if (!Utils.isNetworkAvailable(context))
                    return null;

                AnalysisResult analysisResult = new AnalysisResult();

                float sentimentScore = MicrosoftCognitiveAPI.detectSentiment(params[0]);
                analysisResult.setKeyPhrases(MicrosoftCognitiveAPI.detectKeyPhrases(params[0]));

                analysisResult.setSentimentScore(sentimentScore);

                if (sentimentScore > 0)
                    return analysisResult;
                else return null;
            }

            @Override
            protected void onPostExecute(AnalysisResult analysisResult) {
                super.onPostExecute(analysisResult);

                toggleProgressBar(false);

                if (analysisResult != null) {
                    setPieChartData(analysisResult);
                    setAnalysisResultDetails(analysisResult);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.unable_to_search), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.dismiss), null).show();
                }
            }
        }.execute(feedbackQuery);
    }

    // Show About App Dialog

    private void showAboutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.about_dialog_title));
        alertDialogBuilder.setMessage(getString(R.string.about_dev));

        alertDialogBuilder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        aboutDialog = alertDialogBuilder.create();
        aboutDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        toggleProgressBar(false);

        if (aboutDialog != null) {
            aboutDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup progress bar

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getString(R.string.wait_message));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {

            editTextFeedback.setText("");
            pieChart.clearAnimation();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}
