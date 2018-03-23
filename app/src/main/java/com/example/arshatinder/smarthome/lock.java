package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class lock extends AppCompatActivity {

int i=0,max,pass,sms_en,max_attempt=0;
TextView text1;
String phone,image_back;
ImageView im1,im2,im3,im4,im5,im6,im7,im8,im9,im0,pin1,pin2,pin3,pin0,im;
SharedPreferences sp;
int[] lock_comb = new int[4];
int[] lock_input = new int[4];
ImageView[] pins = new ImageView[4];

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        sp = getSharedPreferences("lock", Context.MODE_PRIVATE);
        int lock=sp.getInt("en",0);
        if(lock == 0){
            startActivity(new Intent(lock.this,splash.class));
            overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
            finish();
        }

        setContentView(R.layout.activity_lock);
        Window win=getWindow();
        win.setStatusBarColor(Color.parseColor("#2D2D2D"));

        phone=sp.getString("phone",null);
        max=sp.getInt("max",1);
        pass=sp.getInt("pass",1234);
        sms_en=sp.getInt("sms_en",0);
        image_back=sp.getString("background","back_lock");

        double num = Math.pow(10,3);
        int k = (int)num;
        int j=0;
        for (;k>0;k/=10) {
            lock_comb[j]=pass / k % 10;
            j++;
        }

        im=(ImageView)findViewById(R.id.lock_im);
        im.setImageResource(getResources().getIdentifier(image_back,"drawable", getPackageName()));
        text1=(TextView)findViewById(R.id.pin_text_new);
        text1.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pin0=(ImageView)findViewById(R.id.pin1);
        pin1=(ImageView)findViewById(R.id.pin2);
        pin2=(ImageView)findViewById(R.id.pin3);
        pin3=(ImageView)findViewById(R.id.pin4);
        pins[0]=pin0;
        pins[1]=pin1;
        pins[2]=pin2;
        pins[3]=pin3;
        im1=(ImageView)findViewById(R.id.key1);
        im2=(ImageView)findViewById(R.id.key2);
        im3=(ImageView)findViewById(R.id.key3);
        im4=(ImageView)findViewById(R.id.key4);
        im5=(ImageView)findViewById(R.id.key5);
        im6=(ImageView)findViewById(R.id.key6);
        im7=(ImageView)findViewById(R.id.key7);
        im8=(ImageView)findViewById(R.id.key8);
        im9=(ImageView)findViewById(R.id.key9);
        im0=(ImageView)findViewById(R.id.key0);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(1);
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(2);
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(3);
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(4);
            }
        });
        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(5);
            }
        });
        im6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(6);
            }
        });
        im7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(7);
            }
        });
        im8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(8);
            }
        });
        im9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(9);
            }
        });
        im0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setVisibility(View.INVISIBLE);
                check(0);
            }
        });
    }

    void check(int k){
        if(i<=3) {
            lock_input[i]=k;
            pins[i].setBackground(getResources().getDrawable(R.mipmap.filled_pin));
            i++;
        } if(i==4) {
            max_attempt++;
            int temp = 0;
            for (int j = 0; j <= 3; j++) {
                if (lock_comb[j] == lock_input[j])
                    temp++;
            }
            if (temp == 4) {
                startActivity(new Intent(lock.this, splash.class));
                overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                finish();
            } else {
                text1.setVisibility(View.VISIBLE);
                setanime();
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                i = 0;
                if(max_attempt>max && sms_en==1){
                    SmsManager smsManager = SmsManager.getDefault();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    smsManager.sendTextMessage(phone, null, "An Intruder Attempts to use Smart Home App on "+formattedDate, null, null);
                }
                for (int j = 0; j <= 3; j++)
                    pins[j].setBackground(getResources().getDrawable(R.mipmap.empty_pin));
            }
        }
    }

    void setanime()
    {
        Animation anime=AnimationUtils.loadAnimation(this,R.anim.shake);
        im1.startAnimation(anime);
        im2.startAnimation(anime);
        im3.startAnimation(anime);
        im4.startAnimation(anime);
        im5.startAnimation(anime);
        im6.startAnimation(anime);
        im7.startAnimation(anime);
        im8.startAnimation(anime);
        im9.startAnimation(anime);
        im0.startAnimation(anime);
        pin0.startAnimation(anime);
        pin1.startAnimation(anime);
        pin2.startAnimation(anime);
        pin3.startAnimation(anime);
        text1.startAnimation(anime);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
