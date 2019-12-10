package com.czq.club;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClubManagerCreateActivity extends Activity {
    Spinner spinner;
    Spinner spinnerin;
    private EditText aname=null;
//   private EditText cname=null;
    private EditText time=null;
    private EditText phone=null;
    private EditText budget=null;
    private EditText detail=null;
    private EditText number=null;
    private EditText leader=null;

    private int cid;

    private List<String> spinnername=new ArrayList<>();
    private List<String> spinnernamein=new ArrayList<>();
    private int index=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_manager_createactivity);

        cid=(int)getIntent().getSerializableExtra("cid");

        Log.d("aaa", String.valueOf(cid));
        number=(EditText)findViewById(R.id.edit_number);
        aname=(EditText)findViewById(R.id.edit_activityname);
   //     cname=(EditText)findViewById(R.id.edit_host);
        time=(EditText)findViewById(R.id.edit_time);
        phone=(EditText)findViewById(R.id.edit_phone);
        budget=(EditText)findViewById(R.id.edit_budget);
        detail=(EditText)findViewById(R.id.edit_content);
        leader=(EditText)findViewById(R.id.edit_leader) ;
        //下拉框
        spinner=findViewById(R.id.edit_location);
        spinnerin=findViewById(R.id.edit_in);

        Button button=(Button)findViewById(R.id.button_commit_activity);
        TextView textname=(TextView)findViewById(R.id.text_activity_name);

       //启动线程获得下拉框里的值。

        spinnername.add("图信");
        spinnername.add("风雨操场");
        spinnername.add("理四230");
        spinnernamein.add("是");
        spinnernamein.add("否");

        ArrayAdapter<String> adapter;//数组 配置器 下拉菜单赋值用
//将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnername);
//设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);//将adapter 添加到spinner中

        adapter.notifyDataSetChanged();       //通知spinner刷新数据


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnernamein);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerin.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {



               spinner.setSelection(pos,true);
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      Mythread2 thread=new Mythread2();
        new Thread(thread).start();

        if(index==1){
            aname.setText("");
     //       cname.setText("");
            time.setText("");
            number.setText("");
            spinnerin.setSelection(0);
            spinner.setSelection(0);
            leader.setText("");
            phone.setText("");
            budget.setText("");
            detail.setText("");
        }

            }
        });
    }




    private class Mythread implements Runnable{
        public void run() {
            DBUtils dbUtils = new DBUtils();

            try {
                Connection conn = dbUtils.getConnection();
                String sql="SELECT cname from classromm";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.ResultSet rs=pst.executeQuery();

                while(rs.next()) {
                    String cname = rs.getString(1);
                    spinnername.add(cname);
                }


                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class Mythread2 implements Runnable{
        public void run() {
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();



                Log.d("aaa", String.valueOf(cid));
                String sql="select * from activity where cid = ? and aname=? and atime=?";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setInt(1,cid);
                pst.setString(2,aname.getText().toString());
                pst.setString(3,time.getText().toString());
                java.sql.ResultSet rs=pst.executeQuery();
                if (rs.next()){
                    Looper.prepare();
                    Toast.makeText(ClubManagerCreateActivity.this,"该活动已在申请中",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                else {
                    sql = "INSERT into activity (cid,aname,atime,number,budget,details,in_activity,rname,phone,astatus)VALUES(?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, cid);
                    pst.setString(2, aname.getText().toString());
                    pst.setString(3, time.getText().toString());
                    pst.setString(4, number.getText().toString());
                    pst.setString(5, budget.getText().toString());
                    pst.setString(6, detail.getText().toString());
                    pst.setString(7, spinnerin.getSelectedItem().toString());
                    pst.setString(8, spinner.getSelectedItem().toString());
                    pst.setString(9, phone.getText().toString());
                    pst.setString(10, "审核中");
                    pst.execute();
                    index=1;
                    Looper.prepare();
                    Toast.makeText(ClubManagerCreateActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    Looper.loop();


                }
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
