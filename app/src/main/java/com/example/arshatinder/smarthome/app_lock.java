package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class app_lock extends AppCompatActivity {

SwitchCompat sc1,sc2;
SharedPreferences sp;
LinearLayout pass,phno,max,bckgrd;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_app_lock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Window win=getWindow();
        win.setStatusBarColor(Color.parseColor("#FF5733"));

        sc1=(SwitchCompat)findViewById(R.id.switch1);
        sc2=(SwitchCompat)findViewById(R.id.switch2);
        sp = getSharedPreferences("lock", Context.MODE_PRIVATE);
        int lock=sp.getInt("en",0);
        int sms=sp.getInt("sms_en",0);
        if(lock == 1) sc1.setChecked(true);
        if(sms == 1) sc2.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pass=(LinearLayout)findViewById(R.id.aoio2);
        phno=(LinearLayout)findViewById(R.id.aoio4);
        max=(LinearLayout)findViewById(R.id.aoio5);
        bckgrd=(LinearLayout)findViewById(R.id.aoio6);
        sc1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putInt("en",1);
                    edit.commit();
                }
                else{
                    sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putInt("en",0);
                    edit.commit();
                    sc2.setChecked(false);
                }
            }
        });
        sc2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putInt("sms_en",1);
                    edit.commit();
                }
                else{
                    sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putInt("sms_en",0);
                    edit.commit();
                }
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final EditText password=new EditText(app_lock.this);
                password.setInputType(InputType.TYPE_CLASS_NUMBER);
                password.setMaxEms(10);
                password.setLetterSpacing(Float.parseFloat("0.2"));
                AlertDialog.Builder ad=new AlertDialog.Builder(app_lock.this);
                ad.setTitle("Change App_lock Password");
                ad.setMessage("Enter your New Password");
                ad.setView(password, 40, 0, 50, 0);
                ad.setPositiveButton("Change",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!password.getText().equals("") || password.getText().length() == 4) {
                            sp = getSharedPreferences("lock", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putInt("pass", Integer.parseInt(password.getText().toString()));
                            edit.commit();
                        }else{ Toast.makeText(app_lock.this,"Length of Password is Invalid",Toast.LENGTH_SHORT).show(); }
                    }
                });
                ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });ad.show();
            }
        });
        phno.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final EditText phone=new EditText(app_lock.this);
                phone.setInputType(InputType.TYPE_CLASS_NUMBER);
                phone.setLetterSpacing(Float.parseFloat("0.2"));
                phone.setMaxEms(10);
                AlertDialog.Builder ad=new AlertDialog.Builder(app_lock.this);
                ad.setTitle("Phone Number");
                ad.setMessage("Enter Phone Number to whom you want to send Alert!");
                ad.setView(phone, 40, 0, 50, 0);
                ad.setPositiveButton("Done",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!phone.getText().equals("") || Integer.parseInt(phone.getText().toString()) > 0 || phone.getText().length() == 10 ) {
                            sp = getSharedPreferences("lock", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("phone", phone.getText().toString());
                            edit.commit();
                        }else{ Toast.makeText(app_lock.this,"Please Enter Correct Phone Number",Toast.LENGTH_SHORT).show(); }
                    }
                });
                ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });ad.show();
            }
        });
        max.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final EditText max=new EditText(app_lock.this);
                max.setInputType(InputType.TYPE_CLASS_NUMBER);
                max.setMaxEms(10);
                max.setLetterSpacing(Float.parseFloat("0.2"));
                AlertDialog.Builder ad=new AlertDialog.Builder(app_lock.this);
                ad.setTitle("Change Max Attempts");
                ad.setMessage("Enter new Maximum number of times user can Attempt!");
                ad.setView(max, 40, 0, 50, 0);
                ad.setPositiveButton("Change",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!max.getText().equals("") || Integer.parseInt(max.getText().toString()) > 0) {
                            sp = getSharedPreferences("lock", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putInt("max", Integer.parseInt(max.getText().toString()));
                            edit.commit();
                        }else{
                            Toast.makeText(app_lock.this,"Please Enter Max Attempts",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });ad.show();
            }
        });
        bckgrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog ad=new Dialog(app_lock.this);
                LayoutInflater li = getLayoutInflater();
                final View layout = li.inflate(R.layout.change_background,
                        (ViewGroup) findViewById(R.id.chk));
                ad.setContentView(layout);
                ad.setTitle("Choose App Lock Background Wallpaper");
                ImageView im1=(ImageView) layout.findViewById(R.id.chk1);
                im1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("background","back_lock_small");
                        edit.commit();
                        ad.dismiss();
                    }
                });
                ImageView im2=(ImageView) layout.findViewById(R.id.chk2);
                im2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("background","back_lock2_small");
                        edit.commit();
                        ad.dismiss();
                    }
                });
                ImageView im3=(ImageView) layout.findViewById(R.id.chk3);
                im3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("background","back_lock3_small");
                        edit.commit();
                        ad.dismiss();
                    }
                });
                ImageView im4=(ImageView) layout.findViewById(R.id.chk4);
                im4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp=getSharedPreferences("lock", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("background","back_lock4_small");
                        edit.commit();
                        ad.dismiss();
                    }
                });
                ad.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(app_lock.this,home.class));
            overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(app_lock.this,home.class));
        overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}