package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class login extends AppCompatActivity {
    EditText mail,pass;
    ImageView login;
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    TextView for_pass;
    int action=0;
    InputStream is=null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy tp=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Window win=getWindow();
        win.setStatusBarColor(Color.parseColor("#585B5A"));
        setContentView(R.layout.activity_login);
        if(!checkInternetConnection()) showSnack(false);
        mail=(EditText)findViewById(R.id.loglogin);
        pass=(EditText)findViewById(R.id.logpass);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        for_pass=(TextView)findViewById(R.id.forgetpass);
        login=(ImageView)findViewById(R.id.logbutton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!checkInternetConnection()) showSnack(false);
                else if(action==1){
                    if(validateEmail()){
                        String user=mail.getText().toString();
                        ArrayList<NameValuePair>al=new ArrayList<>();
                        al.add(new BasicNameValuePair("mail", user));
                        if(getdata("http://smarthome01.freeiz.com/for_pass.php",al)) {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_frame), "Password has been sent to your Inbox. Please check it out!!", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        }else {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_frame), "Please Enter Valid Username!!", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.RED);
                            snackbar.show();
                        }
                    }
                }
                else{
                    String user=mail.getText().toString();
                    String password=pass.getText().toString();
                    login(user,password);
                }
                return false;
            }
        });
        for_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(action==0){
                    inputLayoutPassword.setVisibility(View.GONE);
                    for_pass.setText("Login Again");
                    action=1;
                }
                else {
                    action=0;
                    inputLayoutPassword.setVisibility(View.VISIBLE);
                    for_pass.setText("Forget Password?");
                }
            }
        });
    }

    private boolean submitForm() {
        if (!validateEmail()) return false;
        if (!validatePassword()) return false;
        return true;
    }

    private boolean validateEmail() {
        String email = mail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError("Enter Valid Username");
            requestFocus(mail);
            return false;
        } else inputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword() {
        if (pass.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Enter Password");
            requestFocus(pass);
            return false;
        } else inputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) return true;
        return false;
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }
        Snackbar snackbar = Snackbar.make(findViewById(R.id.login_frame), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(Float.parseFloat("16"));
        textView.setTextColor(color);
        snackbar.show();
    }

    void setanime() {
        Animation anime = AnimationUtils.loadAnimation(this, R.anim.shake);
        inputLayoutPassword.startAnimation(anime);
        inputLayoutEmail.startAnimation(anime);
        mail.startAnimation(anime);
        pass.startAnimation(anime);
        for_pass.startAnimation(anime);
        login.startAnimation(anime);
    }

    boolean getdata(String url, ArrayList al) {
        try {
            HttpClient hc=new DefaultHttpClient();
            HttpPost hp=new HttpPost(url);
            hp.setEntity(new UrlEncodedFormEntity(al));
            HttpResponse hr=hc.execute(hp);
            HttpEntity en=hr.getEntity();
            is=en.getContent();
        } catch (Exception e) { Toast.makeText(this,e+"",Toast.LENGTH_LONG).show(); }
        String data="";
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"ISO-8859-1"));
            StringBuilder sb=new StringBuilder();
            String x="";
            while((x=br.readLine())!=null) { sb.append(x+"\n"); }
            data=sb.toString();
            if(data.contains("unsuccess")) { return false; } else { return true; }
        } catch (Exception e) { Toast.makeText(this, e+"",Toast.LENGTH_LONG).show(); }
        return false;
    }

    private void login(final String username, String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected String doInBackground(String... params) {
                String data = "";
                try {
                    String user = params[0];
                    String password = params[1];
                    ArrayList<NameValuePair> al = new ArrayList<>();
                    al.add(new BasicNameValuePair("username", user));
                    al.add(new BasicNameValuePair("password", password));
                    HttpClient hc = new DefaultHttpClient();
                    HttpPost hp = new HttpPost("http://smarthome01.freeiz.com/login.php");
                    hp.setEntity(new UrlEncodedFormEntity(al));
                    HttpResponse hr = hc.execute(hp);
                    HttpEntity en = hr.getEntity();
                    is = en.getContent();
                } catch (Exception e) { Toast.makeText(login.this, e + "", Toast.LENGTH_LONG).show(); }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
                    StringBuilder sb = new StringBuilder();
                    String x = "";
                    while ((x = br.readLine()) != null) {
                        sb.append(x + "\n");
                    }
                    data = sb.toString();
                } catch (Exception e) { Toast.makeText(login.this, e + "", Toast.LENGTH_LONG).show(); }
                return data;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(login.this, "", "Authenticating");
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                if (!s.contains("unsuccess") && submitForm()) {
                    SharedPreferences sp=getSharedPreferences("user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putString("mail",mail.getText().toString());
                    edit.putString("pass",pass.getText().toString());
                    edit.commit();
                    startActivity(new Intent(login.this,home.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else {
                    loadingDialog.dismiss();
                    setanime();
                    Vibrator vibrate=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrate.vibrate(300);
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.login_frame),"Invalid Username or Password!!", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextSize(Float.parseFloat("16"));
                    textView.setTextColor(Color.RED);
                    snackbar.show();
                }
            }
        }
        LoginAsync la = new LoginAsync();
        la.execute(username, password);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
