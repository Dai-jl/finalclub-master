package com.czq.club;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClubActivity extends AppCompatActivity {
    private ChangeType changeType=new ChangeType();

    private List<BeanMyclub> myclubList_join=new ArrayList<>();
    private List<BeanMyclub> myclubList_att=new ArrayList<>();
    private List<BeanMyclub_passage> myclub_passageList=new ArrayList<>();
    private List<BeanMyclub_task> myclub_taskList=new ArrayList<>();

    private String sNo=null,sName=null,identity=null;
    private byte[] in;
    private int cid=0;
    private class MyThread implements Runnable{
        @Override
        public void run() {

            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql="select a.cName,a.logo,a.presonCount,a.introduction,a.mainActivity,a.cId from club a,joinclub b where b.sNo=? and a.cId=b.cId";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,sNo);
                java.sql.ResultSet rs=pst.executeQuery();
                while(rs.next()) {
                    BeanMyclub myclub_join = new BeanMyclub();
                    myclub_join.setMyclub_name(rs.getString(1));
                    myclub_join.setMember_count(rs.getInt(3));
                    myclub_join.setDescribtion(rs.getString(4));
                    myclub_join.setThings(rs.getString(5));
                    myclub_join.setCid(rs.getInt(6));

                    byte[] logo1 = changeType.blobToBytes(rs.getBlob(2));
                    Bitmap bmpout1 = BitmapFactory.decodeByteArray(logo1, 0, logo1.length);
                    Drawable drawable1 = new BitmapDrawable(bmpout1);
                    myclub_join.setMyclub_logo(drawable1);

                    myclubList_join.add(myclub_join);

                    //根据加入社团列表加载社团任务
                    sql = "select a.cName,a.logo,b.tContent,b.tTime from club a,task b where a.cId=b.cId and a.cId=?";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, myclub_join.getCid());
                    java.sql.ResultSet rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        BeanMyclub_task task = new BeanMyclub_task();
                        task.setMyclub_name(rs1.getString(1));
                        task.setTask_content(rs1.getString(3));
                        task.setTime(rs1.getString(4));

                        byte[] logo3 = changeType.blobToBytes(rs1.getBlob(2));
                        Bitmap bmpout3 = BitmapFactory.decodeByteArray(logo3, 0, logo3.length);
                        Drawable drawable3 = new BitmapDrawable(bmpout3);
                        task.setMyclub_logo(drawable3);

                        myclub_taskList.add(task);
                    }
                }

                sql="select a.cName,a.logo,a.presonCount,a.introduction,a.mainActivity,a.cId from club a,book b where b.sNo=? and a.cId=b.cId";
                pst=conn.prepareStatement(sql);
                pst.setString(1,sNo);
                rs=pst.executeQuery();
                while(rs.next()) {
                    BeanMyclub myclub_book = new BeanMyclub();
                    myclub_book.setMyclub_name(rs.getString(1));
                    myclub_book.setMember_count(rs.getInt(3));
                    myclub_book.setDescribtion(rs.getString(4));
                    myclub_book.setThings(rs.getString(5));
                    myclub_book.setCid(rs.getInt(6));

                    myclub_book.setsNo(sNo);
                    myclub_book.setIn(in);
                    myclub_book.setsName(sName);
                    myclub_book.setIsload("yes");

                    byte[] logo2 = changeType.blobToBytes(rs.getBlob(2));
                    Bitmap bmpout2 = BitmapFactory.decodeByteArray(logo2, 0, logo2.length);
                    Drawable drawable2 = new BitmapDrawable(bmpout2);
                    myclub_book.setMyclub_logo(drawable2);

                    myclubList_att.add(myclub_book);

                    //根据关注社团列表加载社团推送
                    sql = "select a.cName,a.logo,b.pImage,b.pContent,b.pName from club a,passage b where a.cId=b.cId and a.cId=?";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1,myclub_book.getCid());
                    java.sql.ResultSet rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        BeanMyclub_passage passage = new BeanMyclub_passage();
                        passage.setMyclub_name(rs1.getString(1));
                        passage.setPassage_sumary(rs1.getString(5));
                        passage.setPassage_url(rs1.getString(4));

                        byte[] logo3 = changeType.blobToBytes(rs1.getBlob(2));
                        Bitmap bmpout3 = BitmapFactory.decodeByteArray(logo3, 0, logo3.length);
                        Drawable drawable3 = new BitmapDrawable(bmpout3);
                        passage.setMyclub_logo(drawable3);

                        byte[] in1 = changeType.blobToBytes(rs1.getBlob(3));
                        Bitmap bmpout4 = BitmapFactory.decodeByteArray(in1, 0, in1.length);
                        Drawable drawable4 = new BitmapDrawable(bmpout4);
                        passage.setPassage_photo(drawable4);

                        myclub_passageList.add(passage);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }


    //没有加入社团的layout
    private RelativeLayout no_joinview,no_attview;
    //加入社团的layout
    private RecyclerView recyclerView,recyclerView2,recyclerView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club);

        recyclerView=(RecyclerView) findViewById(R.id.club_logo_view);
        recyclerView2=(RecyclerView) findViewById(R.id.club_passage_view);
        recyclerView1=(RecyclerView) findViewById(R.id.club_task_view);
        no_joinview=(RelativeLayout)findViewById(R.id.no_join);
        no_attview=(RelativeLayout)findViewById(R.id.no_att);

        //头部两个按钮和按钮下面的线
        final Button btnj = (Button) findViewById(R.id.join);
        final Button btna = (Button) findViewById(R.id.attention);
        final TextView linej = (TextView) findViewById(R.id.line_join);
        final TextView linea = (TextView) findViewById(R.id.line_att);


        //加载加入社团、关注社团的列表
        sNo=new String();

        if((String)getIntent().getSerializableExtra("number")!=null){
            sNo=(String)getIntent().getSerializableExtra("number");
            sName=(String)getIntent().getSerializableExtra("name");
            in=getIntent().getByteArrayExtra("header");
            identity=(String)getIntent().getSerializableExtra("identity");
            if(getIntent().getSerializableExtra("cid")!=null)
                cid=(int)getIntent().getSerializableExtra("cid");

            MyThread thread=new MyThread();
            new Thread(thread).start();

            if(myclubList_join!=null){
                no_attview.setVisibility(View.GONE);
                no_joinview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.VISIBLE);

                //加载横向社团信息
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ClubActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                MyclubAdapter adapter=new MyclubAdapter(myclubList_join);
                recyclerView.setAdapter(adapter);

                //加载竖向社团任务
                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(ClubActivity.this);
                recyclerView1.setLayoutManager(linearLayoutManager1);
                Myclub_taskAdapter adapter1=new Myclub_taskAdapter(myclub_taskList);
                recyclerView1.setAdapter(adapter1);
            }

            //点击“我关注的”出现社团推送
            btna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    btna.setTextColor(0xff0AAD32);
                    btnj.setTextColor(0xff000000);
                    linea.setVisibility(View.VISIBLE);

                    linea.setBackgroundColor(0xff0AAD32);
                    linej.setVisibility(View.GONE);

                    if(myclubList_att.size()>0){
                        no_attview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView2.setVisibility(View.VISIBLE);
                        recyclerView1.setVisibility(View.GONE);
                        no_joinview.setVisibility(View.GONE);

                        //加载社团头像+信息
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ClubActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        MyclubAdapter1 adapter=new MyclubAdapter1(myclubList_att);
                        recyclerView.setAdapter(adapter);

                        //加载社团推送
                        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(ClubActivity.this);
                        recyclerView2.setLayoutManager(linearLayoutManager1);
                        Myclub_passageAdapter adapter1=new Myclub_passageAdapter(myclub_passageList);
                        recyclerView2.setAdapter(adapter1);

                        view.setFocusable(true);
                        view.requestFocus();
                        view.requestFocusFromTouch();
                    }
                    else{
                        no_attview.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView1.setVisibility(View.GONE);
                        no_joinview.setVisibility(View.GONE);
                    }
                }
            });

            //点击“我加入的”出现社团布置的任务
            btnj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnj.setTextColor(0xff0AAD32);
                    btna.setTextColor(0xff000000);

                    linej.setVisibility(View.VISIBLE);
                    linea.setBackgroundColor(0xff0AAD32);
                    linea.setVisibility(View.GONE);

                    if(myclubList_join!=null){
                        no_attview.setVisibility(View.GONE);
                        no_joinview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView1.setVisibility(View.VISIBLE);
                        recyclerView2.setVisibility(View.GONE);

                        //加载社团的标志和名字
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ClubActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        MyclubAdapter adapter=new MyclubAdapter(myclubList_join);
                        recyclerView.setAdapter(adapter);

                        //加载竖向社团任务
                        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(ClubActivity.this);
                        recyclerView1.setLayoutManager(linearLayoutManager1);
                        Myclub_taskAdapter adapter1=new Myclub_taskAdapter(myclub_taskList);
                        recyclerView1.setAdapter(adapter1);


                        view.setFocusable(true);
                        view.requestFocus();
                        view.requestFocusFromTouch();
                    }
                    else{
                        no_attview.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        recyclerView1.setVisibility(View.GONE);
                        no_joinview.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
        else{
            final Button attention=(Button) findViewById(R.id.attention);
            attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    no_joinview.setVisibility(View.GONE);
                    no_attview.setVisibility(View.VISIBLE);
                }
            });
            Button join=(Button) findViewById(R.id.join);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    no_joinview.setVisibility(View.VISIBLE);
                    no_attview.setVisibility(View.GONE);
                }
            });

        }




        //底部的菜单栏的监听
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(1).getItemId());

    }

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
                    Intent intent1=new Intent(ClubActivity.this,MainActivity.class);
                    intent1.putExtra("number",sNo);
                    intent1.putExtra("name",sName);
                    intent1.putExtra("header",in);
                    intent1.putExtra("identity",identity);
                    intent1.putExtra("cid",cid);
                    if(in==null)
                        intent1.putExtra("isload","no");
                    else
                        intent1.putExtra("isload","yes");
                    startActivity(intent1);
                    return true;
                case R.id.navigation_notifications:
                    // viewpager_launch.setCurrentItem(2);
                    Intent intent=new Intent(ClubActivity.this,MyActivity.class);
                    intent.putExtra("number",sNo);
                    intent.putExtra("name",sName);
                    intent.putExtra("header",in);
                    intent.putExtra("identity",identity);
                    intent.putExtra("cid",cid);
                    if(in==null)
                        intent.putExtra("isload","no");
                    else
                        intent.putExtra("isload","yes");

                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}
