package com.ilandertech.brightness;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Method;

public class OperatorActivity extends AppCompatActivity {
    Button btnOne, btnTwo;
    TextView tvInfo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        btnOne = (Button) findViewById(R.id.but1);
        btnTwo = (Button) findViewById(R.id.but2);
        tvInfo = (TextView) findViewById(R.id.text3);

        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

        boolean isDualSIM = telephonyInfo.isDualSIM();
        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        TelephonyManager manager = (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        try {
            telephonyInfo.imsiSIM1 = telephonyInfo.getDeviceIdBySlot(context,
                    "getSimSerialNumberGemini", 0);
            telephonyInfo.imsiSIM2 = telephonyInfo.getDeviceIdBySlot(context,
                    "getSimSerialNumberGemini", 1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String number = manager.getLine1Number();

        String optName1 = getOutput(getApplicationContext(), "getCarrierName", 0);
        String optName2 = getOutput(getApplicationContext(), "getCarrierName", 1);

        final String carrierName = manager.getSimOperatorName();
        tvInfo.setText(" " + isDualSIM + " " + optName1 + " " + optName2 + " "
                + telephonyInfo.imsiSIM1 + " " + telephonyInfo.imsiSIM2 + " "
                + number + " " + isSIM1Ready + " " + isSIM2Ready);

        btnOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(
                        Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);

            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (carrierName.equalsIgnoreCase("TATA DOCOMO")
                        || carrierName.contains("DOCOMO")) {
                    startService(new Intent(OperatorActivity.this, USSD.class));
                    String ussdCode = "*" + "111" + Uri.encode("#");
                    if (ActivityCompat.checkSelfPermission(OperatorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + ussdCode)));
                } else if (carrierName.equalsIgnoreCase("!dea")
                        || carrierName.contains("idea")) {
                    startService(new Intent(OperatorActivity.this, USSD.class));
                    String ussdCode = "*" + "121" + Uri.encode("#");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + ussdCode)));
                } else if (carrierName.equalsIgnoreCase("AIRTEL")
                        || carrierName.contains("airtel")) {
                    startService(new Intent(OperatorActivity.this, USSD.class));
                    String ussdCode = "*" + "123" + Uri.encode("#");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + ussdCode)));
                }

            }
        });

    }

    private static String getOutput(Context context, String methodName,
                                    int slotId) {
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        String reflectionMethod = null;
        String output = null;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            for (Method method : telephonyClass.getMethods()) {
                String name = method.getName();
                if (name.contains(methodName)) {
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && params[0].getName().equals("int")) {
                        reflectionMethod = name;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (reflectionMethod != null) {
            try {
                output = getOpByReflection(telephony, reflectionMethod, slotId,
                        false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    private static String getOpByReflection(TelephonyManager telephony,
                                            String predictedMethodName, int slotID, boolean isPrivate) {

        // Log.i("Reflection", "Method: " + predictedMethodName+" "+slotID);
        String result = null;

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass()
                    .getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID;
            if (slotID != -1) {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(
                            predictedMethodName, parameter);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName,
                            parameter);
                }
            } else {
                if (isPrivate) {
                    getSimID = telephonyClass
                            .getDeclaredMethod(predictedMethodName);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName);
                }
            }

            Object ob_phone;
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            if (getSimID != null) {
                if (slotID != -1) {
                    ob_phone = getSimID.invoke(telephony, obParameter);
                } else {
                    ob_phone = getSimID.invoke(telephony);
                }

                if (ob_phone != null) {
                    result = ob_phone.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.i("Reflection", "Result: " +  e.printStackTrace());
            return null;
        }

        return result;
    }


}