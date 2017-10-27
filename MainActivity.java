package com.example.lly.noobbot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;



public class MainActivity extends AppCompatActivity implements AIListener {
    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;
    private TextView inputTextView;
    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = (Button) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        inputTextView = (TextView) findViewById(R.id.inputTextView);
        responseTextView = (TextView) findViewById(R.id.responseTextView);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }

        final AIConfiguration config = new AIConfiguration("0448b0aebad044b58622af58b06a91a5",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }


    @Override
    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }
        final String speech = result.getFulfillment().getSpeech();
        // Show results in TextView.
        resultTextView.setText("Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString +
                "\nSpeech: " + speech
        );
        inputTextView.setText("Query:" + result.getResolvedQuery());
        responseTextView.setText("Speech: " + speech);
    }

    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onListeningStarted() {}

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {}

    @Override
    public void onAudioLevel(final float level) {}


}

