package com.android.project2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vp = inflater.inflate(R.layout.fragment_month, container, false);
        //viewpager2 객체를 받아옴
        ViewPager2 vpPager = vp.findViewById(R.id.vpPager);
        vpPager.post(new Runnable() {
            @Override
            public void run() {
                //객체의 현재 페이지를 설정=30
                // 전체 페이지는 60 시작 페이지를 30으로 = 양쪽으로 스와이프 하기위해
                vpPager.setCurrentItem(30, false);
            }
        });
        //어댑터 연결
        FragmentStateAdapter adapter = new MonthViewAdapter(this);
        vpPager.setAdapter(adapter);
        return vp;
    }
}