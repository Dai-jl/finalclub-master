package com.czq.club;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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


public class ChangePwd extends Activity {
    private EditText change_pwd_old,newpwd,newpwd1;
    private Button btn_change;

    private String old_pwd,new_pwd,new_pwd1;
    private String sNo;

    private ImageView showpwd1;
    private ImageView showpwd2;

    private Boolean showPassword = true;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);

        sNo=(String)getIntent().getSerializableExtra("sNo");

        change_pwd_old=(EditText)findViewById(R.id.change_pwd_old);
        newpwd=(EditText)findViewById(R.id.newpwd);
        newpwd1=(EditText)findViewById(R.id.newpwd1);
        btn_change=(Button)findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_pwd=change_pwd_old.getText().toString();
                new_pwd=newpwd.getText().toString();
                new_pwd1=newpwd1.getText().toString();

                Mythread mythread=new Mythread();
                new Thread(mythread).start();
            }
        });

        //未登录下的我的界面的list view
        final EditText oldpwd=(EditText)findViewById(R.id.change_pwd_old);
         oldpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpwd.setCursorVisible(true);
            }
        });
         //toolbar
         final Toolbar toolbar=(Toolbar)findViewById(R.id.change_pwd_toorbar);
        // setTitleCenter(toolbar);

         final ImageView imgback=(ImageView)findViewById(R.id.change_pwd_imgback);
         imgback.setOnClickListener(new View.OnClickListener(){
             public void onClick(View v){
//                 Intent intent=new Intent(ChangePwd.this,MyActivity.class);
//                 startActivity(intent);
                 finish();
             }
         });


        showpwd1=(ImageView)findViewById(R.id.showpwd);
        showpwd2=(ImageView)findViewById(R.id.showpwd2);
        showpwd1.setImageDrawable(getResources().getDrawable(R.drawable.img_unlook));
        showpwd2.setImageDrawable(getResources().getDrawable(R.drawable.img_unlook));


        showpwd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.showpwd:
                        if (showPassword) {
                            showpwd1.setImageDrawable(getResources().getDrawable(R.drawable.img_look));
                            newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            newpwd.setSelection(newpwd.getText().toString().length());
                            showPassword = !showPassword;
                        } else {// 隐藏密码
                            showpwd1.setImageDrawable(getResources().getDrawable(R.drawable.img_unlook));
                            newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            newpwd.setSelection(newpwd.getText().toString().length());
                            showPassword = !showPassword;
                        }
                        break;

                    default:
                        break;

                }
            }
            });
        showpwd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.showpwd:
                        if (showPassword) {
                            showpwd1.setImageDrawable(getResources().getDrawable(R.drawable.img_look));
                            newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            newpwd.setSelection(newpwd.getText().toString().length());
                            showPassword = !showPassword;
                        } else {// 隐藏密码
                            showpwd1.setImageDrawable(getResources().getDrawable(R.drawable.img_unlook));
                            newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            newpwd.setSelection(newpwd.getText().toString().length());
                            showPassword = !showPassword;
                        }
                        break;

                    default:
                        break;

                }
            }
        });

        }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setTitleCenter(Toolbar toolbar) {
        String title = "title";
        final CharSequence originalTitle = toolbar.getTitle();
        toolbar.setTitle(title);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.equals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
            toolbar.setTitle(originalTitle);
        }
    }



    private class Mythread implements Runnable {
        public void run() {
            DBUtils dbUtils = new DBUtils();
            try {
                Looper.prepare();
                Connection conn = dbUtils.getConnection();
                String sql="select sPwd from student where sNo=?";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,sNo);
                java.sql.ResultSet rs=pst.executeQuery();
                rs.next();
                String pwd=rs.getString(1);
                if(!pwd.equals(old_pwd)){
                    Toast toast=Toast.makeText(getApplicationContext(),"原始密码有误！",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else{
                    if(!new_pwd.equals(new_pwd1)){
                        Toast toast=Toast.makeText(getApplicationContext(),"请重新确认密码！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else{
                        sql="update student set sPwd=? where sNo=?";
                        pst=conn.prepareStatement(sql);
                        pst.setString(1,new_pwd);
                        pst.setString(2,sNo);
                        pst.execute();
                        pst.close();

                        Toast toast=Toast.makeText(getApplicationContext(),"修改密码成功！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
                Looper.loop();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
