package com.social.dialogflowtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {

    AIService aiService;
    TextView t;
    TextToSpeech textToSpeech;
    private ListView chat_view;
    ArrayAdapter<String> adapter;
    private ListView chatbot_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t= (TextView) findViewById(R.id.textView);
        chatbot_view = (ListView)findViewById(R.id.chatbot_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chatbot_view.setAdapter(adapter);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }

        final AIConfiguration config = new AIConfiguration("d3f41084aac04fafaa76b5edefb3b60d",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
           if(status != TextToSpeech.ERROR){
               textToSpeech.setLanguage(Locale.US);
           }
            }
        });

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

    public void buttonClicked(View view){
        aiService.startListening();
    }
    @SuppressLint("ResourceType")
    @Override
    public void onResult(AIResponse result) {
        Result result1=result.getResult();

        t.setText("Query "+result1.getResolvedQuery()+" action: "+result1.getAction()+"풀필먼트  "+result1.getFulfillment().getSpeech() );
//View c;
//c= findViewById(android.R.id.text1);

//        Chatbot chatbot = new Chatbot(result1.getResolvedQuery());
        if(!result1.getFulfillment().getSpeech().equals("Sorry, can you say that again?")) {
//            c.parentView.setGravity(Gravity.LEFT);
            adapter.add(result1.getResolvedQuery());

            adapter.add(result1.getFulfillment().getSpeech());
        }
        textToSpeech.speak(result1.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

}
