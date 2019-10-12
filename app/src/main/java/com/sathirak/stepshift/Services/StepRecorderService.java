package com.sathirak.stepshift.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.os.Process;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.sathirak.stepshift.ShiftDisplayActivity;

import java.util.Calendar;

/**
 * Created by chandana on 2016-11-23.
 */
public class StepRecorderService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private float currentSteps;
    private float oldValue = 0;
    public static final String
            ACTION_STEP_BROADCAST = StepRecorderService.class.getName() + "StepBroadcast";



    @Override
    public void onCreate() {
        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread("StepRecorderService",
                Process.THREAD_PRIORITY_BACKGROUND);
        // start the new handler thread
        thread.start();

        mServiceLooper = thread.getLooper();
        // start the service using the background handler
        mServiceHandler = new ServiceHandler(mServiceLooper);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        currentSteps = sharedPreferences.getFloat("steps",0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();

        // call a new service handler. The service ID can be used to identify the service
        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);

        return START_STICKY;
    }

    protected void showToast(final String msg){
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SaveStepPreferences(String key, float value){
        editor.putFloat("steps2", value);

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        float value2 = value-oldValue;
        oldValue = value;
        currentSteps= currentSteps+value2;

        editor.putFloat(key, currentSteps);

        //editor.clear();
        editor.commit();
        Intent intent = new Intent(ACTION_STEP_BROADCAST);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
    // Object responsible for
    private final class ServiceHandler extends Handler implements SensorEventListener{

        SensorManager sensorManager;

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Well calling mServiceHandler.sendMessage(message); from onStartCommand,
            // this method will be called.
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if(countSensor!=null){
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);

            }else{
                showToast("Count Sensor not available");
            }
            // Add your cpu-blocking activity here
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            SaveStepPreferences("steps",sensorEvent.values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
