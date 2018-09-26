package com.example.brian.waveview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.brian.waveview.widget.WaveView;

public class MainActivity extends AppCompatActivity {

    WaveView wave_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initListener();
    }

    private void initListener() {
        wave_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wave_view.isWave()){
                    wave_view.stop();
                }else {
                    wave_view.start();
                }
            }
        });
    }

    private void findView() {
        wave_view = (WaveView) findViewById(R.id.wave_view);
    }
}
