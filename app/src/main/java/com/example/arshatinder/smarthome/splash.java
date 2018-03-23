package com.example.arshatinder.smarthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import java.io.IOException;

public class splash extends Activity {

ImageView im;
WifiManager wifiManager;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Window win=getWindow();
        win.setStatusBarColor(Color.parseColor("#7092BF"));
        setContentView(R.layout.activity_splash);
        im=(ImageView)findViewById(R.id.splash_im);
        im.setImageResource(getResources().getIdentifier("splashweb","drawable", getPackageName()));
        new check().execute();
    }

    private boolean executeCommand(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 192.168.1.191");
            ProcessWithTimeout timer_process = new ProcessWithTimeout(mIpAddrProcess);
            int mExitValue = timer_process.waitForProcess(3000);
            if(mExitValue==0) return true;
            else return false;
        } catch (IOException e) {}
        return false;
    }

    public class ProcessWithTimeout extends Thread
    {
        private Process m_process;
        private int m_exitCode = Integer.MIN_VALUE;

        public ProcessWithTimeout(Process p_process)
        { m_process = p_process; }

        public int waitForProcess(int p_timeoutMilliseconds)
        {
            this.start();
            try { this.join(p_timeoutMilliseconds); }
            catch (InterruptedException e) { this.interrupt(); }
            return m_exitCode;
        }

        @Override
        public void run()
        {
            try { m_exitCode = m_process.waitFor(); } catch (InterruptedException ignore) {}
            catch (Exception ex) {}
        }
    }

    private class check extends AsyncTask<Void, Void, Void> {
        Class new_activity=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        }
        @Override
        protected Void doInBackground(Void... params) {
            if (wifiManager.isWifiEnabled()){
                if(executeCommand()){ new_activity=home.class; } else{ new_activity=home.class; }
            } else{ new_activity=login.class; }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(splash.this,new_activity));
            overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}