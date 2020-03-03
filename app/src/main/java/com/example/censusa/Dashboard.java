package com.example.censusa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class Dashboard extends AppCompatActivity{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPageAdapter(getSupportFragmentManager());

//        Adding Fragment to adapter.
        // TODO: 02/03/2020 will add individual fragment here

        adapter.AddFragment(new IndividualFragment(), "");
        adapter.AddFragment(new CompanyFragment(), "");
        adapter.AddFragment(new SignOutFragment(), "");

        // TODO: 02/03/2020  will add sign out fragment here

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_business);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_exit);
    }

}
