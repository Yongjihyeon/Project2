package com.android.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 앱이 실행되면 기본적으로 MonthViewFragment를 보여줌
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MonthFragment()).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 앱바의 옵션 버튼을 누를 경우 mainmenu.xml의 레이아웃을 보여줌
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 옵션 메뉴에서 '월간' 클릭 시 MonthViewFragment를 띄워 월간 달력 보여줌
            case R.id.action_month:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MonthFragment()).commit();
                Toast.makeText(getApplicationContext(), "monthview", Toast.LENGTH_SHORT).show();
                return true;

            // 옵션 메뉴에서 '주간' 클릭 시 WeekViewFragment를 띄워 주간 달력 보여줌
            case R.id.action_week:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new WeekViewFragment()).commit();
                Toast.makeText(getApplicationContext(), "weekview", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
