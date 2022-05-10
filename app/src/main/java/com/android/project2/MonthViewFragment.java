package com.android.project2;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthViewFragment extends Fragment {
    static GridAdapter adapter;
    private Calendar mCalendar;
    int firstday; // 첫날의 요일
    int lastday; //달의 마지막 날짜
    int year; //현재 년도
    int month; //현재 월
    ArrayList<String> daylist;//날짜 저장 리스트
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_month_view, container, false);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);

        long now = System.currentTimeMillis();//오늘 날짜 설정
        final Date date = new Date(now); //date 객체 생성

        //년 월 일을 따로 따로 저장하기 위해 객체 생성
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();

        //mCalendar = Calendar.getInstance();
        //만약 이전 액티비가 없다면 그냥 현재 연도, 월 정보 가져오기
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        daylist = new ArrayList<String>();
        setCalendar(year, month); //daylist를 초기화 히면서 year, month 정보를 넣는다.

        mCalendar = Calendar.getInstance();
        mCalendar.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++) {//매달 1일과 요일을 일치시키기위한 공백 추가
            daylist.add("");
        }

        //----------------------그리드뷰 토스트 텍스트 설정---------------------------
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //position은 내가 누른 위치.
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity(), (year) + "/" + (month + 1) + "/" + (position - firstday + 1), Toast.LENGTH_SHORT).show();
            }
        });

        // 어댑터를 설정
        adapter = new GridAdapter(getContext(), daylist);
        gridview.setAdapter(adapter);
        return v;
    }

    private void setCalendar(int year, int month){
        //매달 첫날의 요일, 마지막날이 30일인지 31인지를 판별
        Calendar calendar = Calendar.getInstance(); //calendar 객체
        //입력받은 각각의 연도 달을 설정
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// firstday를 만들기 위해 입력받은 년, 월에 대한 일자를 1로 설정한다.
        firstday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //첫날을 설정하는데 Calendar.DAY_OF_WEEK는 일요일부터 차례로 1, 2, 3...으로 설정이 된다
        //convertview, daylist는 모두 0부터 시작하니 Calendar.DAY_OF_WEEK과 맞추기 위해 -1을 해준다
        lastday = calendar.getActualMaximum(Calendar.DATE);
        // 마지막날은 해당 달의 최대값
        for (int i=0; i<6*7; i++) { // 7행6열의 리스트를 만든다
            if ( i < firstday || i > (lastday + firstday - 1)) daylist.add("");
                // 첫날보다 작거나, 마지막날보다 크면 공백으로해줌
            else //그렇지 않으면 첫날부터 마지막날까지 써줌
                daylist.add("" + (i - firstday + 1));
        }
    }
}