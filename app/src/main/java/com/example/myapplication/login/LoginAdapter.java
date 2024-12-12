package com.example.myapplication.login;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.login.FragmentLoginTab;
import com.example.myapplication.login.FragmentSignupTab;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    @Override
    public int getCount() {
        return totalTabs;
    }

    public LoginAdapter(FragmentManager fragmentManager, Context context, int totalTabs) {

        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;

    }

    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentLoginTab fragmentLoginTab = new FragmentLoginTab();
                return fragmentLoginTab;
            case 1:
                FragmentSignupTab fragmentSignupTab = new FragmentSignupTab();
                return fragmentSignupTab;
            default:
                return null;

        }

    }

}
