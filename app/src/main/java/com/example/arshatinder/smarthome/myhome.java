package com.example.arshatinder.smarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Arsh(Atinder) on 04-02-2017.
 */
public class myhome extends Fragment {
    TextView date,day,temp,hum,devices,power;
    RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.myhome, container, false);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
        String dat = sdf.format(new Date());
        String dat2 = sdf2.format(new Date());
        date=(TextView)rootView.findViewById(R.id.myhometext1);
        day=(TextView)rootView.findViewById(R.id.myhometext2);
        temp=(TextView)rootView.findViewById(R.id.myhometext3);
        hum=(TextView)rootView.findViewById(R.id.myhometext4);
        devices=(TextView)rootView.findViewById(R.id.dev);
        power=(TextView)rootView.findViewById(R.id.power);
        layout=(RelativeLayout)rootView.findViewById(R.id.alert);
        layout.setBackgroundResource(R.drawable.night);
        date.setText(dat);
        day.setText(dat2);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp= this.getActivity().getSharedPreferences("pins", Context.MODE_PRIVATE);
        devices.setText(String.valueOf(sp.getInt("devs",0)));
        Thread d=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (;;) {
                        new get_temp().execute();
                        new get_hum().execute();
                        Thread.sleep(15000);
                    }
                }catch (Exception ex){}
            }
        });
        d.start();
    }

    private class get_temp extends AsyncTask<Void, Void, String>{
        String x=null;

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("https://thingspeak.com/channels/295542/fields/1/last");
                HttpResponse response = httpClient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(instream, "ISO-8859-1"));
                    x=br.readLine();
                }
            }catch(Exception ex){}
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            temp.setText(s);
        }
    }

    private class get_hum extends AsyncTask<Void, Void, String>{
        String x=null;

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("https://thingspeak.com/channels/295542/fields/2/last");
                HttpResponse response = httpClient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(instream, "ISO-8859-1"));
                    x=br.readLine();
                }
            }catch(Exception ex){}
            return x;
        }

        @Override
        protected void onPostExecute(String s) {
            hum.setText(s);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
