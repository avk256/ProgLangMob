package com.example.lab6;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameActivity extends AppCompatActivity {
    private TextView time;
    private static String TAG="GameActivity";
    private TextView points;
    private TextView color1;
    private TextView color2;
    private Map<String,Integer> dictionary;
    private int size;
    private Object[] values;
    private List<String> keyList;
    private MyCountDownTimer timer;
    private long  millisInFuture;
    private long countDownInterval;
    private boolean isStopped;
    private String difficulty;
    private String colors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        isStopped = false;

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        int NightMode = sharedPreferences.getInt("NightModeInt", 1);
        getDelegate().setLocalNightMode(NightMode);

        SharedPreferences mySharedPreferences = this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        difficulty = mySharedPreferences.getString("difficulty", "");
        colors = mySharedPreferences.getString("colors", "");
        TextView colorsTextView = (TextView) findViewById(R.id.colors);
        TextView difficultyTextView = (TextView) findViewById(R.id.difficulty);
        difficultyTextView.setText(difficulty.toLowerCase(Locale.ROOT));
        colorsTextView.setText(colors.toLowerCase(Locale.ROOT));

        dictionary = onDictionary(colors);
        Set<String> keySet = dictionary.keySet();
        keyList = new ArrayList<>(keySet);
        values = dictionary.values().toArray();
        size = keyList.size();



        points = (TextView) findViewById(R.id.points);
        color1 = (TextView) findViewById(R.id.color1);
        color2 = (TextView) findViewById(R.id.color2);

        changeColors();

        time = (TextView) findViewById(R.id.time);
        ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progressBar);
        timer = new MyCountDownTimer(60000, 1000) {
            @Override
            public void onFinish() {
                time.setText("00:00");
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.putExtra("result", points.getText());
                finish();


                startActivity(intent);
            }

            @Override
            public void onTick(long millisUntilFinished) {

                if(millisUntilFinished < 10000)
                    time.setText("00:0" + Long.toString(millisUntilFinished/1000) );
                else
                    time.setText("00:" + Long.toString(millisUntilFinished/1000) );
                progress_bar.setProgress((int)millisUntilFinished/1000);
            }
        };
        timer.start();

        onYesClick();
        onNoClick();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parametr_menu, menu);
        return true;
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem pause = menu.findItem(R.id.pause_menu_btn);
        MenuItem resume = menu.findItem(R.id.resume_menu_btn);
        if(isStopped)
        {
            pause.setVisible(false);
            resume.setVisible(true);
        }
        else
        {
            pause.setVisible(true);
            resume.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        switch(item.getItemId()){
            case R.id.settings_menu_btn :{
                Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case  R.id.pause_menu_btn :{
                timer.pause();
                isStopped = true;
                break;
            }
            case R.id.resume_menu_btn :{
                timer.resume();
                isStopped = false;
                break;
            }
            case R.id.stop_menu_btn :{
                timer.pause();
                timer.onFinish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            millisInFuture = savedInstanceState.getLong("millisInFuture");
            countDownInterval = savedInstanceState.getLong("countDownInterval");
            int tmp = savedInstanceState.getInt("points");
            points.setText(""+tmp);
            Log.d(TAG, "MainActivity: onRestoreInstanceState  "+millisInFuture + " "+countDownInterval);


        }else{
            millisInFuture=60000;
            countDownInterval=1000;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("millisInFuture", timer.getMillisInFuture());
        savedInstanceState.putLong("countDownInterval", timer.getCountDownInterval());
        savedInstanceState.putInt("points", Integer.parseInt(points.getText().toString()));
        Log.d(TAG, "MainActivity: onSaveInstanceState  "+timer.getMillisInFuture() + " "+timer.getCountDownInterval());

        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(millisInFuture == 0){
            millisInFuture = 60000;
            countDownInterval = 1000;
            points.setText("0");
        }
        Log.d(TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        int NightMode = sharedPreferences.getInt("NightModeInt", 1);
        getDelegate().setLocalNightMode(NightMode);

        SharedPreferences mySharedPreferences = this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        difficulty = mySharedPreferences.getString("difficulty", "");
        colors = mySharedPreferences.getString("colors", "");
        TextView colorsTextView = (TextView) findViewById(R.id.colors);
        TextView difficultyTextView = (TextView) findViewById(R.id.difficulty);
        difficultyTextView.setText(difficulty.toLowerCase(Locale.ROOT));
        colorsTextView.setText(colors.toLowerCase(Locale.ROOT));

        dictionary = onDictionary(colors);
        Set<String> keySet = dictionary.keySet();
        keyList = new ArrayList<>(keySet);
        values = dictionary.values().toArray();
        size = keyList.size();



        timer.resume(millisInFuture, countDownInterval);
        //timer.resume();
        Log.d(TAG, "MainActivity: onResume() "+millisInFuture+"  "+countDownInterval);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.pause();
        Log.d(TAG, "MainActivity: onPause()");
    }



    public HashMap <String, Integer> onDictionary(String colors){
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        switch (colors){
            case "Всі":{
                dictionary.put("Червоний", -65536);//
                dictionary.put("Зелений",  -14435799);//
                dictionary.put("Помаранчевий", -26368);//
                dictionary.put("Жовтий", -598252);//
                dictionary.put("Блакитний", -16728876);//
                dictionary.put("Синій", -14401322);//
                dictionary.put("Фіолетовий", -9422635);//
                dictionary.put("Чорний", -16777216);
                dictionary.put("Рожевий", -841281027);//
                break;
            }
            case "Теплі":{
                dictionary.put("Червоний", -65536);//
                dictionary.put("Помаранчевий", -26368);//
                dictionary.put("Жовтий", -598252);//
                dictionary.put("Рожевий", -841281027);//
                break;
            }
            case "Холодні":{
                dictionary.put("Зелений",  -14435799);//
                dictionary.put("Блакитний", -16728876);//
                dictionary.put("Синій", -14401322);//
                dictionary.put("Фіолетовий", -9422635);//
                dictionary.put("Чорний", -16777216);
                break;
            }
            default:{
                break;
            }
        }

        return (HashMap<String, Integer>) dictionary;
    }


    public void onYesClick (){
        Button yes = (Button) findViewById(R.id.yes_btn);

        yes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int c2 = color2.getCurrentTextColor();
                        int prev = Integer.parseInt(points.getText().toString());
                        switch (difficulty) {
                            case "Легко": {
                                if (c2 == dictionary.get(color1.getText().toString())) {
                                    points.setText(Integer.toString(prev + 1));
                                } else {

                                }
                                break;
                            }
                            case "Нормально": {
                                if (c2 == dictionary.get(color1.getText().toString())) {
                                    points.setText(Integer.toString(prev + 1));
                                } else {
                                    points.setText(Integer.toString(prev - 1));
                                    timer.onIncrement(-1000);
                                }
                                break;
                            }
                            case "Складно": {
                                if (c2 == dictionary.get(color1.getText().toString())) {
                                    points.setText(Integer.toString(prev + 1));
                                } else {
                                    points.setText(Integer.toString(prev - 2));
                                    timer.onIncrement(-2000);
                                }

                                break;
                            }
                        }
                        changeColors();
                    }
                }
        );
    }
    public void onNoClick (){
        Button no = (Button) findViewById(R.id.no_btn);
        no.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {int c2 = color2.getCurrentTextColor();
                        int prev = Integer.parseInt(points.getText().toString());
                        switch (difficulty) {
                            case "Легко": {
                                if (c2 != dictionary.get(color1.getText().toString())) {
                                    points.setText(Integer.toString(prev + 1));
                                }
                                break;
                            }
                            case "Нормально": {
                                if (c2 != dictionary.get(color1.getText().toString())) {
                                    points.setText(Integer.toString(prev + 1));
                                } else {
                                    points.setText(Integer.toString(prev - 1));
                                    timer.onIncrement(-1000);
                                }
                                break;
                            }
                            case "Складно": {
                                if (c2 != dictionary.get(color1.getText().toString())) {
                                    points.setText(Integer.toString(prev + 1));
                                } else {
                                    points.setText(Integer.toString(prev - 2));
                                    timer.onIncrement(-2000);
                                }
                                break;
                            }
                        }
                        changeColors();
                    }
                }
        );
    }
    public void changeColors(){
        Random R = new Random();
        Log.d("GameActivity", ""+size);
        color1.setText(keyList.get(R.nextInt(size)));
        color1.setTextColor((Integer)values[R.nextInt(values.length)]);
        color2.setText(keyList.get(R.nextInt(size)));
        color2.setTextColor((Integer)values[R.nextInt(values.length)]);
    }
}