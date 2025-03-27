package com.example.modatlas.views;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.modatlas.fragments.ModpackDetailTabData;
import com.example.modatlas.fragments.ModpackDetailTabMods;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new ModpackDetailTabMods() : new ModpackDetailTabData();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}