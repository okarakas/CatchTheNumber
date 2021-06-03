package com.afo.sayibulma;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Vibrator;
import com.squareup.seismic.ShakeDetector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener{

    private TextView txt;
    private SpeechRecognizer sr;
    private Intent recognizerIntent;
    private static final String TAG = "voice recognition";
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private int sum;
    private EditText result;
    private TextView trgt;
    private TextView fnsh;
    private TextToSpeech textToSpeech;
    private String words;
    private HashMap<String, String> ttsParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRecordAudioPermission();

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        btn9 = findViewById(R.id.button9);
        result = findViewById(R.id.editText2);
        trgt = findViewById(R.id.textView3);
        fnsh = findViewById(R.id.textView4);

        SensorManager SM=(SensorManager)getSystemService(SENSOR_SERVICE);
        ShakeDetector SD=new ShakeDetector(this);
        SD.start(SM);

        Random r = new Random();
        int random = r.nextInt(45 - 1) + 1;
        trgt.setText(String.valueOf(random));
        sum = 0;

        textToSpeech = new TextToSpeech(this,
                new OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        textToSpeech.setLanguage(Locale.US);
                        textToSpeech.setOnUtteranceCompletedListener(
                                new OnUtteranceCompletedListener() {
                                    @Override
                                    public void onUtteranceCompleted(String id) {
                                        utteranceCompleted();
                                    }
                                }
                        );
                        playFirstMessage();
                    }
                }
        );
    }

    public void listen() {
        sr.startListening(recognizerIntent);
        Log.i(TAG, "Intent sent");
    }

    private void init() {
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new voiceRecognition());
        txt = findViewById(R.id.txt);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.ENGLISH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }
    @Override
    public void hearShake() {
        if (fnsh.getVisibility() == View.VISIBLE) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
            btn5.setVisibility(View.VISIBLE);
            btn6.setVisibility(View.VISIBLE);
            btn7.setVisibility(View.VISIBLE);
            btn8.setVisibility(View.VISIBLE);
            btn9.setVisibility(View.VISIBLE);
            fnsh.setVisibility(View.INVISIBLE);
            Random r = new Random();
            int random = r.nextInt(45 - 1) + 1;
            trgt.setText(String.valueOf(random));
            sum = 0;
            result.setText("0");
            playFirstMessage();
        }
    }

    public class voiceRecognition implements RecognitionListener {
        @Override
        public void onBeginningOfSpeech() {
        }
        @Override
        public void onEndOfSpeech() {
        }
        @Override
        public void onError(int error) {
            Log.e("err", "hata: " + error);
            textToSpeech.speak("Please say a number!", MODE_PRIVATE, ttsParams);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    listen();
                }
            }, 1400);
        }
        @Override
        public void onResults(Bundle results) {
            ArrayList lst = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < lst.size(); i++) {
                txt.setText("Text: " + lst.get(i));
            }

            if (txt.getText().toString().contains("bir") || txt.getText().toString().contains("1") || txt.getText().toString().contains("one")) {
                txt.setText("1");
                if (btn1.getVisibility() == View.VISIBLE) {
                    btn1.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 1);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("iki") || txt.getText().toString().contains("2") || txt.getText().toString().contains("two")) {
                txt.setText("2");
                if (btn2.getVisibility() == View.VISIBLE) {
                    btn2.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 2);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("üç") || txt.getText().toString().contains("3") || txt.getText().toString().contains("three")) {
                txt.setText("3");
                if (btn3.getVisibility() == View.VISIBLE) {
                    btn3.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 3);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("dört") || txt.getText().toString().contains("4") || txt.getText().toString().contains("four")) {
                txt.setText("4");
                if (btn4.getVisibility() == View.VISIBLE) {
                    btn4.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 4);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("beş") || txt.getText().toString().contains("5") || txt.getText().toString().contains("five")) {
                txt.setText("5");
                if (btn5.getVisibility() == View.VISIBLE) {
                    btn5.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 5);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("altı") || txt.getText().toString().contains("6") || txt.getText().toString().contains("six")) {
                txt.setText("6");
                if (btn6.getVisibility() == View.VISIBLE) {
                    btn6.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 6);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("yedi") || txt.getText().toString().contains("7") || txt.getText().toString().contains("seven")) {
                txt.setText("7");
                if (btn7.getVisibility() == View.VISIBLE) {
                    btn7.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 7);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("sekiz") || txt.getText().toString().contains("8") || txt.getText().toString().contains("eight")) {
                txt.setText("8");
                if (btn8.getVisibility() == View.VISIBLE) {
                    btn8.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 8);
                } else {
                    invalid_number();
                }
            } else if (txt.getText().toString().contains("dokuz") || txt.getText().toString().contains("9") || txt.getText().toString().contains("nine")) {
                txt.setText("9");
                if (btn9.getVisibility() == View.VISIBLE) {
                    btn9.setVisibility(View.INVISIBLE);
                    control(Integer.valueOf(trgt.getText().toString()), 9);
                } else {
                    invalid_number();
                }
            } else {
                words = "Number not understood. Please try again.";
                textToSpeech.speak(words, MODE_PRIVATE, ttsParams);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        listen();
                    }
                }, 2500);
            }
        }
        @Override
        public void onReadyForSpeech(Bundle params) {
        }
        @Override
        public void onRmsChanged(float rmsdB) {
        }
        @Override
        public void onBufferReceived(byte[] buffer) {
        }
        @Override
        public void onPartialResults(Bundle partialResults) {
        }
        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    }

    public void control(int a, int b){
        sum = sum + b;
        result.setText(String.valueOf(sum), TextView.BufferType.EDITABLE);
        if (sum == a) {
            fnsh.setVisibility(View.VISIBLE);
            fnsh.setText("Congratulations!");
            words = "Congratulations. Shake the phone to start again.";
            textToSpeech.speak(words, MODE_PRIVATE, ttsParams);
            fnsh.setTextColor(Color.GREEN);
        } else if (sum >= a) {
            fnsh.setVisibility(View.VISIBLE);
            fnsh.setText("Game Over");
            words = "Game Over. Shake the phone to start again.";
            textToSpeech.speak(words, MODE_PRIVATE, ttsParams);
            fnsh.setTextColor(Color.RED);
        } else {
            listen();
        }
    }

    public void invalid_number(){
        words = "Number is invalid.";
        textToSpeech.speak(words, MODE_PRIVATE, ttsParams);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                listen();
            }
        }, 1500);
    }

    private void utteranceCompleted() {
        listen();
    }

    private void playFirstMessage() {
        textToSpeech.speak("Let the game begin! Say a number.", MODE_PRIVATE, ttsParams);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                init();
                listen();
            }
        }, 3300);
    }
}