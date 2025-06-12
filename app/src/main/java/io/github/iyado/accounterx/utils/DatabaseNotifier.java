package io.github.iyado.accounterx.utils;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import io.github.iyado.accounterx.R;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class DatabaseNotifier extends FirebaseMessagingService {

// --Commented out by Inspection START (04/06/2025 12:58):
//    public DatabaseNotifier() {
//    }
// --Commented out by Inspection STOP (04/06/2025 12:58)

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this,"sy")
                    .setAutoCancel(true)
                    .setColor(getColor(R.color.light_blue_600))
                    .setColorized(true)
                    .setBadgeIconType(R.drawable.shapefx)
                    .setContentText(Objects.requireNonNull(message.getNotification()).getBody())
                    .setContentTitle(message.getNotification().getTitle())
                    .setSmallIcon(R.drawable.shapefxred)
                    .build();
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(manager.hashCode(),notification);
    }
}