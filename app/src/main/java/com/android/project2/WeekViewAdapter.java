package com.android.project2;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;
//자바 calendar클래스로 연도, 날짜 월을 계산
//그 계산한 값을 배열로 만들어 weekfragment로 인자를 전달해 weekfragment가 주간달력의 형태, 디자인을 구성
public class WeekViewAdapter extends FragmentStateAdapter {
    private static final int NUM_ITEMS = 60;
    // 앱을 실행시킨 순간의 연도, 월, 날짜
    private int year;
    private int month;
    private int day;

    public WeekViewAdapter(@NonNull Fragment fragment) {
        super(fragment);
        //java calendar를 통해 년월일 초기화 시킴
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
    }

    @NonNull
    @Override
    public WeekFragment createFragment(int position) {
        // 시작 포지션은 전체 60페이지중 30뻔째 페이지(양옆으로 스와이프 되기때문)
        position -= NUM_ITEMS/2;
        // 시작 일자인 newday 변수를 오늘 일자에서 position*7을 더한 값으로 설정
        //다음 페이지로 넘길때 7일씩 더해진 날짜가 보이도록..
        int newday = day + (position * 7);

        Calendar calendar = Calendar.getInstance();
        int lastday = calendar.getActualMaximum(Calendar.DATE);//이번달의 마지막날
        // newyear, newmonth 현재 연도, 월로 초기화
        int newyear = year;
        int newmonth = month;

        if ( newday > lastday ) {
            // newday가 31또는 30일을 넘을경우=한달을 넘는다면
            while ( newday > lastday ) {
                //newday가 마지막날보다 작아질때까지 다음달의 마지막날을 받아와 newday에서 뺀다
                calendar.set(Calendar.YEAR, newyear);
                calendar.set(Calendar.MONTH, newmonth);
                lastday = calendar.getActualMaximum(Calendar.DATE);
                newday -= lastday;
                if ( newmonth == 11 ) {
                    newyear++;
                    newmonth = 0; }
                else
                    newmonth++;
            }
        }
        else if ( newday < 1 ) {
            //스와이프 도중 newday가 1일보다 작을경우
            while ( newday < 1 ) {
                //newday가 1보다 커질때까지 이전달의 마지막날을 더해준다
                if ( newmonth < 0 ) {
                    newyear--;
                    newmonth = 11;
                }
                else
                    newmonth--;
                calendar.set(Calendar.YEAR, newyear);
                calendar.set(Calendar.MONTH, newmonth);
                newday += lastday;
            }
        }
        int[][] weekcalendar = getweek(newyear, newmonth, newday);
        //연도, 월, 날짜를 인자로 전달
        //weekfragment에게 계산한 날짜값 전달
        return WeekFragment.newInstance(weekcalendar[0], weekcalendar[1][0], weekcalendar[1][1]);
    }

    // --------getweeek 연도,월,일을 입력받아 주간 날짜 배열 설정----------
    private int[][] getweek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        int lastday = calendar.getActualMaximum(Calendar.DATE);
        //요일 설정
        day -= calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // date 1보다 작을 때에 대한 설정
        if ( day < 1 ) {
            if ( month == 0 ) {//1월일경우
                year--;
                month = 11;
                calendar.set(Calendar.YEAR, year);
            }
            else  // 1월이아닌 다른 월
                month--;
            // 새롭게 얻은 월로 calendar set
            calendar.set(Calendar.MONTH, month);
            lastday = calendar.getActualMaximum(Calendar.DATE);
            day += lastday;
        }
        // 날짜 배열 초기화
        int[] dayseven = new int[7];
        for (int i=0; i<dayseven.length; i++) {
            int d = day + i;
            if ( d > lastday ) {//30,31을 넘을 경우 지난 달의 마지막날에서 빼줌
                d -= lastday;
            }
            dayseven[i] = d;
        }

        // 배열,연도,월을 전달하기 위해 반환에 2차원 배열 형식 사용
        return new int[][] {
                dayseven,
                new int[] {
                        year,
                        month
                }
        };
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
