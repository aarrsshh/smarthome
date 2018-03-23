package com.example.arshatinder.smarthome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Arsh(Atinder) on 04-02-2017.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new myhome();
            case 1:
                return new camera();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
