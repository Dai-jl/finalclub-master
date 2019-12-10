package com.czq.club.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.czq.club.Club;
import com.czq.club.DBUtils;
import com.czq.club.R;

import java.sql.Connection;
import java.sql.SQLException;

public class AdminClubDescription extends AppCompatActivity {
    private String name;
    private de.hdodenhof.circleimageview.CircleImageView logo;
    private TextView clubName;
    private TextView member;
    private TextView description;
    private TextView things;
    private Button cancel;
    private ImageView back;
    private Club club = new Club();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_club_describtion);
        name = getIntent().getStringExtra("name");
        //从数据库中获取club数据
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Connection conn = DBUtils.getConnection();
                    String sql = "select * from club where cName = ?";
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1,name);
                    java.sql.ResultSet rs = pst.executeQuery();
                    while(rs.next()){
                        club.setName(rs.getString(2));
                        club.setIntroduction(rs.getString(6));
                        club.setCount(rs.getInt(3));
                        club.setThings(rs.getString(8));
                        club.setImageId(rs.getBytes(5));
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        });
        try{
            thread.start();
            thread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        //将club数据显示出来
        logo = findViewById(R.id.club_logo);
        logo.setImageBitmap(club.ToBitMap());
        clubName = findViewById(R.id.club_name);
        clubName.setText(club.getName());
        member = findViewById(R.id.member_count);
        member.setText(club.getCount()+"");
        description = findViewById(R.id.club_describtion);
        description.setText(club.getIntroduction());
        things = findViewById(R.id.club_things);
        things.setText(club.getThings());

        //设置按键的点击
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminClubDescription.this,AdminClub.class);
                startActivity(intent);
            }
        });
        cancel = findViewById(R.id.cancel_club);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(v);
            }
        });

    }

    //取消社团
    public  void cancel(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("取消社团");//设置对话框的标题
        builder.setMessage("确定取消社团？");//设置对话框的内容
        builder.setPositiveButton("再想想", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Connection conn = DBUtils.getConnection();
                            String sql = "delet from club where cName = ?";
                            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setString(1,name);
                            pst.execute();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try{
                    thread.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(AdminClubDescription.this,AdminClub.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        AlertDialog b=builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
    }


}
