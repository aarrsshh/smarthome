package com.example.arshatinder.smarthome;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Arsh(Atinder) on 04-02-2017.
 */
public class camera extends Fragment {
    TextView t1;
    WebView myWebView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera, container, false);
        t1=(TextView)rootView.findViewById(R.id.cam_no);
        new isAvailable().execute();
        myWebView = (WebView) rootView.findViewById(R.id.cam);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.setFitsSystemWindows(true);
        myWebView.clearCache(true);
        myWebView.clearHistory();
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.loadUrl("http://192.168.0.102:8080/jsfs.html");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GridView grid= (GridView) getActivity().findViewById(R.id.grid);
                grid.setVisibility(View.GONE);
                ViewPager pager=(ViewPager) getActivity().findViewById(R.id.pager);
                pager.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                FloatingActionButton fab=(FloatingActionButton) getActivity().findViewById(R.id.fabhome);
                fab.setVisibility(View.GONE);
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                toolbar.setVisibility(View.GONE);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
    }

    private class isAvailable extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... params) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 192.168.0.102");
                int mExitValue = mIpAddrProcess.waitFor();
                if(mExitValue==0) return true;
                else return false;
            } catch (InterruptedException ignore) {}
            catch (IOException e) {}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result){ t1.setVisibility(View.GONE); }
        }
    }
}
