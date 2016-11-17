package com.upiicsa.lectorlinear;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private SeekBar seekSpeed;
    private SeekBar seekTextSize;
    private ImageButton btnStart;
    private ImageButton btnPause;
    private ImageButton btnStop;
    private Button btnResume;
    private ImageButton btnnextChapter;
    private PonerScrollTexto scroll;
    private Chronometer cronometro;
    private long lastPause;
    boolean finished = false;
    private int cap = 0;
    private boolean IsPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Libro libro = new Libro();
        scroll = (PonerScrollTexto) findViewById(R.id.scrolltext);
        scroll.setTextSize(20);
        scroll.setText(libro.getIntroduccion());

        scroll.SetOnScrollEndListener(new OnScrollEndListener() {

            @Override
            public void OnScrollEnd() {
                cap += 1;
                switch (cap) {
                    case 1: {
                        scroll.setText(libro.getBiografia());
                        scroll.startScroll();
                    }
                    break;
                    case 2: {
                        scroll.setText(libro.getBiografia2());
                        scroll.startScroll();
                    }
                    break;
                    case 3: {
                        scroll.setText(libro.getBiografia3());
                        scroll.startScroll();
                    }
                    break;
                }
                if (cap == 3)
                    cap = 0;
            }
        });

        seekSpeed = (SeekBar) findViewById(R.id.seekSpeed);
        seekSpeed.setMax(100000);
        seekSpeed.setProgress(50000);

        cronometro = (Chronometer) findViewById(R.id.cronometro);

        seekTextSize = (SeekBar) findViewById(R.id.seekText);
        seekTextSize.setMax(50);
        seekTextSize.setProgress(20);
        seekTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (seekTextSize.getProgress() <= 12)
                    scroll.setTextSize(12);
                else
                    scroll.setTextSize(seekTextSize.getProgress());

            }
        });

        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scroll.setRndDuration(110000 - seekSpeed.getProgress());
                btnStart.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                btnResume.setVisibility(View.INVISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                cronometro.setBase(SystemClock.elapsedRealtime());
                cronometro.start();
                scroll.startScroll();

            }
        });

        btnPause = (ImageButton) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!IsPaused) {
                    scroll.pauseScroll();
                    cronometro.stop();
                    lastPause = SystemClock.elapsedRealtime();
                    btnPause.setImageResource(R.drawable.play);
                    IsPaused = true;
                }
                else
                {
                    scroll.resumeScroll();
                    cronometro.setBase(cronometro.getBase()
                            + SystemClock.elapsedRealtime() - lastPause);
                    cronometro.start();
                    btnPause.setImageResource(R.drawable.pause);
                    IsPaused = false;
                }

            }
        });
        btnnextChapter = (ImageButton) findViewById(R.id.btn_nextChapter);
        btnnextChapter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scroll.setRndDuration(110000 - seekSpeed.getProgress());
                scroll.stopScroll();
                scroll.startScroll();
                scroll.setText(libro.getBiografia());

            }
        });

        btnStop = (ImageButton) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scroll.stopScroll();
                btnStop.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
                btnResume.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setImageResource(R.drawable.pause);
                long segundos = (SystemClock.elapsedRealtime() - cronometro.getBase())/1000;
                long minutos;
                if(segundos > 60) {
                    minutos = segundos / 60;
                    if(minutos > 0 && minutos < 2)
                        Toast.makeText(getApplicationContext(), "Leiste " + minutos + " minuto", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Leiste " + minutos + " minutos", Toast.LENGTH_SHORT).show();
                }
                if(segundos < 60) {
                    if(segundos > 0 && segundos < 2)
                        Toast.makeText(getApplicationContext(), "Leiste " + segundos + " segundo", Toast.LENGTH_SHORT).show();
                    else
                    Toast.makeText(getApplicationContext(), "Leiste " + segundos + " segundos", Toast.LENGTH_SHORT).show();
                }
                cronometro.stop();

            }
        });

        btnResume = (Button) findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scroll.resumeScroll();
                cronometro.setBase(cronometro.getBase()
                        + SystemClock.elapsedRealtime() - lastPause);
                cronometro.start();
                btnResume.setVisibility(View.INVISIBLE);

            }
        });

        seekSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (seekSpeed.getProgress() <= 10000) {
                    scroll.pauseScroll();
                    scroll.setRndDuration(100000);
                    scroll.resumeScroll();
                } else {
                    scroll.pauseScroll();
                    scroll.setRndDuration(110000 - seekSpeed.getProgress());
                    scroll.resumeScroll();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });
        Inicializar();

    }

    public void Inicializar() {
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
        btnResume.setVisibility(View.INVISIBLE);
        btnStop.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        // Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

}
