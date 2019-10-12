package com.sathirak.stepshift.Services;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.sathirak.stepshift.Services.StepRecorderService;

public class ServiceManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, StepRecorderService.class);
            context.startService(pushIntent);
        }
    }
}
