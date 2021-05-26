package com.example.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.chattingapp.Fragments.ChatFragment;
import com.example.chattingapp.Fragments.ProfileFragment;
import com.example.chattingapp.Fragments.UsersFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_mainpage);

        //tab layout & View Pager
        TabLayout tabLayout =findViewById(R.id.tablayout);
        ViewPager viewPager =findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());
       viewPagerAdapter.addFragment(new ChatFragment(),"Chats");
       viewPagerAdapter.addFragment(new UsersFragment(),"Users");
       viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");

       viewPager.setAdapter(viewPagerAdapter);
       tabLayout.setupWithViewPager(viewPager);

    }

    //logout functionality

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
                return true;
        }
        return false;
    }

    //add ViewpagerAdapter class for fragments

class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles =new ArrayList<>();
        }

        @NonNull
        @Override
    public Fragment getItem(int position){
            return fragments.get(position);
        }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment (Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            return titles.get(position);
    }
}


}
