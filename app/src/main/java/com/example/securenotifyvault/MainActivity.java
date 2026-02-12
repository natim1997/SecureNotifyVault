package com.example.securenotifyvault;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private TextView tvScreen;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewOp = true;
    private int wrongAttempts = 0;

    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private static final String SECRET_CODE = "1337";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        } else {
            startCamera();
        }

        tvScreen = findViewById(R.id.tvScreen);
        setupCalculatorButtons();
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                imageCapture = new ImageCapture.Builder().build();

                try {
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture);
                } catch (Exception e) {
                }

            } catch (ExecutionException | InterruptedException e) {
            }
        }, getExecutor());
    }

    private void takeIntruderSelfie() {
        if (imageCapture == null) return;

        long timestamp = System.currentTimeMillis();
        File photoFile = new File(getFilesDir(), "intruder_" + timestamp + ".jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, getExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "System Error: 0x99", Toast.LENGTH_SHORT).show() // הטעיה
                );
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
            }
        });
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void setupCalculatorButtons() {
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        View.OnClickListener numberListener = v -> {
            Button b = (Button) v;
            if (isNewOp) {
                tvScreen.setText("");
                isNewOp = false;
            }
            currentInput = tvScreen.getText().toString() + b.getText().toString();
            tvScreen.setText(currentInput);
        };

        for (int id : numberIds) findViewById(id).setOnClickListener(numberListener);

        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> setOperator("-"));

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            tvScreen.setText("0");
            currentInput = "";
            firstNumber = 0;
            isNewOp = true;
        });

        findViewById(R.id.btnEqual).setOnClickListener(v -> onEqualPressed());
    }

    private void setOperator(String op) {
        try {
            firstNumber = Double.parseDouble(tvScreen.getText().toString());
            operator = op;
            isNewOp = true;
        } catch (NumberFormatException e) {}
    }

    private void onEqualPressed() {
        String input = tvScreen.getText().toString();

        if (input.equals(SECRET_CODE)) {
            wrongAttempts = 0;
            startActivity(new Intent(MainActivity.this, VaultActivity.class));
            tvScreen.setText("0");
            return;
        }

        if (input.length() == 4 && !input.equals(SECRET_CODE)) {
            wrongAttempts++;
            if (wrongAttempts >= 3) {
                takeIntruderSelfie();
                wrongAttempts = 0;
            }
        }

        double secondNumber = 0;
        try {
            secondNumber = Double.parseDouble(input);
        } catch (NumberFormatException e) { return; }

        double result = 0;
        if (operator.equals("+")) result = firstNumber + secondNumber;
        if (operator.equals("-")) result = firstNumber - secondNumber;

        if (result == (long) result) {
            tvScreen.setText(String.format("%d", (long) result));
        } else {
            tvScreen.setText(String.format("%s", result));
        }
        isNewOp = true;
    }
}