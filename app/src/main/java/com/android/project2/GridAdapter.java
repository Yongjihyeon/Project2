package com.android.project2;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//그리드뷰 어댑터 생성(어댑터 클래스 생성)
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
        holder.tvItemGridView.setTextColor(color);
        //그리드뷰에 적힌 날짜 색 설정
        return convertView;
    }
}



