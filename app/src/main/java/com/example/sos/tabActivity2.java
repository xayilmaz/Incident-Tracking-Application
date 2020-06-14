package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.sos.Adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class tabActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);

        final TabLayout tabLayout = findViewById(R.id.tabBar);

        final ViewPager viewPager = findViewById(R.id.ViewPager2);



        final PagerAdapter pagerAdapter = new
                PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);//For Sliding

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
