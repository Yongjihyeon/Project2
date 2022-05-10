package com.android.project2;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeekViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WeekViewFragment newInstance(String param1, String param2) {
        WeekViewFragment fragment = new WeekViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
        // fragment_week_view.xml내의 weekpage 가져옴
        ViewPager2 Page = rootView.findViewById(R.id.weekpage);
        Page.post(new Runnable() {
            @Override
            public void run() {
                //객체의 현재 페이지를 설정=30
                // 전체 페이지는 60 시작 페이지를 30으로 = 양쪽으로 스와이프 하기위해
                Page.setCurrentItem(30, false);
            }
        });
        // WeekViewCalendarAdapter 어댑터 객체를 생성하여 어댑터로 설정
        FragmentStateAdapter adapter = new WeekViewAdapter(this);
        Page.setAdapter(adapter);

        return rootView;
    }
}
