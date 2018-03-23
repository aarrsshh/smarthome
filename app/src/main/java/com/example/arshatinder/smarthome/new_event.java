package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class new_event extends AppCompatActivity {
    String[] imp={"Normal","High"};
    EditText sum,note;
    Spinner impor;
    TextView done,cancel,duedate,deadline,remain_date,remain_time;
    String name;
    int mHour,mMinute,myear,mmonth,mday,hh,mon,yyyy,mm,dd;
    Calendar c;
    int id=0;
    SharedPreferences sp;
    final static int RQS_1 = 1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Window win = getWindow();
        win.setStatusBarColor(Color.parseColor("#2FBF3C"));

        sum=(EditText)findViewById(R.id.summary);
        note=(EditText)findViewById(R.id.note);
        duedate=(TextView)findViewById(R.id.date);
        deadline=(TextView)findViewById(R.id.exdate);
        impor=(Spinner)findViewById(R.id.importance);
        done=(TextView)findViewById(R.id.done);
        cancel=(TextView)findViewById(R.id.cancel);
        remain_date=(TextView)findViewById(R.id.remainder_date);
        remain_time=(TextView)findViewById(R.id.remainder_time);

        ArrayAdapter<String> importance=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,imp);
        impor.setAdapter(importance);
        remain_time.setVisibility(View.GONE);

        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        duedate.setText(dateFormatter.format(now));
        deadline.setText(dateFormatter.format(now));

        sp=getSharedPreferences("event",MODE_PRIVATE);
        id=sp.getInt("id",0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        duedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name="duedate";
                setDate(v);
            }
        });
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name="deadline";
                setDate(v);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new_event.this,event.class));
                overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sum.getText().toString().equals("")) {
                    Toast.makeText(new_event.this, "Add Summary Field has to be filled", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar current = Calendar.getInstance();
                    Calendar cal = Calendar.getInstance();
                    cal.set(yyyy,mon,dd,hh,mm,00);
                    if(cal.compareTo(current) <= 0){ Toast.makeText(getApplicationContext(), "Invalid Date/Time ", Toast.LENGTH_LONG).show();
                    } else{
                        setAlarm(cal);
                        try {
                            File dir = new File(Environment.getExternalStorageDirectory(), "/SmartHome/");
                            if (!dir.exists()) {
                                try {
                                    dir.mkdir();
                                } catch (SecurityException se) {
                                }
                            }
                            File file = new File(Environment.getExternalStorageDirectory(), "/SmartHome/cache_event");
                            if (!file.exists())
                                file.createNewFile();
                            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.append(sum.getText().toString() + "$" + note.getText().toString() + "$" + duedate.getText().toString() + "$" + deadline.getText().toString() + "$"
                                    + impor.getSelectedItem().toString() + "$" + remain_date.getText().toString() + "$" + remain_time.getText().toString() + "\n");
                            bw.close();
                        } catch (IOException e) { e.printStackTrace(); }
                    }
                    startActivity(new Intent(new_event.this, event.class));
                    overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
                    finish();
                }
            }
        });
        remain_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name="remainder";
                remain_time.setVisibility(View.VISIBLE);
                setDate(v);
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            c=Calendar.getInstance();
            myear=c.get(Calendar.YEAR);
            mmonth=c.get(Calendar.MONTH);
            mday=c.get(Calendar.DATE);
            return new DatePickerDialog(this,myDateListener, myear, mmonth, mday);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            if(name.equals("duedate")){
                duedate.setText(arg3+"/"+arg2+1+"/"+arg1);
            } else if(name.equals("deadline")){
                deadline.setText(arg3+"/"+arg2+1+"/"+arg1);
            } else if(name.equals("remainder")) {
                yyyy = arg1;
                mon = arg2;
                dd = arg3;
                remain_date.setText(arg3 + "/" + arg2 + 1 + "/" + arg1);
                c= Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(new_event.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                remain_time.setText(hourOfDay + ":" + minute);
                                hh = hourOfDay;
                                mm = minute;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(new_event.this,event.class));
        overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
        finish();
    }

    private void setAlarm(Calendar targetCal){
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.setData(Uri.parse("alarm:" + (id=id+1)));
        sp=getSharedPreferences("event",MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.putInt("id",id);
        edit.commit();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
