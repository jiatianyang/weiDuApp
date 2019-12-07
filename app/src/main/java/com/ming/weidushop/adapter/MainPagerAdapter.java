package com.ming.weidushop.adapter;




import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ming.weidushop.fragment.AccountFragment;
import com.ming.weidushop.fragment.CircleFragment;
import com.ming.weidushop.fragment.HomeFragment;
import com.ming.weidushop.fragment.OrderFragment;
import com.ming.weidushop.fragment.ShopCarFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * author:AbnerMing
 * date:2019/3/29
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new CircleFragment());
        mFragmentList.add(new ShopCarFragment());
        mFragmentList.add(new OrderFragment());
        mFragmentList.add(new AccountFragment());
    }


    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
