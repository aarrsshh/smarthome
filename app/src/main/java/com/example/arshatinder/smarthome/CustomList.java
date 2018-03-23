package com.example.arshatinder.smarthome;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arsh(Atinder) on 26-01-2017.
 */
public class CustomList extends BaseAdapter {
    private Context mContext;
    private ArrayList title=new ArrayList<String>();
    private ArrayList subtitle=new ArrayList<String>();
    private ArrayList start=new ArrayList<String>();
    private ArrayList deadline=new ArrayList<String>();
    private String alarm;

    public CustomList(Context c, ArrayList title, ArrayList subtitle, ArrayList start, ArrayList deadline, String alarm) {
        mContext = c;
        this.title = title;
        this.start = start;
        this.deadline = deadline;
        this.subtitle = subtitle;
        this.alarm = alarm;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int n=start.size();
        return n;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            list = new View(mContext);
            list = inflater.inflate(R.layout.list_view, null);
            TextView textView1 = (TextView) list.findViewById(R.id.list_text);
            TextView textView2 = (TextView) list.findViewById(R.id.list_text2);
            TextView textView3 = (TextView) list.findViewById(R.id.start_date);
            TextView textView4 = (TextView) list.findViewById(R.id.deadline);
            ImageView im = (ImageView) list.findViewById(R.id.ala);
            textView1.setText(title.get(position).toString());
            textView2.setText(subtitle.get(position).toString());
            textView3.setText(start.get(position).toString());
            textView4.setText(deadline.get(position).toString());
            if(alarm.equals("NoneNone"))
                im.setBackground(mContext.getDrawable(R.mipmap.alarm_off));
            else
                im.setBackground(mContext.getDrawable(R.mipmap.alarm_on));
        } else {
            list = (View) convertView;
        }
        return list;
    }
}
