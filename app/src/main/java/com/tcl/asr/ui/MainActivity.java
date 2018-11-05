package com.tcl.asr.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcl.asr.serviceHook.AMSHookHelper;


public class MainActivity extends Activity {

    public static TextView mStatus;
    public static final int RC_KEY_UP = 1;
    public static final int RC_KEY_DOWN = 2;
    public static final int RC_KEY_LOOP = 3;


    public static boolean useWalleveService = true;

    //默认调用本service
    public static String stubServicePkg = "com.tcl.asr.ui";
    public static String stubServiceActionValue = "com.tcl.asr.manager.aidl.Ircvoice";

    //如果配置使用walleve，则使用walleve中的service
    public static String useWalleveServicePkg = "com.tcl.walleve";
    public static String useWalleveServiceActionValue = "com.tcl.asr.manager.aidl.Ircvoice";
    public static String useWalleveServiceName = "com.tcl.walleve.ASRService";


    //如果配置使用asrManager，则使用asrManager中的service
    public static String useASRManagerServicePkg = "com.tcl.asr.manager";
    public static String useASRManagerServiceActionValue = "com.tcl.asr.manager.aidl.Ircvoice";
    public static String useASRManagerServiceName = "com.tcl.asr.manager.asrService";

    Button mButton_putong;
    Button mButton_sicuan;
    Button mButton_guangodng;
    Button mButton_henan;
    Button mButton_dongbei;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            AMSHookHelper.hookAMN();
            AMSHookHelper.hookActivityThread();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatus = (TextView) findViewById(R.id.TextView1);
        mButton_putong = (Button) findViewById(R.id.btn_putong);
        mButton_sicuan = (Button) findViewById(R.id.btn_sicuan);
        mButton_guangodng = (Button) findViewById(R.id.btn_guangdong);
        mButton_henan = (Button) findViewById(R.id.btn_henan);
        mButton_dongbei = (Button) findViewById(R.id.btn_dogbei);
        mButton_sicuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rcUiService.getRcUiService().setProperty("方言","四川话");
                try {
                    rcUiService.getRcUiService().getBinder().setDialects(rcUiService.LANGUAGE_CH_SICHUANGHUA);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mButton_putong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rcUiService.getRcUiService().setProperty("方言","普通话");
                try {
                    rcUiService.getRcUiService().getBinder().setDialects(rcUiService.LANGUAGE_CH_PUTONGHUA);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mButton_guangodng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rcUiService.getRcUiService().setProperty("方言","广东话");
                try {
                    rcUiService.getRcUiService().getBinder().setDialects(rcUiService.LANGUAGE_CH_GUNAGDONGHUA);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mButton_henan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rcUiService.getRcUiService().setProperty("方言","河南话");
                try {
                    rcUiService.getRcUiService().getBinder().setDialects(rcUiService.LANGUAGE_CH_HENANHUA);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mButton_dongbei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rcUiService.getRcUiService().setProperty("方言","东北话");
                try {
                    rcUiService.getRcUiService().getBinder().setDialects(rcUiService.LANGUAGE_CH_DONGBEIHUA);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        //sendMsgDelay(RC_KEY_LOOP,100);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public static void showText(String s) {
        mStatus.setText(s);
    }

    // The Handler that gets information back from
    public static Handler mHandler = new Handler(Looper.getMainLooper()) {
        String str = "";

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RC_KEY_DOWN:
                    String st1 = (String) (msg.obj);
                    showText("onShowText:" + st1);
                    //voiceKeyDown();
                    break;
                case RC_KEY_UP:
                    //byte[] b=(byte [])msg.obj;
                    String st2 = (String) (msg.obj);
                    showText("onShowText:" + st2);
                    //rcUiService.
                    break;
                case RC_KEY_LOOP:
                    //Log.d("MainActivity","------LUCK---");
                    //sendMsgDelay(RC_KEY_LOOP,100);
                    //sendMsgDelay(RC_KEY_LOOP,100);
                    break;


            }
        }
    };

    public static void sendMsgDelay(int cmd, int delay) {

        try {
            Message msg = mHandler.obtainMessage(cmd);
            mHandler.sendMessageDelayed(msg, delay);

        } catch (Exception e) {
            Log.d("MainActivity", "Error" + e.toString());
        }
    }


}
