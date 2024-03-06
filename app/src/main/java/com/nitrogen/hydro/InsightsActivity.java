package com.nitrogen.hydro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.nitrogen.hydro.fragements.MountlyFragment;

import java.util.ArrayList;
import java.util.List;

public class InsightsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.activity_insights);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        ViewHolder vh = new ViewHolder(getSupportFragmentManager());
        vh.addLayout(new MountlyFragment() , "ماهانه");
        viewPager.setAdapter(vh);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewHolder extends FragmentPagerAdapter {
        List<String> titleList;
        List<Fragment> fragmentList;

        public ViewHolder(@NonNull FragmentManager fm) {
            super(fm);
            titleList = new ArrayList<>();
            fragmentList = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        private void addLayout(Fragment fragment, String title){
            fragmentList.add(fragment);
            titleList.add(title);
        }
    }
}