package com.android.project2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthViewFragment extends Fragment {
    private static final String ARG_PARAM1 = "year";
    private static final String ARG_PARAM2 = "month";
    static GridAdapter adapter;
    private Calendar mCalendar;
    int firstday; // 첫날의 요일
    int lastday; //달의 마지막 날짜
    int year; //현재 년도
    int month; //현재 월
    int h;  //gridview를 구성하는 textview 높이 설정
    int w;  //gridview를 구성하는 textview 폭 설정
    DisplayMetrics dm;  // 화면의 크기를 받아오는 변수 선언
    private TextView block; //이전에 선택된 textview를 저장하는 변수
    ArrayList<String> daylist;//날짜 저장 리스트

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //프래그먼트가 생성됐을 때 전달받은 변수 초기화
            year = getArguments().getInt(ARG_PARAM1);
            month = getArguments().getInt(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_month_view, container, false);
        //액션바 이름 설정
        getActivity().setTitle(year + "년 " + (month + 1) + "월");

        //화면에 대한 크기 정보를 받아옴
        dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        h = dm.heightPixels;
        w = dm.widthPixels;

        GridView gridview = (GridView) v.findViewById(R.id.gridview);

        long now = System.currentTimeMillis();//오늘 날짜 설정
        final Date date = new Date(now); //date 객체 생성



        Calendar calendar = Calendar.getInstance();

        mCalendar = Calendar.getInstance();
        mCalendar.set(year,month,1);

        daylist = new ArrayList<String>();
        setCalendar(year, month); //daylist를 초기화 히면서 year, month 정보를 넣는다.

        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++) {//매달 1일과 요일을 일치시키기위한 공백 추가
            daylist.add("");
        }

        //----------------------그리드뷰 클릭이벤트 설정----------------------
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //position은 내가 누른 위치.
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //현재 클릭한 textview의 배경색을 CYAN으로 변경
                TextView tv = (TextView) view.findViewById(R.id.item_gridview);
                tv.setBackgroundColor(Color.CYAN);
                //이전에 클릭된 textview는 배경색을 WHITE로 변경
                block.setBackgroundColor(Color.WHITE);
                block = tv;
                //현재 클릭된 textview의 년,월 정보 출력
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
    class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            //입력받음 daylist로 객체 초기화
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {//리스트 크기 반환
            return list.size();
        }
        @Override
        public String getItem(int position) {//리스트 해탕 위치에 날짜 반환
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {//리스트의 위치 반환
            return position;
        }

        @Override
        //각각 포지션에 convertview 설정
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {// convertView는 그리드 하나
                convertView = inflater.inflate(R.layout.calendar_gridview, parent, false);
                // calendar_gridview.xml파일을 convertview 객체로 반환
                holder = new ViewHolder();// View를 담고 있는 ViewHolder 객체
                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.item_gridview);
                // holder의 TextView객체에 calendar_gridview.xml의 텍스트뷰를 연결한다
                convertView.setTag(holder);
                // convertView하나마다의 태그에 둘을 연결하기위해 태그를 holder로 설정.
            }
            else {
                holder = (ViewHolder) convertView.getTag();
                //convertview의 태그를 가져와 holder에 초기화한다. 따라서 holder는 convertview의 태그가 된다.
            }
            holder.tvItemGridView.setText("" + getItem(position)); //그리드뷰 각 포지션에 날짜 입력
            //--------------글자 색 설정---------------
            int color = Color.BLACK;
            switch (position % 7) {
                case 0: // 일요일은 빨강색으로 설정
                    color = Color.RED;
                    break;
                case 6: // 토요일은 파랑으로 설정
                    color = Color.BLUE;
                    break;
            }
            //그리드뷰에 적힌 날짜 색 설정
            holder.tvItemGridView.setTextColor(color);

            //각 textview의 높이와 폭 설정
            holder.tvItemGridView.setHeight((h/7));
            holder.tvItemGridView.setWidth(w/7);

            //block 초기화
            TextView tv = (TextView) convertView.findViewById(R.id.item_gridview);
            if(position==0)
                block = tv;

            return convertView;
        }
    }
    public class ViewHolder {// ViewHolder 클래스(태그에 쓰일 클래스)
        TextView tvItemGridView;
    }
    public static MonthViewFragment newInstance(int year, int month) {
        MonthViewFragment fragment = new MonthViewFragment();

        Bundle args = new Bundle();  // 인자 값을 저장할  번들 객체 생성
        args.putInt(ARG_PARAM1, year); // 인자 값을 (키,값) 페어로 번들 객체에 설정
        args.putInt(ARG_PARAM2, month); // 인자 값을 (키,값) 페어로 번들 객체에 설정
        fragment.setArguments(args);  // 인자값을 저장한 번들 객체를 프래그먼트로 전달

        return fragment;
    }
}