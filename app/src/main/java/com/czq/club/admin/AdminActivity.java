package com.czq.club.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.czq.club.Activity;
import com.czq.club.Club;
import com.czq.club.DBUtils;
import com.czq.club.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity   extends AppCompatActivity {
    private List<Activity> aList = new ArrayList<>();
    private com.czq.club.NoScrollListview listview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminclub);
        this.aInit();
        Log.i("size",aList.size()+"");
        listview = findViewById(R.id.clubList);
        listview.setAdapter(new ActivityListAdapter(aList,this.getBaseContext()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("selected",position+"");
                Intent intent = new Intent(AdminActivity.this,AdminActivityDescription.class);
                intent.putExtra("activity",aList.get(position));
                startActivity(intent);
            }
        });

        //底部的菜单栏的监听
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.getMenu().getItem(0).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    void aInit(){
        try{
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Connection conn = DBUtils.getConnection();
                        String sql = "select * from activity";
                        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                        java.sql.ResultSet rs = pst.executeQuery();
                        int i = 0;
                        while(rs.next()){
                            Activity a = new Activity();
                            a.setaId(rs.getInt(1));
                            sql = "select cName,logo from club where cId = ?";
                            pst = conn.prepareStatement(sql);
                            pst.setInt(1,rs.getInt(2));
                            java.sql.ResultSet clubrs = pst.executeQuery();
                            Club club = null;
                            while(clubrs.next()){
                                club = new Club();
                                club.setName(clubrs.getString(1));
                                club.setImageId(clubrs.getBytes(2));
                            }
                            a.setClub(club);
                            a.setaName(rs.getString(3));
                            a.setaTime(rs.getString(4).toString());
                            a.setNumber(rs.getInt(5));
                            a.setBudget(Float.toString(rs.getFloat(6)));
                            a.setDetails(rs.getString(7));
                            a.setIn_activity(rs.getString(8));
                            a.setStatus(rs.getString(11));
                            aList.add(a);
                            Log.i("no",""+i++);
                        }
                    }catch(SQLException e){
                        e.printStackTrace();
                    }finally{

                    }
                }
            });
            thread.start();
            thread.join();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //底部菜单栏的点击事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //   viewpager_launch.setCurrentItem(0);
                    Intent intent1=new Intent(AdminActivity.this, AdminClub.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_dashboard:
                    //  viewpager_launch.setCurrentItem(1);
                    return true;

            }
            return false;
        }
    };
}
