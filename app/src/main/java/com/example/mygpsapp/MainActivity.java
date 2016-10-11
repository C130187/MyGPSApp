package com.example.mygpsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

    Button btnShowLocation;
    TextView textView;
    TextView finalResult;

    GPSTracker gps;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
/*

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

*/
            btnShowLocation = (Button) findViewById(R.id.show_Location);
            textView = (TextView) findViewById(R.id.date_text);
            finalResult = (TextView) findViewById(R.id.result_text);


            btnShowLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gps = new GPSTracker(MainActivity.this);

                    if(gps.canGetLocation()){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        Toast.makeText(getApplicationContext(),"Your Location is -\nLatitude: " + latitude+ "\nLongitude: " + longitude,
                                Toast.LENGTH_SHORT).show();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = sdf.format(c.getTime());
                        textView.setText(strDate);
                        try {
                            String url = "http://10.27.117.155//MyLife/albums_browser/test.php?lat="+
                                    URLEncoder.encode(String.valueOf(latitude), "UTF-8")+"&lon="+
                                    URLEncoder.encode(String.valueOf(longitude),"UTF-8")+
                                    "&datetime=" + URLEncoder.encode(strDate,"UTF-8");
                            System.out.println("before execution");
                            NetworkConnect nc=  new NetworkConnect();
                            nc.execute(url);
                            System.out.println("after execution");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        gps.showSettingsAlert();
                    }
                }
            });
        }


    public class NetworkConnect extends AsyncTask<String,String, String> {


        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping...");
            String url = params[0];
            //publishProgress(url);
            System.out.println("url:"+url);
            try {

                URLConnection connection = new URL(url).openConnection();
                InputStream response = connection.getInputStream();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "some string";
        }

        @Override
        protected void onProgressUpdate(String... values){
            finalResult.setText(values[0]);
        }


        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }


    }

}

