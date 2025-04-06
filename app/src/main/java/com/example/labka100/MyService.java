package com.example.labka100;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private MediaPlayer soundPlayer;
    private static final String CHANNEL_ID = "foreground_service_channel";
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        soundPlayer = MediaPlayer.create(this, R.raw.song);  // Подключаем музыкальный файл из res/raw
        soundPlayer.setLooping(true);  // Включаем зацикливание музыки
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        Log.i("MyService", "Foreground service started...");

        // Создаем канал уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Foreground Music Service";
            String channelDescription = "This channel is used for foreground music playback notifications.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Создаем уведомление для Foreground Service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)  // Убедитесь, что иконка есть в res/drawable
                .setContentTitle("My Music Player")
                .setContentText("Music is playing...")
                .setOngoing(true)  // Уведомление будет постоянно отображаться, пока сервис работает
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Запускаем сервис в Foreground
        startForeground(1, builder.build());

        // Начинаем воспроизведение музыки
        soundPlayer.start();

        // Возвращаем START_STICKY, чтобы сервис не был остановлен системой
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPlayer.stop();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        Log.i("MyService", "Foreground service stopped...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;  // Мы не используем биндинг
    }
}
