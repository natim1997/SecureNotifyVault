package com.example.securenotifyvault;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.os.Bundle;
import java.util.Arrays;
import java.util.List;

public class NotificationService extends NotificationListenerService {

    private DatabaseHelper dbHelper;

    private String lastTitle = "";
    private String lastText = "";
    private long lastTime = 0;


    private static final List<String> TARGET_APPS = Arrays.asList(
            "com.whatsapp",
            "com.facebook.orca",
            "com.instagram.android",
            "org.telegram.messenger",
            "com.google.android.apps.messaging",
            "com.samsung.android.messaging"
    );

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        if (!TARGET_APPS.contains(packageName)) {
            return;
        }

        if (sbn.isOngoing()) {
            return;
        }

        Bundle extras = sbn.getNotification().extras;
        if (extras == null) return;

        String title = extras.getString(Notification.EXTRA_TITLE);
        CharSequence textSeq = extras.getCharSequence(Notification.EXTRA_TEXT);
        String text = (textSeq != null) ? textSeq.toString() : "";

        if (title == null || text.isEmpty()) return;

        if (text.contains("new messages") ||
                text.contains("Checking for new messages") ||
                text.contains("WhatsApp Web is currently active")) {
            return;
        }


        long currentTime = System.currentTimeMillis();
        if (title.equals(lastTitle) && text.equals(lastText) && (currentTime - lastTime < 2000)) {
            return;
        }

        lastTitle = title;
        lastText = text;
        lastTime = currentTime;

        dbHelper.addNotification(packageName, title, text);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}