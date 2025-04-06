package com.example.labka100;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText randomCharacterEditText;
    private BroadcastReceiver broadcastReceiver;
    private Intent serviceIntent;

    public static final String ACTION_TAG = "my.custom.action.tag.lab6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomCharacterEditText = findViewById(R.id.editText_randomCharacter);
        Button startButton = findViewById(R.id.button_start);
        Button endButton = findViewById(R.id.button_end);
        Button musicButton = findViewById(R.id.button_music);  // New button for music service

        startButton.setOnClickListener(this::onClick);
        endButton.setOnClickListener(this::onClick);
        musicButton.setOnClickListener(this::onClickMusic);  // Music button click listener

        broadcastReceiver = new MyBroadcastReceiver();
        serviceIntent = new Intent(getApplicationContext(), RandomCharacterService.class);
    }

    // Button click handler for start and end
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_start) {
            startService(serviceIntent);
        } else if (id == R.id.button_end) {
            stopService(serviceIntent);
            randomCharacterEditText.setText("");
        }
    }

    // New method for starting the music service (Foreground Service)
    public void onClickMusic(View view) {
        Intent musicServiceIntent = new Intent(MainActivity.this, MyService.class); // Intent for Music Service
        ContextCompat.startForegroundService(MainActivity.this, musicServiceIntent); // Start the foreground service
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ACTION_TAG);

        // Use the RECEIVER_EXPORTED flag to indicate that this receiver is exported
        // If you're targeting Android 12 and above, you should specify this flag
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            intentFilter.setPriority(0);  // Optional: set priority, depending on your use case
        }
        registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    // Broadcast receiver to receive random character
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            char data = intent.getCharExtra("randomCharacter", '?');
            randomCharacterEditText.setText(String.valueOf(data));
        }
    }
}
