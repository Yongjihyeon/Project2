package com.android.project2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class MonthViewAdapter extends FragmentStateAdapter {
    private static final int NUM_ITEMS = 60;
    // 앱을 실행시킨 순간의 연도, 월, 날짜
    private int year;
    private int month;

    public MonthViewAdapter(Fragment fa) {
        super(fa);
        //java calendar를 통해 년월일 초기화 시킴

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
    }

    @Override
    public Fragment createFragment(int position) {
        position -= NUM_ITEMS / 2;
        int curYear = year;
        int curMonth = month + (position);
        if (curMonth > 11) {
            curYear += 1;
            curMonth = 1;
        }
        if (curMonth < 0) {
            curYear -= 1;
            curMonth = 11;
        }
        return MonthViewFragment.newInstance(curYear, curMonth);
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}