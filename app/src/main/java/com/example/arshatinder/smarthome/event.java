package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class event extends AppCompatActivity {
    ListView listView;
    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String> subtitle=new ArrayList<String>();
    ArrayList<String> start=new ArrayList<String>();
    ArrayList<String> dead=new ArrayList<String>();
    ArrayList<String> rem_date=new ArrayList<String>();
    ArrayList<String> rem_time=new ArrayList<String>();
    TextView tv1,tv2,tv3,notask;
    String imp,alarm;
    SwipyRefreshLayout swipe_referesh;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_event);
        Window win = getWindow();
        win.setStatusBarColor(Color.parseColor("#884EA0"));
        notask=(TextView)findViewById(R.id.notask);
        notask.setVisibility(View.GONE);
        title.clear();
        subtitle.clear();
        start.clear();
        dead.clear();
        rem_date.clear();
        rem_time.clear();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#884EA0")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(event.this,new_event.class));
                overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(event.this,home.class));
            overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        swipe_referesh = (SwipyRefreshLayout)findViewById(R.id.swipe_refresh_event);
        swipe_referesh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                readcache();
            }
        });
        title.clear();
        subtitle.clear();
        start.clear();
        dead.clear();
        rem_date.clear();
        rem_time.clear();
        readcache();
        if(title.isEmpty())
            notask.setVisibility(View.VISIBLE);
        final CustomList adapter = new CustomList(event.this, title, subtitle, start, dead,alarm);
        listView = (ListView) findViewById(R.id.listView);
        tv1 = (TextView) listView.findViewById(R.id.list_text);
        tv2 = (TextView) listView.findViewById(R.id.start_date);
        tv3 = (TextView) listView.findViewById(R.id.deadline);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeLineFrom(title.get(position)+"$"+subtitle.get(position));
                title.remove(position);
                subtitle.remove(position);
                start.remove(position);
                dead.remove(position);
                rem_time.remove(position);
                rem_date.remove(position);
                final CustomList adapter = new CustomList(event.this, title, subtitle, start, dead, alarm);
                listView.setAdapter(adapter);
                if(title.isEmpty())
                    notask.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    public void readcache() {
        BufferedReader br = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/SmartHome/cache_event");
            FileReader read = new FileReader(file.getAbsoluteFile());
            br = new BufferedReader(read);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] parts = line.toString().split("\\$");
                title.add(parts[0]);
                subtitle.add(parts[1]);
                start.add(parts[2]);
                dead.add(parts[3]);
                imp=parts[4];
                rem_date.add(parts[5]);
                rem_time.add(parts[6]);
                alarm=parts[5]+parts[6];
            }
            swipe_referesh.setRefreshing(false);
        }catch(Exception ex){}
    }

    public void removeLineFrom (String lineToRemove){
        try {
            File oldFile = new File(Environment.getExternalStorageDirectory(), "/SmartHome/cache_event");
            File tempFile = new File(Environment.getExternalStorageDirectory(), "/SmartHome/cache_event2");
            BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory()+"/SmartHome/cache_event"));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(lineToRemove)) {
                    pw.write(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            oldFile.delete();
            tempFile.renameTo(oldFile);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(event.this,home.class));
        overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
