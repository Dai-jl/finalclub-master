package com.czq.club;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyActivity extends AppCompatActivity {
    private List<my_listitem> listitems=new ArrayList<>();//未登录
    private List<my_listitem> listitems2=new ArrayList<>();//登陆
    int n=0;//判断是否登陆
    int s=1;//判断是否是社长

    private AlertDialog.Builder alertDialog;

    private Button returnbutton;
    private AppCompatTextView nameview;
    private TextView numview;
    private CircleImageView header;
    private String number,name,identity;
    int cid;
    private Drawable drawable;private Bitmap bmpout;
    private byte[] in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name=(String)getIntent().getSerializableExtra("name");
        number=(String)getIntent().getSerializableExtra("number");
        String isload=(String)getIntent().getSerializableExtra("isload");
        identity=(String)getIntent().getSerializableExtra("identity");
        if(getIntent().getSerializableExtra("cid")!=null){
            cid=(int)getIntent().getSerializableExtra("cid");
            Log.d("MyActivity",cid+"#"+identity);
        }


        if("yes".equals(isload))
            n=1;

        //未登录下的我的界面的list view
        if (n==0) {
            setContentView(R.layout.activity_my);
            initItems();
            my_listitem_adapter myListitemAdapter = new my_listitem_adapter(MyActivity.this, R.layout.my_notlogin_listitem, listitems);
            ListView notloginlistView = (ListView) findViewById(R.id.my_notlogin_listview);
            notloginlistView.setAdapter(myListitemAdapter);

            Button button=(Button)findViewById(R.id.img_head_login);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });

        }
        else {
            setContentView(R.layout.activity_my2);

            returnbutton=(Button)findViewById(R.id.idreturn);
            alertDialog = new AlertDialog.Builder(MyActivity.this);
            returnbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog
                            .setMessage("确定要退出登陆吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyActivity.this, MyActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create();
                    alertDialog.show();

                }
            });
            //登陆界面下的我的list view
            nameview=(AppCompatTextView)findViewById(R.id.my_stuname);
            numview=(TextView)findViewById(R.id.my_stunumber);
            header=(CircleImageView)findViewById(R.id.img_head_login);

            nameview.setText(name);
            numview.setText(number);
            in = getIntent().getByteArrayExtra("header");
            bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
            drawable = new BitmapDrawable(bmpout);
            header.setImageDrawable(drawable);

            initItems2();
            my_listitem_adapter myListitemAdapter2 = new my_listitem_adapter(MyActivity.this, R.layout.my_notlogin_listitem, listitems2);
            ListView alloginlistView = (ListView) findViewById(R.id.my_allogin_listview);
            alloginlistView.setAdapter(myListitemAdapter2);
            alloginlistView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    if (position==0){
                        Mythread mythread=new Mythread();
                        new Thread(mythread).start();
                    }
                    else if(position==1){
                        Intent intent=new Intent(MyActivity.this,ChangePwd.class);
                        intent.putExtra("sNo",number);
                        startActivity(intent);
                    }
                    else if (s==1&&position==2){
                        Intent intent=new Intent(MyActivity.this,ClubManage.class);
                        intent.putExtra("sNo",number);
                        intent.putExtra("identity",identity);
                        intent.putExtra("cid",cid);
                        Log.d("MyA",identity+"#"+cid);

                        startActivity(intent);
                    }

                }

            });
        }

        //底部的菜单栏的监听
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(2).getItemId());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //   viewpager_launch.setCurrentItem(0);
                    Intent intent1=new Intent(MyActivity.this,ClubActivity.class);
                    intent1.putExtra("number",number);
                    intent1.putExtra("name",name);
                    intent1.putExtra("header",in);
                    intent1.putExtra("identity",identity);
                    if(in==null)
                        intent1.putExtra("isload","no");
                    else
                        intent1.putExtra("isload","yes");
                    startActivity(intent1);
                    return true;
                case R.id.navigation_dashboard:

                    //  viewpager_launch.setCurrentItem(1);
                    Intent intent2=new Intent(MyActivity.this,MainActivity.class);
                    intent2.putExtra("number",number);
                    intent2.putExtra("name",name);
                    intent2.putExtra("header",in);
                    intent2.putExtra("identity",identity);
                    if(in==null)
                        intent2.putExtra("isload","no");
                    else
                        intent2.putExtra("isload","yes");
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    // viewpager_launch.setCurrentItem(2);
//                    Intent intent=new Intent(MyActivity.this,MyActivity.class);
//                    startActivity(intent);
                    item.setCheckable(true);
                    return true;
            }
            return false;
        }
    };


    private void initItems(){
        my_listitem item1=new my_listitem("使用帮助");
        my_listitem item2=new my_listitem("关于社团助手");
        listitems.add(item1);
        listitems.add(item2);
    }
    private void initItems2(){
        my_listitem item1=new my_listitem("个人信息");
        my_listitem item2=new my_listitem("修改密码");
        my_listitem item3=new my_listitem("使用帮助");
        my_listitem item4=new my_listitem("关于社团助手");
        listitems2.add(item1);
        listitems2.add(item2);


        if (s==1){
            my_listitem item5=new my_listitem("管理社团");
            listitems2.add(item5);
        }

        listitems2.add(item3);
        listitems2.add(item4);
    }

    private class Mythread implements Runnable {
        @Override
        public void run() {
            ChangeType changeType=new ChangeType();
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql = "select sNo,sName,collegeName,sPhone,sImage from student where sNo=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, number);
                java.sql.ResultSet rs=pst.executeQuery();
                rs.next();

                Intent intent=new Intent();
                intent.setClass(MyActivity.this,PersonalInformation.class);
                intent.putExtra("sNo",rs.getString(1));
                intent.putExtra("sName",rs.getString(2));
                intent.putExtra("collegeName",rs.getString(3));
                intent.putExtra("sPhone",rs.getString(4));
                byte[] header = changeType.blobToBytes(rs.getBlob(5));
                intent.putExtra("header", header);
                MyActivity.this.startActivity(intent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
