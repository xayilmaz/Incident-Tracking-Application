package com.example.sos.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sos.Fragment.AddIncidentFragment;
import com.example.sos.Fragment.IncidentsFragment;
import com.example.sos.Fragment.ProfileFragment;

public class PagerAdapter extends FragmentPagerAdapter  {

    private int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new AddIncidentFragment();
            case 2:
                return new IncidentsFragment();
            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    public CharSequence getPageTitle(int position) {
        //this is where you set the titles
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "Add Incident";
            case 2:
                return "Incidents";
        }
        return null;
    }
}