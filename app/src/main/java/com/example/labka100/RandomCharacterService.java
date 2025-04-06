package com.example.labka100;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.Random;

public class RandomCharacterService extends Service {
    private boolean isRandomGeneratorOn;
    private final String TAG = "RandomCharacterService";
    private final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static final String ACTION_TAG = "my.custom.action.tag.lab6";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service started...");

        isRandomGeneratorOn = true;

        new Thread(this::startRandomGenerator).start();

        return START_STICKY;
    }

    private void startRandomGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                int randomIdx = new Random().nextInt(alphabet.length);
                char randomChar = alphabet[randomIdx];

                Log.i(TAG, "Generated Character: " + randomChar);

                Intent broadcastIntent = new Intent(ACTION_TAG);
                broadcastIntent.putExtra("randomCharacter", randomChar);
                sendBroadcast(broadcastIntent);
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread interrupted", e);
            }
        }
    }

    private void stopRandomGenerator() {
        isRandomGeneratorOn = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenerator();
        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service destroyed...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
