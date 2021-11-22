package com.lovepushapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lovepushapp.fragments.FragmentFreePlan;
import com.lovepushapp.fragments.FragmentFreePremium;
import com.lovepushapp.fragments.FragmentFreeVIp;

public class PaymentPagerAdapter extends FragmentStatePagerAdapter {
    int count = 0;

    public PaymentPagerAdapter(@NonNull FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new FragmentFreePlan();
        else if (position == 1)
            return new FragmentFreePremium();
        else if (position == 2)
            return new FragmentFreeVIp();
        return new FragmentFreePlan();
    }

    @Override
    public int getCount() {
        return count;
    }
}
