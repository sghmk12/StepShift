package com.sathirak.stepshift;

import android.content.BroadcastReceiver;
import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sathirak.stepshift.Services.StepRecorderService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShiftDisplayActivity extends AppCompatActivity{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Boolean isSupported = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);

        Boolean isSupported2 = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);

        if(isSupported&&isSupported2){

        Intent intent = new Intent(this, StepRecorderService.class);
        startService(intent);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final TextView stepText = (TextView)findViewById(R.id.stepText);
        final TextView stepText2 = (TextView)findViewById(R.id.stepText2);
        final TextView timeText = (TextView)findViewById(R.id.timeText);

        //stepText.setText(String.valueOf(sharedPreferences.getFloat("steps",0)));
        stepText2.setText(String.valueOf((int)sharedPreferences.getFloat("steps",0)));
        //timeText.setText(String.valueOf(secondsDilated(sharedPreferences.getFloat("steps2",0))));
            timeText.setText(secondsDilated(sharedPreferences.getFloat("steps",0)));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        stepText2.setText(String.valueOf((int)sharedPreferences.getFloat("steps",0)));
                        //stepText.setText(String.valueOf(sharedPreferences.getFloat("steps2",0)));

                        //timeText.setText(String.valueOf(secondsDilated(sharedPreferences.getFloat("steps",0))));
                        timeText.setText(secondsDilated(sharedPreferences.getFloat("steps",0)));

                    }
                }, new IntentFilter(StepRecorderService.ACTION_STEP_BROADCAST)
        );}else{
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog);
            builder.setTitle("Your phone does not support Step Tracking").setMessage("This app will close").setNeutralButton("ok", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                              android.os.Process.killProcess(android.os.Process.myPid());
                              System.exit(1);

                          }
                        }).setIcon(R.mipmap.time).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shift_display, menu);

                return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            /*case R.id.action_reset:
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog);
                builder.setTitle("Confirm Reset?").setMessage("Press Ok to reset Step Count").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ShiftDisplayActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putFloat("steps", 0);
                        editor.putFloat("steps2", 0);
                        editor.commit();

                        TextView stepText2 = (TextView)findViewById(R.id.stepText2);
                        TextView timeText = (TextView)findViewById(R.id.timeText);

                        stepText2.setText(String.valueOf(sharedPreferences.getFloat("steps",0)));
                        //timeText.setText(String.valueOf(secondsDilated(sharedPreferences.getFloat("steps2",0))));
                        timeText.setText(secondsDilated(sharedPreferences.getFloat("steps",0)));

                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(ShiftDisplayActivity.this, R.style.Theme_AppCompat_Dialog);
                        builder.setTitle("App step data has been cleared").setMessage("Restart your phone to clear device step data or it will return after a step is taken").setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setIcon(R.mipmap.time).show();


                    }
                }).setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setIcon(R.mipmap.time).show();
                return true;*/
            case R.id.action_info:
                startActivity(new Intent(this, Info.class));
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    public static String secondsDilated(float steps){
        /*double realTime = (0.762*steps)/1.38582;

        double speedInSpeedOfLight = .00000000462259794;

        double speedOfLight = 299792458;

        double timeExperienced = realTime / Math.sqrt(1-(speedInSpeedOfLight*speedInSpeedOfLight));

        int rounded = (int) Math.round(timeExperienced);

        return rounded;*/

        double speedOfLight = 299792458;
        double velocityWalking = 1.67;
        double stepDistance = 0.71628;

        double totalDistance = steps*stepDistance;
        double timeTaken = totalDistance/velocityWalking;


        BigDecimal velocity = new BigDecimal(String.valueOf(velocityWalking*velocityWalking));
        BigDecimal speedC = new BigDecimal(String.valueOf(speedOfLight*speedOfLight));
        BigDecimal ratioSpeed = velocity.divide(speedC,17,BigDecimal.ROUND_HALF_UP);
        ratioSpeed = ratioSpeed.setScale(17,RoundingMode.HALF_UP);
        BigDecimal one = new BigDecimal("1");
        one = one.setScale(17, RoundingMode.HALF_UP);
        BigDecimal subtraction = one.subtract(ratioSpeed);

        subtraction = subtraction.setScale(17,RoundingMode.HALF_UP);

        BigDecimal ratioFinal = bigSqrt(subtraction);
        ratioFinal = ratioFinal.setScale(18, RoundingMode.HALF_UP);

        BigDecimal realTime = new BigDecimal(String.valueOf(timeTaken));
        realTime = realTime.setScale(18,RoundingMode.HALF_UP);

        BigDecimal interpretedTime = realTime.multiply(ratioFinal);
        interpretedTime = interpretedTime.setScale(18, RoundingMode.HALF_UP);

        BigDecimal bigFinal = realTime.subtract(interpretedTime);

        BigDecimal femto = new BigDecimal("1000000000000");
        BigDecimal yes = femto.multiply(bigFinal);
        yes = yes.stripTrailingZeros();

        /*System.out.println(velocityWalking*velocityWalking/(speedOfLight*speedOfLight));
        System.out.println(1-velocityWalking*velocityWalking/(speedOfLight*speedOfLight));
        System.out.println(Math.sqrt(1-(velocityWalking*velocityWalking/(speedOfLight*speedOfLight))));*/

        System.out.println(velocity.toString());
        System.out.println(speedC.toString());
        System.out.println(ratioSpeed.toString());
        System.out.println(one.toString());
        System.out.println(subtraction.toString());
        System.out.println(ratioFinal.toString());
        System.out.println(realTime.toString());
        System.out.println(interpretedTime.toString());
        System.out.println(bigFinal.toString());

        if(yes.compareTo(new BigDecimal("0"))==0){
            return "0";
        }else{

        return yes.toString();}
    }




    private static final BigDecimal SQRT_DIG = new BigDecimal(150);
    private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

    /**
     * Private utility method used to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1){
            return xn1;
        }
        return sqrtNewtonRaphson(c, xn1, precision);
    }

    /**
     * Uses Newton Raphson to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    public static BigDecimal bigSqrt(BigDecimal c){
        return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
    }

    public void openInfo (View v){
        startActivity(new Intent(this, Info.class));
    }

}
