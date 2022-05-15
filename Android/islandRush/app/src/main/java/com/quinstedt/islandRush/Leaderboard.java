package com.quinstedt.islandRush;
import com.quinstedt.islandRush.Leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {
    /**
     * FragmentPagerAdapter is DEPRECATED
     * Look at : https://developer.android.com/reference/androidx/fragment/app/FragmentPagerAdapter
     * Video : https://www.youtube.com/watch?v=ufZhpgoHLv8
     *
     * if we have time can change it.
     */

    public class VPAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        private final ArrayList<String> fragmentTitle = new ArrayList<>();

        public VPAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentArrayList.add(fragment);
            fragmentTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageButton escapeHash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        escapeHash =  findViewById(R.id.leaderboard_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Leaderboard1(),"Leaderboard 1");
        vpAdapter.addFragment(new Leaderboard2(),"Leaderboard 2");
        viewPager.setAdapter(vpAdapter);
    }

    private void goBack() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }

    public void setPlayerName(int textID, String input){
        TextView textView = findViewById(textID);
        textView.setText(input);
    }



}

