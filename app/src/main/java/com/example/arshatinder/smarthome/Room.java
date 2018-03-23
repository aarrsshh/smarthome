package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Room extends AppCompatActivity {

    GridView grid;
    String[] web;
    int[] part,but;
    int[] digital_pins=new int[24];
    String title;
    int image,bulb,fan,door,sec_door,devs=0;
    LinearLayout layout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy tp=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        setContentView(R.layout.activity_room);
        SharedPreferences sp = getSharedPreferences("Title", Context.MODE_PRIVATE);
        title = sp.getString("title", "NULL");
        image = sp.getInt("im", -1);
        setTitle(" "+title);
        setup();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp2=getSharedPreferences("pins",MODE_PRIVATE);
        devs=sp2.getInt("devs",0);

        layout = (LinearLayout) findViewById(R.id.lay);
        layout.setBackground(getDrawable(image));

        CustomGrid adapter = new CustomGrid(Room.this, web, part, but);
        grid = (GridView) findViewById(R.id.grid2);
        grid.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (web[position].equals("Light")) {
                    if (part[position]== R.mipmap.bulb_off) {
                        part[position]=R.mipmap.bulb_on;
                        ++devs;
                        set_pins(web[position],1);
                        toast("   Light is Turned On   ");}
                    else{
                        part[position]=R.mipmap.bulb_off;
                        --devs;
                        set_pins(web[position],0);
                        toast("   Light is Turned Off   ");}
                }  else if (web[position].equals("Door") || web[position].equals("Back Door")) {
                    if (part[position] == R.mipmap.door_open) {
                        part[position] = R.mipmap.door_close;
                        --devs;
                        set_pins(web[position],0);
                        toast("   Door is Closed   ");}
                    else{
                        part[position] = R.mipmap.door_open;
                        ++devs;
                        set_pins(web[position],1);
                        toast("   Door is Opened   ");}
                } else if (web[position].equals("Garage Door")) {
                    if (part[position] == R.mipmap.garage_open) {
                        --devs;
                        set_pins(web[position],0);
                        part[position] = R.mipmap.garage_close;
                        toast("   Garage Door is Closed   ");
                    } else {
                        part[position] = R.mipmap.garage_open;
                        ++devs;
                        set_pins(web[position],1);
                        toast("   Garage Door is Opened   ");
                    }
                } else if(web[position].equals("Fan")) {
                    if (but[position]==1){
                        ++devs;
                        set_pins(web[position],1);
                        but[position]=2;
                        toast("   Fan is Turned On   ");}
                    else{
                        but[position]=1;
                        --devs;
                        set_pins(web[position],0);
                        toast("   Fan is Turned Off   ");}
                }
                CustomGrid adapter = new CustomGrid(Room.this, web, part, but);
                grid.setAdapter(adapter);
            }
        });
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (web[position].equals("Light")) {
                    if(part[position] == R.mipmap.bulb_on) {
                        ColorPickerDialogBuilder
                            .with(Room.this)
                            .setTitle("Choose color")
                            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                            .density(10)
                            .setPositiveButton("OK", new ColorPickerClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                }
                                })
                            .build()
                            .show();
                    }
                } else if(web[position].equals("Fan")) {
                    if(but[position]==2) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(Room.this);
                        LayoutInflater li = getLayoutInflater();
                        View layout = li.inflate(R.layout.seekbar, (ViewGroup) findViewById(R.id.seek_lay));
                        final Croller croller = (Croller) layout.findViewById(R.id.croller);
                        croller.setProgress(0);
                        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                            @Override
                            public void onProgressChanged(int progress) {
                                croller.setLabel(String.valueOf(progress));
                            }
                        });
                        ad.setView(layout);
                        ad.setTitle("Select Speed");
                        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    }
                } else if(web[position].equals("Garage Door")){
                    AlertDialog.Builder ad=new AlertDialog.Builder(Room.this);
                    LayoutInflater li = getLayoutInflater();
                    View layout = li.inflate(R.layout.garagedoor,
                            (ViewGroup) findViewById(R.id.garage_door_layout));
                    ad.setView(layout);
                    ad.setTitle("Garage Lift Options");
                    ImageView up=(ImageView) layout.findViewById(R.id.garage_door_up);
                    up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Room.this,"UP Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ImageView down=(ImageView) layout.findViewById(R.id.garage_door_down);
                    down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Room.this,"Down Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });ad.show();
                }
                return true;
            }
        });
    }

    public void toast(String msg) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView text = (TextView) layout.findViewById(R.id.custom_toast_message);
        text.setText(msg);
        final Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        int toastDurationInMilliSeconds = 500;
        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }
            public void onFinish() {
                toast.cancel();
            }
        };
        toast.show();
        toastCountDown.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Room.this,home.class));
            overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Room.this,home.class));
        overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
        finish();
    }

    public void setup(){
        switch (title){
            case "Lobby":
                get_pins(1,3);
                web=new String[] {"Light","Fan","Door"};
                if(digital_pins[1]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[2]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[3]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                part=new int[] {bulb, R.mipmap.fan, door};
                but=new int[] {0,fan,0};
                break;
            case "Drawing Room":
                get_pins(4,3);
                web=new String[] {"Light","Fan","Door"};
                if(digital_pins[4]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[5]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[6]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                part=new int[] {bulb, R.mipmap.fan, door};
                but=new int[] {0,fan,0};
                break;
            case "Kitchen":
                get_pins(7,3);
                web=new String[] {"Light","Fan","Door"};
                if(digital_pins[7]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[8]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[9]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                part=new int[] {bulb, R.mipmap.fan, door};
                but=new int[] {0,fan,0};
                break;
            case "Bed Room":
                get_pins(10,3);
                web=new String[] {"Light","Fan","Door"};
                if(digital_pins[10]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[11]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[12]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                part=new int[] {bulb, R.mipmap.fan, door};
                but=new int[] {0,fan,0};
                break;
            case "Bath Room":
                get_pins(13,3);
                web=new String[] {"Light","Fan","Door"};
                if(digital_pins[13]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[14]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[15]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                part=new int[] {bulb, R.mipmap.fan, door};
                but=new int[] {0,fan,0};
                break;
            case "Garage":
                get_pins(16,4);
                web=new String[] {"Light","Fan","Back Door","Garage Door"};
                if(digital_pins[16]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[17]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[18]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                if(digital_pins[19]==1){ sec_door=R.mipmap.garage_open; } else{ sec_door=R.mipmap.garage_close; }
                part=new int[] {bulb, R.mipmap.fan, door, sec_door};
                but=new int[] {0,fan,0,0};
                break;
            case "Porch":
                get_pins(20,4);
                web=new String[] {"Light","Fan","Main Door","Door"};
                if(digital_pins[20]==1){ bulb=R.mipmap.bulb_on; } else{ bulb=R.mipmap.bulb_off; }
                if(digital_pins[21]==1){ fan=2; } else{ fan=1; }
                if(digital_pins[22]==1){ door=R.mipmap.door_open; } else{ door=R.mipmap.door_close; }
                if(digital_pins[23]==1){ sec_door=R.mipmap.garage_open; } else{ sec_door=R.mipmap.garage_close; }
                part=new int[] {bulb, R.mipmap.fan, door, sec_door};
                but=new int[] {0,fan,0,0};
                break;
        }
    }

    private class arduino extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpGet httpGet = new HttpGet();
                httpGet.setURI(new URI(params[0]));
                httpclient.execute(httpGet);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void set_pins(String name, int value){
        String id=null;
        switch (title){
            case "Lobby":
                switch (name){
                    case "Light":
                        id="pin1";
                        break;
                    case "Fan":
                        id="pin2";
                        break;
                    case "Door":
                        id="pin3";
                        break;
                }
                break;
            case "Drawing Room":
                switch (name){
                    case "Light":
                        id="pin4";
                        break;
                    case "Fan":
                        id="pin5";
                        break;
                    case "Door":
                        id="pin6";
                        break;
                }
                break;
            case "Kitchen":
                switch (name){
                    case "Light":
                        id="pin7";
                        break;
                    case "Fan":
                        id="pin8";
                        break;
                    case "Door":
                        id="pin9";
                        break;
                }
                break;
            case "Bed Room":
                switch (name){
                    case "Light":
                        id="pin10";
                        break;
                    case "Fan":
                        id="pin11";
                        break;
                    case "Door":
                        id="pin12";
                        break;
                }
                break;
            case "Bath Room":
                switch (name){
                    case "Light":
                        id="pin13";
                        break;
                    case "Fan":
                        id="pin14";
                        break;
                    case "Door":
                        id="pin15";
                        break;
                }
                break;
            case "Garage":
                switch (name){
                    case "Light":
                        id="pin16";
                        break;
                    case "Fan":
                        id="pin17";
                        break;
                    case "Back Door":
                        id="pin18";
                        break;
                    case "Garage Door":
                        id="pin19";
                        break;
                }
                break;
            case "Porch":
                switch (name) {
                    case "Light":
                        id = "pin20";
                        break;
                    case "Fan":
                        id = "pin21";
                        break;
                    case "Door":
                        id = "pin22";
                        break;
                    case "Main Door":
                        id = "pin23";
                        break;
                }
                break;
        }
        SharedPreferences sp=getSharedPreferences("pins",MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.putInt(id,value);
        edit.putInt("devs",devs);
        edit.commit();
    }

    public void get_pins(int start, int length) {
        for (int i = start; i <= start+length-1; i++) {
            SharedPreferences sp = getSharedPreferences("pins", MODE_PRIVATE);
            digital_pins[i] = sp.getInt("pin" + i, 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
