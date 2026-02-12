package com.example.securenotifyvault;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VaultActivity extends AppCompatActivity implements SensorEventListener {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private NotificationsAdapter adapter;
    private TextView tvTotalCount, tvSuspiciousCount, tvTopApp;

    private SensorManager sensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private boolean isPanicTriggered = false;

    private final String[] DANGEROUS_WORDS = {
            "password", "pass", "code", "otp", "bank", "money", "urgent",
            "סיסמה", "קוד", "בנק", "אשראי", "כסף", "דחוף", "העברה", "סודי"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvSuspiciousCount = findViewById(R.id.tvSuspiciousCount);
        tvTopApp = findViewById(R.id.tvTopApp);

        dbHelper = new DatabaseHelper(this);
        loadNotifications();

        Button btnClear = findViewById(R.id.btnClearHistory);
        btnClear.setOnClickListener(v -> {
            dbHelper.deleteAll();
            loadNotifications();
            Toast.makeText(this, "All evidence destroyed!", Toast.LENGTH_SHORT).show();
        });

        Button btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(v -> exportData());

        Button btnIntruders = findViewById(R.id.btnIntruders);
        btnIntruders.setOnClickListener(v -> {
            startActivity(new Intent(this, GalleryActivity.class));
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 11 && !isPanicTriggered) {
                isPanicTriggered = true;
                panicLock();
            }
        }
    }

    private void panicLock() {
        Toast.makeText(this, "PANIC DETECTED - LOCKING VAULT", Toast.LENGTH_LONG).show();

        finishAffinity();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        isPanicTriggered = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    private void loadNotifications() {
        ArrayList<DatabaseHelper.NotificationItem> list = dbHelper.getAllNotifications();
        adapter = new NotificationsAdapter(list);
        recyclerView.setAdapter(adapter);
        calculateStats(list);
    }

    private void calculateStats(ArrayList<DatabaseHelper.NotificationItem> list) {
        int total = list.size();
        int suspicious = 0;
        Map<String, Integer> appCounts = new HashMap<>();
        String topApp = "-";
        int maxCount = 0;

        for (DatabaseHelper.NotificationItem item : list) {
            String lowerText = item.text.toLowerCase();
            for (String word : DANGEROUS_WORDS) {
                if (lowerText.contains(word)) {
                    suspicious++;
                    break;
                }
            }
            String cleanName = item.app.replace("com.", "").replace("android.", "");
            if (cleanName.contains(".")) {
                String[] parts = cleanName.split("\\.");
                cleanName = parts[parts.length - 1];
            }
            int count = appCounts.getOrDefault(cleanName, 0) + 1;
            appCounts.put(cleanName, count);
            if (count > maxCount) {
                maxCount = count;
                topApp = cleanName;
            }
        }
        tvTotalCount.setText(String.valueOf(total));
        tvSuspiciousCount.setText(String.valueOf(suspicious));
        tvTopApp.setText(topApp);
    }

    private void exportData() {
        ArrayList<DatabaseHelper.NotificationItem> list = dbHelper.getAllNotifications();
        if (list.isEmpty()) {
            Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,App,Sender,Message,Time,Is_Suspicious\n");

        for (DatabaseHelper.NotificationItem item : list) {
            boolean isSuspicious = false;
            for (String word : DANGEROUS_WORDS) {
                if (item.text.toLowerCase().contains(word)) {
                    isSuspicious = true;
                    break;
                }
            }
            String cleanText = item.text.replace(",", " ").replace("\n", " ");
            String cleanTitle = item.title.replace(",", " ");
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(this);
            String dateString = dateFormat.format(new java.util.Date(Long.parseLong(item.time)));

            csvBuilder.append("ID").append(",")
                    .append(item.app).append(",")
                    .append(cleanTitle).append(",")
                    .append(cleanText).append(",")
                    .append(dateString).append(",")
                    .append(isSuspicious ? "YES" : "NO")
                    .append("\n");
        }

        String csvContent = csvBuilder.toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, csvContent);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Stolen_Data.csv");
        startActivity(Intent.createChooser(sendIntent, "Export stolen data to:"));
    }
}