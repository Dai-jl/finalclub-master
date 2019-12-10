package com.czq.club;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.SQLException;

public class ClubActivity2 extends AppCompatActivity {
    private ChangeType changeType;
    private String name,sNo;
    private int cid;
    private int iscancle=0;
    private AlertDialog alertDialog2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_describtion1);

        sNo=(String)getIntent().getSerializableExtra("number");
        cid= (int)getIntent().getSerializableExtra("myclub_id");

        //返回的点击事件
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //对对应点击社团信息赋值
        changeType = new ChangeType();

        ImageView club_logo = (ImageView) findViewById(R.id.club_logo);
        TextView member_count = (TextView) findViewById(R.id.member_count);
        TextView club_des = (TextView) findViewById(R.id.club_describtion);
        final TextView club_name = (TextView) findViewById(R.id.club_name);
        TextView club_thingd = (TextView) findViewById(R.id.club_things);

        byte[] in = getIntent().getByteArrayExtra("myclub_logo");
        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        Drawable drawable = new BitmapDrawable(bmpout);
        club_logo.setImageDrawable(drawable);
        member_count.setText(Integer.toString((int) getIntent().getSerializableExtra("myclub_num")));
        club_name.setText((String) getIntent().getSerializableExtra("myclub_name"));
        club_des.setText((String) getIntent().getSerializableExtra("myclub_des"));
        club_thingd.setText((String) getIntent().getSerializableExtra("myclub_things"));


        //取消社团订阅事件
        Button cancel_save = (Button) findViewById(R.id.cancel_save);
        cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //出现提示弹窗事件
                alertDialog2 = new AlertDialog.Builder(ClubActivity2.this)
                        .setTitle("小贴士：")
                        .setMessage("   亲，真的要取消关注了吗？取消关注后将接收不到该社团的推送了哦...")
                        .setIcon(R.drawable.tips)
                        .setPositiveButton("取消关注", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消关注事件
                                Mythread mythread=new Mythread();
                                new Thread(mythread).start();

                                Intent intent =new Intent();
                                intent.setClass(ClubActivity2.this,ClubActivity.class);
                                intent.putExtra("name",(String) getIntent().getSerializableExtra("name"));
                                intent.putExtra("number",(String) getIntent().getSerializableExtra("number"));
                                intent.putExtra("header",getIntent().getByteArrayExtra("header"));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("我再想想", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog2.hide();
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
    }

    private class Mythread implements Runnable {
        @Override
        public void run() {

            DBUtils dbUtils = new DBUtils();
            try {
                Looper.prepare();
                Connection conn = dbUtils.getConnection();
                String sql = "delete from book where cId=? and sNo=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, cid);
                pst.setString(2,sNo);
                pst.execute();
                pst.close();
                conn.close();

                Looper.loop();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
