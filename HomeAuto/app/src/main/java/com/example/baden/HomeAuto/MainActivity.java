package com.example.baden.HomeAuto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    private static String logtag = "TEST";//for use as the tag when logging
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MQTTService.class));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    public void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MQTTService.class));
    }

    public void onPause(){
        super.onPause();
        stopService(new Intent(this, MQTTService.class));
    }

    public void onResume(){
        super.onResume();
        stopService(new Intent(this, MQTTService.class));
        startService(new Intent(this, MQTTService.class));
    }

    public static void ChangeBackground(byte flag){
        switch(flag){
            case 0: {
                viewPager.setBackgroundResource(R.drawable.spacefadenight);
                break;
            }
            case 1: {
                viewPager.setBackgroundResource(R.drawable.spacefade);
                break;
            }
            default:
            {
                break;
            }
        }
    }

}
