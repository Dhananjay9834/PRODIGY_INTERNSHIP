package com.example.t1_calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvDisplay;
    String currentInput = "";
    double firstValue = 0;
    char operator = ' ';
    boolean isOperatorSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        GridLayout grid = findViewById(R.id.gl);

        for (int i = 0; i < grid.getChildCount(); i++) {
            View view = grid.getChildAt(i);
            if (view instanceof Button) {
                ((Button) view).setOnClickListener(this::onButtonClick);
            }
        }
    }

    private void onButtonClick(View v) {
        Button btn = (Button) v;
        String text = btn.getText().toString();

        if (text.equals("C")) {
            resetCalculator();
            return;
        }
        if (text.equals("⌫")) {

            if (currentInput.isEmpty()) {
                tvDisplay.setText("0");
                return;
            }
            currentInput = currentInput.substring(0, currentInput.length() - 1);

            if (currentInput.isEmpty()) {
                resetCalculator();
                return;
            }

            isOperatorSet = false;
            operator = ' ';
            for (int i = 0; i < currentInput.length(); i++) {
                char ch = currentInput.charAt(i);
                if (isOperator(String.valueOf(ch))) {
                    operator = ch;
                    isOperatorSet = true;
                    break;
                }
            }

            updateDisplay(currentInput, "");
            return;
        }

        if (isOperator(text)) {
            if (currentInput.isEmpty()) return;

            // Replace operator if pressed continuously
            if (isOperatorSet && !endsWithDigit()) {
                currentInput =
                        currentInput.substring(0, currentInput.length() - 1) + text;
                operator = text.charAt(0);
                updateDisplay(currentInput, "");
                return;
            }

            if (isOperatorSet && endsWithDigit()) {
                double result = calculateResult();
                firstValue = result;
                operator = text.charAt(0);
                currentInput = formatResult(result) + text;
                updateDisplay(currentInput, formatResult(result));
                isOperatorSet = true;
                return;
            }

            // First operator
            firstValue = Double.parseDouble(currentInput);
            operator = text.charAt(0);
            currentInput += text;
            isOperatorSet = true;
            updateDisplay(currentInput, "");
            return;
        }
        if (text.equals("=")) {

            if (!isOperatorSet || !endsWithDigit()) return;

            double result = calculateResult();
            updateDisplay(currentInput, formatResult(result));
            currentInput = formatResult(result);
            isOperatorSet = false;
            return;
        }
        currentInput += text;
        updateDisplay(currentInput, "");
    }

    private void updateDisplay(String expression, String result) {
        if (result.isEmpty()) {
            tvDisplay.setText(expression);
        } else {
            tvDisplay.setText(expression + "\n" + result);
        }
    }
    private boolean isOperator(String s) {
        return "+-×÷%".contains(s);
    }

    private boolean endsWithDigit() {
        char last = currentInput.charAt(currentInput.length() - 1);
        return Character.isDigit(last);
    }

    private double calculateResult() {
        int index = currentInput.lastIndexOf(operator);
        double secondValue =
                Double.parseDouble(currentInput.substring(index + 1));
        return calculate(firstValue, secondValue, operator);
    }

    private String formatResult(double result) {
        return (result == (int) result)
                ? String.valueOf((int) result)
                : String.valueOf(result);
    }

    private double calculate(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '×': return a * b;
            case '÷': return b != 0 ? a / b : 0;
            case '%': return a % b;
        }
        return 0;
    }

    private void resetCalculator() {
        currentInput = "";
        firstValue = 0;
        operator = ' ';
        isOperatorSet = false;
        tvDisplay.setText("0");
    }
}
