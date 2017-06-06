package com.ilandertech.brightness;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class WidthActivity extends AppCompatActivity {
    TextView one,two,model,name;
    Button button,modelname;
    int deviceWidth,deviceHeight,height,width = 0;
    Context context;
    DisplayMetrics displayMetrics;
    Vibrator vibrator;
    String DeviceModel, DeviceName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_width);
        one = (TextView)findViewById(R.id.textView1);
        two = (TextView)findViewById(R.id.textView2);
        model=(TextView)findViewById(R.id.tv_model);
        modelname=(Button)findViewById(R.id.bt_model);
        button = (Button)findViewById(R.id.button1);
        name=(TextView)findViewById(R.id.textView4);
        context = getApplicationContext();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        displayMetrics = new DisplayMetrics();

        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        modelname.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DeviceModel= android.os.Build.MODEL;
        DeviceName= android.os.Build.MANUFACTURER;

        model.setText(DeviceModel);
        name.setText(DeviceName);
    }
});
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vibrator.vibrate(7000);
                height = Math.round(displayMetrics.heightPixels / displayMetrics.density);

                width = Math.round(displayMetrics.widthPixels / displayMetrics.density);

                one.setText("Width = " + String.valueOf(width) + " - DP");

                two.setText("Height = " +String.valueOf(height) + " - DP");
            }
        });
    }



}