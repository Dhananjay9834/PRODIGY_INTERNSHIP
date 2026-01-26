package com.example.tictactoe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button[] buttons = new Button[9];
    TextView tvStatus;
    Button btnReset;

    boolean isPlayerX = true;
    boolean gameActive = true;
    int moveCount = 0;

    int[][] winPositions = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        btnReset = findViewById(R.id.btnReset);

        for (int i = 0; i < 9; i++) {
            int id = getResources().getIdentifier("btn" + i, "id", getPackageName());
            buttons[i] = findViewById(id);
            int index = i;

            buttons[i].setOnClickListener(v -> handleMove(index));
        }

        btnReset.setOnClickListener(v -> resetGame());
    }

    private void handleMove(int index) {
        if (!gameActive || !buttons[index].getText().toString().isEmpty())
            return;

        if (isPlayerX) {
            buttons[index].setText("X");
            buttons[index].setTextColor(Color.parseColor("#D32F2F"));
        } else {
            buttons[index].setText("O");
            buttons[index].setTextColor(Color.parseColor("#1976D2"));
        }

        moveCount++;

        if (checkWin()) {
            tvStatus.setText("Player " + (isPlayerX ? "X" : "O") + " Wins!");
            gameActive = false;
            return;
        }

        if (moveCount == 9) {
            tvStatus.setText("It's a Draw!");
            gameActive = false;
            return;
        }

        isPlayerX = !isPlayerX;
        tvStatus.setText("Player " + (isPlayerX ? "X" : "O") + "'s Turn");
    }

    private boolean checkWin() {
        for (int[] pos : winPositions) {
            String a = buttons[pos[0]].getText().toString();
            String b = buttons[pos[1]].getText().toString();
            String c = buttons[pos[2]].getText().toString();

            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                highlightWin(pos);
                return true;
            }
        }
        return false;
    }

    private void highlightWin(int[] pos) {
        for (int i : pos) {
            buttons[i].setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#C8E6C9")));
        }
    }

    private void resetGame() {
        for (Button button : buttons) {
            button.setText("");
            button.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }
        isPlayerX = true;
        gameActive = true;
        moveCount = 0;
        tvStatus.setText("Player X's Turn");
    }
}
