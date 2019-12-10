package com.czq.club;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;


import java.sql.Connection;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInformation extends Activity {
    private CircleImageView head;
    private TextView num,name,collegename;
    private EditText phone;
    private Button btn;

    private String phonenum,sNo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information);

        head=(CircleImageView)findViewById(R.id.head);
        num=(TextView)findViewById(R.id.num);
        collegename=(TextView)findViewById(R.id.collegename);
        name=(TextView)findViewById(R.id.name);

        //Edittext设置一开始的时候光标隐藏，当点击的时候光标出现
        phone=(EditText)findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setCursorVisible(true);
            }
        });

        final Toolbar toolbar=(Toolbar)findViewById(R.id.personal_informaton_toorbar);
        sNo=(String)getIntent().getSerializableExtra("sNo");
        num.setText(sNo);
        name.setText((String)getIntent().getSerializableExtra("sName"));
        collegename.setText((String)getIntent().getSerializableExtra("collegeName"));
        phonenum=(String)getIntent().getSerializableExtra("sPhone");
        phone.setText(phonenum);

        byte[] in = getIntent().getByteArrayExtra("header");
        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        Drawable drawable = new BitmapDrawable(bmpout);
        head.setImageDrawable(drawable);

        btn=(Button)findViewById(R.id.push);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(phone.getText().toString()).equals(phonenum)){
                    if((phone.getText().toString()).length()!=11){
                        Toast toast1=Toast.makeText(getApplicationContext(),"电话输入有误！",Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER,0,0);
                        toast1.show();
                    }
                    else{
                        Mythread thread=new Mythread();
                        new Thread(thread).start();
                        Toast toast=Toast.makeText(getApplicationContext(),"电话修改成功！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            }
        });

        final ImageView imgback=(ImageView)findViewById(R.id.personal_informaton_imgback);
        imgback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                Intent intent=new Intent(PersonalInformation.this,MyActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    private class Mythread implements Runnable {
        @Override
        public void run() {
            ChangeType changeType = new ChangeType();
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql="update student set sPhone=? where sNo=?";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,phone.getText().toString());
                pst.setString(2,sNo);
                pst.execute();
                pst.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
