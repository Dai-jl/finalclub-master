package com.czq.club.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.czq.club.Activity;
import com.czq.club.DBUtils;
import com.czq.club.R;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AdminActivityDescription extends AppCompatActivity {

    private Activity activity = new Activity();
    private ImageView back;
    private Button pass;
    private TextView name;
    private TextView time;
    private TextView number;
    private TextView budget;
    private TextView inner;
    private TextView details;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_description);

        activity = (Activity) getIntent().getSerializableExtra("activity");


        name = findViewById(R.id.name);
        name.setText(name.getText() + activity.getaName());
        time = findViewById(R.id.time);
        time.setText(time.getText() + activity.getaTime());
        number = findViewById(R.id.number);
        number.setText(number.getText() + ""+activity.getNumber());
        budget = findViewById(R.id.budget);
        budget.setText(budget.getText() + activity.getBudget());
        inner = findViewById(R.id.inner);
        inner.setText(inner.getText()+activity.getIn_activity());
        details = findViewById(R.id.deatils);
        details.setText(details.getText()+activity.getDetails());
        //设置按键的点击
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivityDescription.this,AdminActivity.class);
                startActivity(intent);
            }
        });
        pass = findViewById(R.id.pass);
        if(activity.getStatus().equals("审核中")){
            pass.setText("审批活动");
            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel(v);
                }
            });
        }

    }

    //通过活动
    public  void cancel(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("通过审批");//设置对话框的标题
        builder.setMessage("是否通过审批？");//设置对话框的内容
        builder.setPositiveButton("不通过", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Connection conn = DBUtils.getConnection();
                            String sql = "update activity set aStatus = 不通过' where aId = ?";
                            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setInt(1,activity.getaId());
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
                pass.setText("已审批");
            }
        });
        builder.setNegativeButton("通过", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Connection conn = DBUtils.getConnection();
                            String sql = "update activity set aStatus = '通过' where aId = ?";
                            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setInt(1,activity.getaId());
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
                pass.setText("已审批");
            }
        });
        AlertDialog b=builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
    }
}
