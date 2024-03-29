package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Button;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Random;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 10;
    private int flag_left = 4;

    private int clock = 0;
    private boolean running = true;

    private boolean isPicked = true;

    private boolean gameOver = false;

    private boolean gameWin = true;



    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;

    private ArrayList<Integer> numbers = new ArrayList<>();
    Random random = new Random();

    private ArrayList<Integer> flagNumbers = new ArrayList<>();

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();

        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }

        runTimer();

        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(120);
            numbers.add(randomNumber);
        }

        TextView textViewPick = findViewById(R.id.textViewPick);
        textViewPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPicked) {
                    textViewPick.setText(R.string.flag);
                } else {
                    textViewPick.setText(R.string.pick);
                }
                isPicked = !isPicked;
            }
        });

        // Method (1): add statically created cells
//        TextView tv00 = (TextView) findViewById(R.id.textView00);
//        TextView tv01 = (TextView) findViewById(R.id.textView01);
//        TextView tv10 = (TextView) findViewById(R.id.textView10);
//        TextView tv11 = (TextView) findViewById(R.id.textView11);
//
//        tv00.setTextColor(Color.GRAY);
//        tv00.setBackgroundColor(Color.GRAY);
//        tv00.setOnClickListener(this::onClickTV);
//
//        tv01.setTextColor(Color.GRAY);
//        tv01.setBackgroundColor(Color.GRAY);
//        tv01.setOnClickListener(this::onClickTV);
//
//        tv10.setTextColor(Color.GRAY);
//        tv10.setBackgroundColor(Color.GRAY);
//        tv10.setOnClickListener(this::onClickTV);
//
//        tv11.setTextColor(Color.GRAY);
//        tv11.setBackgroundColor(Color.GRAY);
//        tv11.setOnClickListener(this::onClickTV);
//
//        cell_tvs.add(tv00);
//        cell_tvs.add(tv01);
//        cell_tvs.add(tv10);
//        cell_tvs.add(tv11);

        // Method (2): add four dynamically created cells
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i<=11; i++) {
            for (int j=0; j<=9; j++) {
                TextView tv = new TextView(this);
                tv.setHeight( dpToPixel(32) );
                tv.setWidth( dpToPixel(32) );
                tv.setTextSize( 16 );//dpToPixel(32) );
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }

        // Method (3): add four dynamically created cells with LayoutInflater
//        LayoutInflater li = LayoutInflater.from(this);
//        for (int i = 4; i<=5; i++) {
//            for (int j=0; j<=1; j++) {
//                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
//                //tv.setText(String.valueOf(i)+String.valueOf(j));
//                tv.setTextColor(Color.GRAY);
//                tv.setBackgroundColor(Color.GRAY);
//                tv.setOnClickListener(this::onClickTV);
//
//                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
//                lp.rowSpec = GridLayout.spec(i);
//                lp.columnSpec = GridLayout.spec(j);
//
//                grid.addView(tv, lp);
//
//                cell_tvs.add(tv);
//            }
//        }

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clock", clock);
        savedInstanceState.putBoolean("running", running);
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textView03);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                String time = String.valueOf(clock);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }


    public void onClickTV(View view){
        TextView tv = (TextView) view;

        if (gameOver){
            Intent intent = new Intent(this, ResultActivity.class);
            gameOver = false;
            intent.putExtra("time", clock);
            intent.putExtra("gameWin", gameWin);
            startActivity(intent);
        }

        int n = findIndexOfCellTextView(tv);

        if (isPicked)
        {
            int i = n / COLUMN_COUNT;
            int j = n % COLUMN_COUNT;

            tv.setText("");
            if (tv.getCurrentTextColor() == Color.GREEN) {
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);

                int mineCount = getMineCount(i, j);

                if (numbers.contains(n)) {
                    String mineText = getResources().getString(R.string.mine);
                    tv.setText(mineText);
                    for (Integer cellIndex : numbers) {
                        TextView cellTextView = cell_tvs.get(cellIndex);
                        cellTextView.setText(mineText);
                        cellTextView.setBackgroundColor(Color.RED);
                    }
                    gameWin = false;
                    gameOver = true;
                    running = false;
                }

                else{
                    if (mineCount > 0) {
                        tv.setText(String.valueOf(mineCount));
                        tv.setTextColor(Color.GRAY);
                    }
                    else{
                        revealAdjacentCells(i, j);
                    }

                    boolean allCellsProcessed = true;

                    for (TextView cell : cell_tvs) {
                        int textColor = cell.getCurrentTextColor();

                        int cellIndex = cell_tvs.indexOf(cell);
                        if (!numbers.contains(cellIndex)) {
                            if (textColor != Color.BLUE && textColor != Color.GRAY) {
                                allCellsProcessed = false;
                                break;
                            }
                        }
                    }

                    if (allCellsProcessed) {
                        String flagText = getResources().getString(R.string.flag);
                        for (Integer cellIndex : numbers) {
                            TextView cellTextView = cell_tvs.get(cellIndex);
                            cellTextView.setText(flagText);
                            cellTextView.setBackgroundColor(Color.YELLOW);
                        }
                        gameWin = true;
                        gameOver = true;
                        running = false;
                    }
                }
            }
        }
        else{
            String flagText = getResources().getString(R.string.flag);
            if (tv.getText()==flagText)
            {
                tv.setText("");
                flag_left++;
                flagNumbers.remove(Integer.valueOf(n));
            }
            else if (flag_left>0 && tv.getCurrentTextColor() == Color.GREEN) {
                tv.setText(flagText);
                flag_left--;
                flagNumbers.add(n);
            }
            TextView flagNumberView = findViewById(R.id.textView01);
            flagNumberView.setText(String.valueOf(flag_left));
        }

    }

    private void revealAdjacentCells(int row, int col) {
        if (row < 0 || row >= 12 || col < 0 || col >= 10) {
            return;
        }

        int cellIndex = row * COLUMN_COUNT + col;
        TextView currentCell = cell_tvs.get(cellIndex);

        if (currentCell.getCurrentTextColor()==Color.BLUE || !currentCell.getText().equals("")) {
            return;
        }

        int mineCount = getMineCount(row, col);
        if (numbers.contains(cellIndex)) {
            return;
        } else if (mineCount > 0) {
            currentCell.setText(String.valueOf(mineCount));
            currentCell.setTextColor(Color.GRAY);
            currentCell.setBackgroundColor(Color.LTGRAY);
        } else {
            currentCell.setTextColor(Color.BLUE);
            currentCell.setBackgroundColor(Color.LTGRAY);

            revealAdjacentCells(row - 1, col - 1);
            revealAdjacentCells(row - 1, col);
            revealAdjacentCells(row - 1, col + 1);
            revealAdjacentCells(row, col - 1);
            revealAdjacentCells(row, col + 1);
            revealAdjacentCells(row + 1, col - 1);
            revealAdjacentCells(row + 1, col);
            revealAdjacentCells(row + 1, col + 1);
        }


    }


    private int getMineCount(int row, int col) {
        int mineCount = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < 12 && j >= 0 && j < 10) {
                    int neighborIndex = i * COLUMN_COUNT + j;
                    if (numbers.contains(neighborIndex)) {
                        mineCount++;
                    }
                }
            }
        }
        return mineCount;
    }

}