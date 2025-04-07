package com.example.labka100;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
        Button musicButton = findViewById(R.id.button_music);  // Кнопка для музыкального сервиса

        // Один раз создаем serviceIntent для RandomCharacterService
        serviceIntent = new Intent(this, RandomCharacterService.class);

        startButton.setOnClickListener(this::onClick);
        endButton.setOnClickListener(this::onClick);
        musicButton.setOnClickListener(this::onClickMusic);  // Обработчик для кнопки музыки

        // Создаем и регистрируем BroadcastReceiver для получения символов
        broadcastReceiver = new MyBroadcastReceiver();
    }

    // Обработчик кликов для кнопок "Start" и "End"
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_start) {
            startService(serviceIntent);
        } else if (id == R.id.button_end) {
            stopService(serviceIntent);
            randomCharacterEditText.setText("");
        }
    }

    // Обработчик для запуска музыкального сервиса
    public void onClickMusic(View view) {
        Intent musicServiceIntent = new Intent(MainActivity.this, MyService.class); // Intent для музыкального сервиса
        ContextCompat.startForegroundService(MainActivity.this, musicServiceIntent); // Запуск foreground сервиса
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ACTION_TAG);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            intentFilter.setPriority(0);  // Опционально, можно установить приоритет
        }
        // Регистрируем broadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Отменяем регистрацию broadcastReceiver
        unregisterReceiver(broadcastReceiver);
    }

    // BroadcastReceiver для получения случайных символов
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            char data = intent.getCharExtra("randomCharacter", '?');
            randomCharacterEditText.setText(String.valueOf(data));
        }
    }
}
