package pt.ulisboa.tecnico.cmov.sensors;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentSender;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

     SensorManager manager;
    double x,y,z;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        manager.registerListener(this,manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),manager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            try {
                textView = (TextView) findViewById(R.id.x);
                textView.setText(String.valueOf(formater(x)));

                textView = (TextView) findViewById(R.id.y);
                textView.setText(String.valueOf(formater(y)));

                textView = (TextView) findViewById(R.id.z);
                textView.setText(String.valueOf(formater(z)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    public double formater(double number) throws ParseException {
        NumberFormat decimal = new DecimalFormat("#.000");
//        String format = decimal.format(auxNumer);
        double finalValue = decimal.parse(String.valueOf(number)).doubleValue();
        return  finalValue;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
