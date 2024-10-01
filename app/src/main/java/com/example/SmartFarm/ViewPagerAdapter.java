package com.example.SmartFarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeTab();

            case 1:
                return new SuperviseTab();
            case 2:
                return new AutoTab();
            case 3:
                return new PersonTab();
            default:
                return new HomeTab();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
