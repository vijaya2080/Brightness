package com.ilandertech.brightness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar seekbar;
    TextView progress,deviceid;
    Context context;
    int Brightness;
Button get,id;
    String unique_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekbar = (SeekBar)findViewById(R.id.seekBar1);
        progress = (TextView)findViewById(R.id.textView1);
        deviceid=(TextView)findViewById(R.id.textView);
        get=(Button)findViewById(R.id.button);
        id=(Button)findViewById(R.id.button2);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WidthActivity.class));
            }
        });
        context = getApplicationContext();
        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                deviceid.setText(unique_id);
            }
        });
        //Getting Current screen brightness.
        Brightness = Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0
        );

        //Setting up current screen brightness to seekbar;
        seekbar.setProgress(Brightness);

        //Setting up current screen brightness to TextView;
        progress.setText("Screen Brightness : " + Brightness);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                progress.setText("Screen Brightness : " + i);

                //Changing Brightness on seekbar movement.

                Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,i
                );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}