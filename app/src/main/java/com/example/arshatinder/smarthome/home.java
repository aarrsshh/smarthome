package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView grid;
    String[] web = {"Lobby", "Drawing", "Kitchen", "Bed Room", "Bath Room", "Garage", "Porch", "Security"
    };
    int[] imageId = {
            R.mipmap.lobby, R.mipmap.drawing, R.mipmap.kitchen, R.mipmap.bed, R.mipmap.bath, R.mipmap.garage, R.mipmap.porch, R.mipmap.secoff
    };
    int[] but = {0, 0, 0, 0, 0, 0, 0, 0};
    SharedPreferences sp;
    private TabsPagerAdapter mAdapter;
    Toolbar toolbar;
    InputStream is=null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_home);
        Window win=getWindow();
        win.setStatusBarColor(Color.parseColor("#09588D"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabhome);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#033252")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,event.class));
                overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final ViewPager viewPager=(ViewPager)findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager);

        sp = getSharedPreferences("homename", Context.MODE_PRIVATE);
        if(!(sp.getString("name", "NULL").equals("NULL"))){
            setTitle(sp.getString("name", "NULL"));
        }

        CustomGrid adapter = new CustomGrid(home.this, web, imageId, but);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final SwipyRefreshLayout swipe_referesh = (SwipyRefreshLayout)findViewById(R.id.swipe_refresh);
        final BottomSheetDialogFragment myBottomSheet = MyBottomSheetDialogFragment.newInstance("hello");
        swipe_referesh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipe_referesh.setRefreshing(false);
                myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final EditText ed=new EditText(home.this);
                ed.setEms(10);
                ed.setLetterSpacing(Float.parseFloat("0.2"));
                AlertDialog.Builder ad=new AlertDialog.Builder(home.this);
                ad.setTitle("Enter Name Of Your House");
                ad.setIcon(R.mipmap.home);
                ad.setView(ed, 40, 0, 50, 0);
                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp=getSharedPreferences("homename",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("name",ed.getText().toString());
                        setTitle(ed.getText().toString());
                        edit.commit();
                    }
                });
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });ad.show();
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(web[position].toString().equals("Lobby")){
                    data("Lobby",R.mipmap.backlobby);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Drawing")){
                    data("Drawing Room",R.mipmap.backdrawing);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Kitchen")){
                    data("Kitchen",R.mipmap.backkitchen);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Bed Room")){
                    data("Bed Room",R.mipmap.backbed);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Bath Room")){
                    data("Bath Room",R.mipmap.backbath);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Garage")){
                    data("Garage",R.mipmap.backgarage);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Porch")){
                    data("Porch",R.mipmap.backporch);
                    startActivity(new Intent(home.this, Room.class));
                    overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                    finish();
                } else if(web[position].toString().equals("Security")) {
                    if(imageId[7] == R.mipmap.secoff){
                        Toast.makeText(home.this,"Securing Home",Toast.LENGTH_SHORT).show();
                        imageId[7]=R.mipmap.secon;}
                    else{
                        Toast.makeText(home.this,"Releasing Security",Toast.LENGTH_SHORT).show();
                        imageId[7] = R.mipmap.secoff;}
                    CustomGrid adapter = new CustomGrid(home.this, web, imageId, but);
                    grid.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            AlertDialog.Builder ad=new AlertDialog.Builder(home.this);
            ad.setTitle("Quit");
            ad.setMessage("Do You really want to Quit?");
            ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            ad.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setIcon(R.mipmap.about);
            ad.setTitle("About Us");
            ad.setMessage("Arshdeep Singh (101583004)\nHarsh Gaba (101583012)\nRohit Verma (101453011)\nCOE9");
            ad.setCancelable(false);
            ad.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            ad.show();
        } else if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.send) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:sandhuatinder191@gmail.com"));
            emailIntent.setType("text/plain");
            startActivity(emailIntent);
        } else if(id == R.id.changepass){
            AlertDialog.Builder ad=new AlertDialog.Builder(home.this, R.style.MyDialogTheme);
            LayoutInflater li = getLayoutInflater();
            final View layout = li.inflate(R.layout.changepassword,
                    (ViewGroup) findViewById(R.id.changepass_layout));
            final EditText password=(EditText)layout.findViewById(R.id.ch_pass);
            final EditText c_password=(EditText)layout.findViewById(R.id.ch_cpass);
            ad.setView(layout);
            ad.setTitle("Change Password");
            ad.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sp=getSharedPreferences("user",MODE_PRIVATE);
                    String mail=sp.getString("mail",null);
                    String message;
                    int color;
                    if(c_password.getText().toString().equals(password.getText().toString()) && !mail.equals(null) && !password.getText().toString().equals("")){
                        ArrayList<NameValuePair> al=new ArrayList<>();
                        al.add(new BasicNameValuePair("username",mail));
                        al.add(new BasicNameValuePair("pass",password.getText().toString()));
                        new database("http://smarthome01.freeiz.com/update_pass.php",al).execute();
                        message="Password has been Successfully Updated :)";
                        color=Color.GREEN;
                    }else {
                        message="Password Updation Unsuccessful :(";
                        color=Color.RED;
                    }
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.homelay),message, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextSize(Float.parseFloat("16"));
                    textView.setTextColor(color);
                    snackbar.show();
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });ad.show();
        }else if(id == R.id.signout) {
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setTitle("Sign Out");
            ad.setCancelable(false);
            ad.setMessage("Do you really to want to sign out?");
            ad.setIcon(R.mipmap.sign_out);
            ad.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(home.this,login.class));
                    overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
                    finish();
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });ad.show();
        }else if(id == R.id.applock) {
            startActivity(new Intent(home.this,app_lock.class));
            overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Sends Room Name and Background image to Room activity
    public void data(String data, int id){
        sp = getSharedPreferences("Title", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString("title",data);
        edit.putInt("im",id);
        edit.commit();
    }

    //Connecting with Database
    private class database extends AsyncTask<Void, Void, Void> {
        String url;
        ArrayList al;
        public database(String url, ArrayList al){
            this.url = url;
            this.al = al;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                HttpClient hc=new DefaultHttpClient();
                HttpPost hp=new HttpPost(url);
                hp.setEntity(new UrlEncodedFormEntity(al));
                HttpResponse hr=hc.execute(hp);
                HttpEntity en=hr.getEntity();
                is=en.getContent();
            }
            catch (Exception e) {Toast.makeText(home.this,"Your Not Connected To Internet",Toast.LENGTH_LONG).show();}
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}