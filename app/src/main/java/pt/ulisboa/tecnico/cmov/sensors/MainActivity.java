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
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

     SensorManager manager;
    double x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        manager.registerListener(this,manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),manager.SENSOR_DELAY_FASTEST);
        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                System.out.println("enviar");
                new serverRequestAddDistance().execute(String.valueOf(x),String.valueOf(y));
            }
        };
        Timer time = new Timer();
        time.scheduleAtFixedRate(timer,0,100);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            try {
                x = formater(event.values[0]);
                y = formater(event.values[1]);


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

    private class serverRequestAddDistance extends AsyncTask<String, Void, String> {

        @Override
      protected String doInBackground(String... params) {

            String urlServer = "http://192.168.0.14:8080" + "/move?x=";
            urlServer += params[0] + "&y=" + params[1];

            StringBuffer result = new StringBuffer("");
            try{
                URL url = new URL(urlServer);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                System.out.println(urlServer);
                while ((line = rd.readLine()) != null) result.append(line);

            }catch (SocketTimeoutException e) {
                return "FailedConnection";
            } catch(ConnectException e) {
                return "FailedConnection";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
