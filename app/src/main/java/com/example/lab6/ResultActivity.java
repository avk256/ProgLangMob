package com.example.lab6;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {
    private TextView result;
    private Button play_btn;
    private Button main_btn;
    private Button exit_btn;
    private String email;
    private boolean statistics;
    private final String TAG = "ResultActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        int NightMode = sharedPreferences.getInt("NightModeInt", 1);
        getDelegate().setLocalNightMode(NightMode);
        result = (TextView) findViewById(R.id.result);

        SharedPreferences mySharedPreferences = this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        String difficulty = mySharedPreferences.getString("difficulty", "");

        Intent intent = getIntent();
        String resultValue = intent.getStringExtra("result");

        result.setText(resultValue);

        email = mySharedPreferences.getString("email", "");
        statistics = mySharedPreferences.getBoolean("sendStatistics", false);
        Log.e(TAG, "sendStatistics = "+statistics);
        if(statistics) {
            sendEmail(email, result.getText().toString());
        }
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        LocalDateTime lt = LocalDateTime.now();
        User user = new User();
        user.uid = new Random().nextInt();
        user.time = lt.toString().substring(0, lt.toString().length());
        user.email = email;
        user.result = Integer.valueOf(resultValue);
        user.difficulty = difficulty;
        userDao.insertAll(user);


        List<User> users = userDao.getAllByEmail(email);
        TableLayout tl=(TableLayout)findViewById(R.id.statistics_table);
        TextView testTV = (TextView) findViewById(R.id.textView7);
        if(users != null){
            for(User usr : users){
                TableRow tr = new TableRow(this);
                TextView tv1 = new TextView(this);
                tv1.setText(""+usr.time);
                tv1.setGravity(Gravity.CENTER);
                tv1.setLayoutParams(testTV.getLayoutParams());
                TextView tv2 = new TextView(this);
                tv2.setText(""+usr.result);
                tv2.setGravity(Gravity.CENTER);
                tv2.setLayoutParams(testTV.getLayoutParams());
                TextView tv3 = new TextView(this);
                tv3.setText(usr.difficulty);
                tv3.setGravity(Gravity.CENTER);
                tv3.setLayoutParams(testTV.getLayoutParams());
                tr.addView(tv1);
                tr.addView(tv2);
                tr.addView(tv3);
                tl.addView(tr);
            }
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab4-1d1e9-default-rtdb.europe-west1.firebasedatabase.app");
        String emailChild = email.substring(0, email.indexOf("."));
        DatabaseReference myRef = database.getReference(emailChild);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int value;
                if (dataSnapshot.child(difficulty+"/BestResult").getValue(Integer.class) == null){
                    value = 0;
                }else{
                    value = dataSnapshot.child(difficulty+"/BestResult").getValue(Integer.class);
                }
                Log.d(TAG, ""+value + "  " + resultValue);
                if(value < Integer.valueOf(resultValue)){
                    myRef.child(difficulty+"/BestResult").setValue(Integer.valueOf(resultValue));
                }
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        addListenerOnButton();
    }

    void sendEmail(String email, String result){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Score");
        intent.putExtra(Intent.EXTRA_TEXT,  "Your score is " + result);

        intent.setType("message/rfc822");

        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }
    void addListenerOnButton(){
        play_btn = (Button) findViewById(R.id.play_btn);
        main_btn = (Button) findViewById(R.id.main_btn);
        exit_btn = (Button) findViewById(R.id.exit_btn);
        play_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
        );
        main_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
                        finish();
                        startActivity(intent);

                    }
                }
        );
        exit_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
    }

}