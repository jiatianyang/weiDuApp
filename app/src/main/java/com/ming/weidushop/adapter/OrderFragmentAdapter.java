package com.ming.weidushop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.abner.ming.base.BaseFragment;
import com.ming.weidushop.fragment.OrderAllFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/9
 */
public class OrderFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();

    public OrderFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList.add(new OrderAllFragment());
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
