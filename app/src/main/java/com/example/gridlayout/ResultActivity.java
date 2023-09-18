package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Button playAgainButton = findViewById(R.id.playAgainButton);

        TextView textViewUsedTime = findViewById(R.id.textView1);
        int timeValue = getIntent().getIntExtra("time", 0);
        textViewUsedTime.setText("Used " + timeValue + " seconds.");
        boolean gameWin = getIntent().getBooleanExtra("gameWin", false);
        if (!gameWin){
            TextView textView2 = findViewById(R.id.textView2);
            TextView textView3 = findViewById(R.id.textView3);

            textView2.setText("You Lost.");
            textView3.setText("Keep trying!");
        }

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        String timeValue = MainActivity.time;
//        textViewUsedTime.setText("Used " + timeValue + " seconds.");
    }
}