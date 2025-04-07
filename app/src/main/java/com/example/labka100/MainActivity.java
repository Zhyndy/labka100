package com.example.labka100;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText randomCharacterEditText;
    private BroadcastReceiver broadcastReceiver;
    private Intent serviceIntent;
    public static final String ACTION_TAG = "my.custom.action.tag.lab6";

    // Добавляем переменные для управления музыкой
    private MediaPlayer mediaPlayer;
    private Button buttonStartMusic;
    private Button buttonStopMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomCharacterEditText = findViewById(R.id.editText_randomCharacter);
        Button startButton = findViewById(R.id.button_start);
        Button endButton = findViewById(R.id.button_end);
        Button musicButton = findViewById(R.id.button_music);
        buttonStopMusic = findViewById(R.id.button_stop_music); // Инициализация кнопки остановки музыки

        serviceIntent = new Intent(this, RandomCharacterService.class);

        startButton.setOnClickListener(this::onClick);
        endButton.setOnClickListener(this::onClick);
        musicButton.setOnClickListener(this::onClickMusic);
        buttonStopMusic.setOnClickListener(this::onClickStopMusic); // Устанавливаем обработчик для кнопки остановки музыки

        broadcastReceiver = new MyBroadcastReceiver();

        // Инициализация MediaPlayer для воспроизведения музыки
        mediaPlayer = MediaPlayer.create(this, R.raw.song); // Замените на имя вашего файла
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_start) {
            startService(serviceIntent);
        } else if (id == R.id.button_end) {
            stopService(serviceIntent);
            randomCharacterEditText.setText("");
        }
    }

    // Обработчик для кнопки "Start Music"
    public void onClickMusic(View view) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Обработчик для кнопки "Stop Music"
    public void onClickStopMusic(View view) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.prepareAsync(); // Подготовка к возможному повторному воспроизведению
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ACTION_TAG);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            intentFilter.setPriority(0);
        }

        registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Освобождение ресурсов
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            char data = intent.getCharExtra("randomCharacter", '?');
            randomCharacterEditText.setText(String.valueOf(data));
        }
    }
}
