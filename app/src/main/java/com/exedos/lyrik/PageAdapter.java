package com.exedos.lyrik;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Locale;

public class PageAdapter extends FragmentPagerAdapter
{
    public String actName;

    int tabcount;




    public PageAdapter(@NonNull  FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;



    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        actName = Song_View_page.actvtyName.toString()+1;
        String act1 = "MainActivity";
        Log.d("tag", "Pag: "+actName);

    switch (position) {
        case 0:
            return new ftab1();
        case 1:
            return new ftab2();
        // case 2 : return new ftab3();
//        default:

    }


return null;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
