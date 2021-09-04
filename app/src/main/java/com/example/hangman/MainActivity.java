package com.example.hangman;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String word;
    TextView guessedword;
    EditText txt;
    ImageView img1;
    String[] imgaddr;
    int imgnum;
    Random rand;
    String guessedwords;
    TextView guessview;
    Button guessBtn;
    Button NewBtn;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rand=new Random();
        word=getResources().getStringArray(R.array.words)[rand.nextInt(5441)];
        Log.i("word",word);
        guessedword=(TextView)findViewById(R.id.wordview);
        guessedword.setText(word.replaceAll(".","?"));
        guessview=(TextView)findViewById(R.id.guessview);
        guessBtn=(Button)findViewById(R.id.guess);
        NewBtn=(Button)findViewById(R.id.New);

        imgaddr= new String[]{
                "@drawable/hangman6",
                "@drawable/hangman5",
                "@drawable/hangman4",
                "@drawable/hangman3",
                "@drawable/hangman2",
                "@drawable/hangman1",
                "@drawable/hangman0"};

        imgnum=0;
        guessedwords="";

    }
    public void Guessword(View view){
        txt=(EditText)findViewById(R.id.txt);
        char[] temp=guessedword.getText().toString().toCharArray();
        if(guessedwords.contains(txt.getText().toString())) {
            Toast.makeText(this,"Already entered",Toast.LENGTH_LONG).show();
            txt.setText("");
            return;
        }

        boolean check=true;
        for(int i=0;i<word.length();i++) {
            if (word.charAt(i) == txt.getText().charAt(0)) {
                temp[i] = word.charAt(i);
                check = false;
            }
        }


        String str=new String(temp);
        guessedword.setText(str);
        if(str.equals(word)) {
            Toast.makeText(this, "You win", Toast.LENGTH_LONG).show();
            guessBtn.setEnabled(false);
            return;
        }

        if(check){
            if(++imgnum>=7) {
                tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onInit(int status) {
                        tts.speak("Game over", TextToSpeech.QUEUE_FLUSH, null, "1234");

                    }
                });
                guessBtn.setEnabled(false);
                guessview.setText("Game over word was "+word);
                return;
            }
        }
        guessedwords+=txt.getText().toString();
        String message=(7-(imgnum))+" guesses left";
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "1234");

            }
        });


        guessview.setText("You have guessed :"+guessedwords+"("+message+")");

        txt.setText("");


        int imageResource = getResources().getIdentifier(imgaddr[imgnum], null, getPackageName());
        img1= (ImageView)findViewById(R.id.img);
        Drawable res = getResources().getDrawable(imageResource);
        img1.setImageDrawable(res);

    }

    public void newWord(View view){
        guessBtn.setEnabled(true);
        guessview.setText("You have guessed : ");
        word=getResources().getStringArray(R.array.words)[rand.nextInt(5441)];
        guessedword.setText(word.replaceAll(".","?"));
        img1.setImageResource(R.drawable.hangman6);
        imgnum=0;
        guessedwords="";

    }


    public void recognizer(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the single letter");
        startActivityForResult(intent, 1234);


    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> list=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        txt=(EditText)findViewById(R.id.txt);
        txt.setText(list.get(0));

    }





}


