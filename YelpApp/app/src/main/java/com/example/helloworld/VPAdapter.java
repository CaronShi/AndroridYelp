package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    private  ArrayList<Fragment>  fragmentsArr = new ArrayList<Fragment>();
    private  ArrayList<String>  frag = new ArrayList<String>();

    public VPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsArr.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsArr.size();
    }


    public void addFrag(Fragment fragment, String title){
        fragmentsArr.add(fragment);
        frag.add(title);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return frag.get(position);
    }
}
