package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvTimer;
    ImageButton btnStartPause, btnReset;

    Handler handler = new Handler();
    Runnable runnable;

    boolean isRunning = false;

    long startTime = 0L;
    long timeInMillis = 0L;
    long timeWhenPaused = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnReset = findViewById(R.id.btnReset);

        btnStartPause.setOnClickListener(v -> {
            if (!isRunning) {
                startOrResumeTimer();
            } else {
                pauseTimer();
            }
        });

        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void startOrResumeTimer() {
        isRunning = true;
        btnStartPause.setImageResource(R.drawable.pause_icon);

        // Adjust start time to support resume
        startTime = SystemClock.elapsedRealtime() - timeWhenPaused;

        runnable = new Runnable() {
            @Override
            public void run() {
                timeInMillis = SystemClock.elapsedRealtime() - startTime;

                int minutes = (int) (timeInMillis / 60000);
                int seconds = (int) (timeInMillis / 1000) % 60;
                int millis  = (int) (timeInMillis % 1000) / 10;

                tvTimer.setText(String.format(
                        "%02d:%02d:%02d", minutes, seconds, millis));

                handler.postDelayed(this, 10);
            }
        };

        handler.post(runnable);
    }

    private void pauseTimer() {
        handler.removeCallbacks(runnable);
        timeWhenPaused = timeInMillis;
        isRunning = false;
        btnStartPause.setImageResource(R.drawable.play_icon);
    }

    private void resetTimer() {
        handler.removeCallbacks(runnable);

        isRunning = false;
        startTime = 0L;
        timeInMillis = 0L;
        timeWhenPaused = 0L;

        tvTimer.setText("00:00:00");
        btnStartPause.setImageResource(R.drawable.play_icon);
    }
}