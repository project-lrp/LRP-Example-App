package com.example.arcontroller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.lrptest.daemon.ILrpBoundService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SettingsActivity extends AppCompatActivity {

    final String TAG = "LRP_Demo_App";

    ILrpBoundService mBoundService = null;
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(16);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Button btn_pause = (Button) findViewById(R.id.demostart);
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new Thread(new ClientSend("PauseGame")).start();
                demoStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Initiating LRP service bind");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.lrptest.daemon","com.lrptest.daemon.LrpService"));
        bindService(intent, boundServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void demoStart(){
        Runnable lrpinit = () -> {
            try {
                mBoundService.startParInf();
                Log.d(TAG, "LRP startParInf");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        lrpinit.run();

        // The next packet is estimated to be sent 50ms later
        int esti_delay = 1000;
        Runnable lrpcall = () -> {
            try {
                mBoundService.reduceDozeAndSchedule(System.nanoTime(), esti_delay);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        ses.scheduleAtFixedRate(lrpcall, 0, esti_delay, TimeUnit.MILLISECONDS);
        ses.scheduleAtFixedRate(new ClientSend("Data"), 0, esti_delay, TimeUnit.MILLISECONDS);
    }


    private final ServiceConnection boundServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "LRP service connected");
            mBoundService =  ILrpBoundService.Stub.asInterface(service);
            Toast.makeText(getApplicationContext(), "AIDL service connected", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "LRP service disconnected");
            mBoundService = null;
            Toast.makeText(getApplicationContext(), "AIDL service disconnected", Toast.LENGTH_SHORT).show();
        }
    };


}