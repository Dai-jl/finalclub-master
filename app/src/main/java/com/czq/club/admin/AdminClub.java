package com.czq.club.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.czq.club.Club;
import com.czq.club.DBUtils;
import com.czq.club.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminClub  extends AppCompatActivity {
    private String delete = null;
    private List<Club> clubList = new ArrayList<Club>();
    private com.czq.club.NoScrollListview listview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminclub);
        this.clubInit();
        delete = getIntent().getStringExtra("name");
        if(delete != null) {
            for(Club c:clubList){
                if(c.getName().equals(delete)){
                    clubList.remove(c);
                    break;
                }
            }
        }
        Log.i("size",clubList.size()+"");
        listview = findViewById(R.id.clubList);
        listview.setAdapter(new ClubListAdapter(clubList,this.getBaseContext()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("selected",position+"");
                Intent intent = new Intent(AdminClub.this,AdminClubDescription.class);
                intent.putExtra("name",clubList.get(position).getName());
                startActivity(intent);
            }
        });

        //底部的菜单栏的监听
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.getMenu().getItem(1).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    void clubInit(){
        try{
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Connection conn = DBUtils.getConnection();
                        String sql = "select cName,logo,introduction from club";
                        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                        java.sql.ResultSet rs = pst.executeQuery();
                        int i = 0;
                        while(rs.next()){
                            Club club = new Club();
                            club.setName(rs.getString(1));
                            club.setImageId(rs.getBytes(2));
                            club.setIntroduction(rs.getString(3));
                            clubList.add(club);
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
                    return true;
                case R.id.navigation_dashboard:
                    //  viewpager_launch.setCurrentItem(1);
                    Intent intent1=new Intent(AdminClub.this, AdminActivity.class);
                    startActivity(intent1);
                    return true;

            }
            return false;
        }
    };

}
