package com.android.project2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class WeekFragment extends Fragment {
    // 다른 프래그먼트와 year, month값을 가지고 통신, 공유
    private static final String ARG_PARAM1 = "year";
    private static final String ARG_PARAM2 = "month";
    private int year;  // 입력받은 년도
    private int month; // 입력받은 월
    private int[] sevendays;//현재 주의 날짜들을 저장하는 배열
    private String[] schedules;// 일정 데이터가 들어갈 배열

    private TextView block;
    //linearlayout내부의 textview를 선택하려면 block이 선택 되었을 때 선택된다.
    //다른 block을 선택하면 이전의 block은 다시 흰색이 되기 때문에 이전의 블록을 block변수에 저장

    public WeekFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //다른 프래그먼트로 부터 받은 year, month 초기화
            year = getArguments().getInt(ARG_PARAM1);
            month = getArguments().getInt(ARG_PARAM2);

            // 주간 날짜 배열을 day1, 2,...,7을 통해 초기화한다.
            sevendays = new int[7];
            for (int i=0; i<sevendays.length; i++) {
                sevendays[i] = getArguments().getInt("day" + (i+1));
            }
            //schedule배열을 일주일에 24시간이므로 7*24=168개의 배열을만들고 공백으로 채움
            schedules = new String[168];
            Arrays.fill(schedules, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        // 스케줄 Gridㅍiew의 Adapter 어댑터 설정
        // gridView는 fragment_week_calender의 schedule_grid
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity().getApplicationContext(), schedules);
        GridView gridView = view.findViewById(R.id.schedule);
        gridView.setAdapter(scheduleAdapter);

        //0~23의 숫자가 있는 시간대 배열을 생성
        TimeAdapter timeAdapter = new TimeAdapter(getActivity().getApplicationContext());
        GridView timeGridView = view.findViewById(R.id.hour);
        timeGridView.setAdapter(timeAdapter);

        // 블록을 클릭하면 토스트메세지와 배경색변경
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.week_gridview);
                //view에서 클릭된 text를 가져온다
                textView.setBackgroundColor(Color.CYAN);
                //가져온 text부분 배경색을 cyan으로 변경하고 이전에 선택한 블록은 원상태인 흰색으로 변경한다
                block.setBackgroundColor(Color.WHITE);
                block = textView;
                Toast.makeText(getActivity(), "position=" + (position % 7), Toast.LENGTH_SHORT).show();
            }
        });

        // 날짜들이 저장될 TextView 7개
        TextView[] textViews = {
                view.findViewById(R.id.week1),
                view.findViewById(R.id.week2),
                view.findViewById(R.id.week3),
                view.findViewById(R.id.week4),
                view.findViewById(R.id.week5),
                view.findViewById(R.id.week6),
                view.findViewById(R.id.week7)
        };

        // 앱바타이틀을 현재 월로 변경함
        getActivity().setTitle(year + "년 " + (month + 1) + "월");

        // 이전에 선택했던 블록의 배경색을 흰색으로 바뀌게 하기 위한 배열
        // 이벤트 리스너로 만들 익명 클래스에서는 final 타입의 변수만 이용 가능하기에, 1개짜리 배열 생성
        final TextView[] preselect = {
                textViews[0]
        };
        // 매월 첫날짜의 배경을 cyan으로 설정해둠
        preselect[0].setBackgroundColor(Color.CYAN);

        for (int i=0; i<sevendays.length; i++) {
            final int index = i;
            // 날짜 설정
            textViews[i].setText("" + sevendays[i]);
            textViews[i].setOnClickListener(new View.OnClickListener() {//날짜 클릭
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "position=" + index, Toast.LENGTH_SHORT).show();
                    //눌린 포지션 토스트 메세지 출력
                    textViews[index].setBackgroundColor(Color.CYAN);//클릭된 텍스트뷰 배경색 변경
                    preselect[0].setBackgroundColor(Color.WHITE);//이전에 클릭된 텍스트뷰 배경색 흰색변경
                    preselect[0] = textViews[index];//preselect값을 방금 누른 텍스트뷰로 변경
                }
            });
        }
        return view;
    }

    // newInstance 메소드에서는 파라미터로 주간 달력의 날짜 7개, 년, 월 정보를 입력 받음
    public static WeekFragment newInstance(int[] daySeven, int year, int month) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        // fragment에 아까 전달받은 날짜 배열을 day1, day2, ..., day7 형태로 전달
        for (int i=0; i<daySeven.length; i++) {
            args.putInt("day" + (i+1), daySeven[i]);
        }
        // fragment에 year, month를 전달
        args.putInt("year", year);
        args.putInt("month", month);
        fragment.setArguments(args);
        return fragment;
    }

    //------------------schedule adapter------------------
    // 스케줄 GridView의 데이터를 채워넣기 위한 GridAdapter 내부클래스
    private class ScheduleAdapter extends BaseAdapter {

        // 스케줄 데이터 배열
        private String[] schedules;
        // LayoutInflater 객체 생성.
        private LayoutInflater inflater;

        public ScheduleAdapter(Context context, String[] schedules) {
            // 매개변수로 입력 받은 schedules로 schedules 배열 초기화.
            this.schedules = schedules;
            //context에서 LayoutInflater 가져옴.
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return schedules.length;
        }

        // 배열 position번째의 원소 반환
        @Override
        public String getItem(int position) {
            return schedules[position];
        }

        // 몇번째인지 반환
        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // convertView는 그리드의 한 블락(하나의 뷰)
            // week_gridview.xml파일을 View객체로 만들어서 반환.
            if ( convertView == null ) convertView = inflater.inflate(R.layout.week_gridview, null);
            // calendar_week_gridview 레이아웃 안의 week_item_gridview TextView를 가져옴
            TextView textView = (TextView) convertView.findViewById(R.id.week_gridview);
            // 그 TextView의 글자를 schedules 배열의 원소로 설정
            textView.setText(schedules[position]);
            //---처음 설정을 첫인덱스의 배경을 cyan색, 이전블록변수에 값 저장---
            if ( position == 0 ) {  // 처음의 데이터를 넣는 중일 경우 배경색을 CYAN으로 설정 및 Block에 이 블록 저장
                textView.setBackgroundColor(Color.CYAN);
                block = textView;
            }
            // 설정된 convertView 반환
            return convertView;
        }
    }

    //----------------time adapter----------------
    // 시간대 GridView의 데이터를 채워넣기 위한 GridAdapter 내부클래스
    private class TimeAdapter extends BaseAdapter {

        // 시간대 데이터 배열
        private String[] times;
        // LayoutInflater 객체 생성.
        private LayoutInflater inflater;

        public TimeAdapter(Context context) {
            //------0~23숫자 넣기------------
            this.times = new String[24];
            for (int i=0; i<times.length; i++) times[i] = "" + i;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return times.length;
        }

        @Override
        public String getItem(int position) {
            return times[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // convertView는 그리드의 한 블락(하나의 뷰)
            // schedule_gridview.xml파일을 View객체로 만들어서 반환.
            if ( convertView == null ) convertView = inflater.inflate(R.layout.schedule_gridview, null);
            // schedule_gridview 레이아웃 안의 timeschedule TextView를 가져옴
            TextView textView = (TextView) convertView.findViewById(R.id.timeschedule);
            // TextView를 해당 시간대로 설정
            textView.setText(times[position]);
            return convertView;
        }
    }

}

