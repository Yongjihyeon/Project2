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

    // fragment를 전달받기 때문에
    public MonthViewAdapter(Fragment fa) {
        super(fa);
        //java calendar를 통해 년월일 초기화 시킴

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
    }

    @Override
    public Fragment createFragment(int position) {
        //초기값 position 값을 0으로 설정
        position -= NUM_ITEMS / 2;
        int curYear = year;
        int curMonth = month + (position);
        //month를 12월에서 오른쪽으로 스와이프 한 경우
        if (curMonth > 11) {
            curYear += 1;
            curMonth = 0;
        }
        //month를 1월에서 왼쪽으로 스와이프 한 경우
        if (curMonth < 0) {
            curYear -= 1;
            curMonth = 11;
        }
        //년과 월에 대한 정보를 newInstance객체를 통해 전달
        return MonthViewFragment.newInstance(curYear, curMonth);
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}