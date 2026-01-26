package com.example.qrcodescanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    Button btnScan, btnOpen, btnCopy;
    TextView tvResult;

    String scannedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        btnOpen = findViewById(R.id.btnOpen);
        btnCopy = findViewById(R.id.btnCopy);
        tvResult = findViewById(R.id.tvResult);

        btnOpen.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);

        checkCameraPermission();

        btnScan.setOnClickListener(v -> startQRScan());

        btnOpen.setOnClickListener(v -> {
            if (Patterns.WEB_URL.matcher(scannedText).matches()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannedText));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Scanned content is not a valid URL", Toast.LENGTH_SHORT).show();
            }
        });

        btnCopy.setOnClickListener(v -> {
            if (!scannedText.isEmpty()) {
                ClipboardManager clipboard =
                        (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("QR Result", scannedText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔐 Camera Permission
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE
            );
        }
    }

    // 📷 Start QR Scan
    private void startQRScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan QR or Barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    // 📩 Scan Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                scannedText = result.getContents();
                tvResult.setText(scannedText);

                btnCopy.setVisibility(View.VISIBLE);

                if (Patterns.WEB_URL.matcher(scannedText).matches()) {
                    btnOpen.setVisibility(View.VISIBLE);
                } else {
                    btnOpen.setVisibility(View.GONE);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
