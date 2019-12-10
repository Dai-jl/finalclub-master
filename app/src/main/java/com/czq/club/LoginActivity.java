package com.czq.club;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.czq.club.admin.AdminActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginActivity extends AppCompatActivity {



    private TextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    //wcc
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //wcc
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the login form.
        mEmailView = (TextView) findViewById(R.id.email);
    //    populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
             //   if () {
            //        attemptLogin();
               //     return true;
                //}
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //wcc
                account = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();

                MyThread thread=new MyThread();
                new Thread(thread).start();

                editor = pref.edit();
                if (rememberPass.isChecked()) {
                    //检查复选框是否被选中
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", account);
                    editor.putString("password", password);
                } else {
                    editor.clear();
                }
                editor.commit();


            }
        });


        //wcc
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            //将账号和密码都设置到文本框中
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            mEmailView.setText(account);
            mPasswordView.setText(password);
            rememberPass.setChecked(true);

        }




    }

    private class MyThread implements Runnable{
        @Override
        public void run() {

            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                Looper.prepare();
                if(mEmailView.getText().toString().charAt(0) == 'J'){
                    String sql = "select password from admin where adminId = ?";
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1,mEmailView.getText().toString());
                    java.sql.ResultSet rs = pst.executeQuery();
                    String pwd;
                    if(rs.next()) {
                        pwd = rs.getString(1);
                        if (pwd.equals(mPasswordView.getText().toString())) {
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast toast = Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                }else {

                    String sql = "select sPwd,identity,sName,sNo,sImage from student where sNo=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, account);
                    ResultSet rs = pst.executeQuery();

                    if (!rs.next()) {
                        Toast toast = Toast.makeText(LoginActivity.this, "输入的用户不合法！", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        String pwd = rs.getString(1);
                        String identity = rs.getString(2);

                        if (!pwd.equals(password)) {
                            Toast toast = Toast.makeText(getApplicationContext(), "输入的密码有误！", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            ChangeType changeType = new ChangeType();

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MyActivity.class);
                            intent.putExtra("identity", identity);
                            intent.putExtra("isload", "yes");
                            intent.putExtra("name", rs.getString(3));
                            intent.putExtra("number", rs.getString(4));

                            byte[] header = changeType.blobToBytes(rs.getBlob(5));
                            intent.putExtra("header", header);


                            sql="select cId from joinclub where sNo=? and type=?";
                            pst=conn.prepareStatement(sql);
                            pst.setString(1,rs.getString(4));
                            pst.setString(2,"leader");
                            rs=pst.executeQuery();

                            int cid=0;
                            if(rs.next())
                                cid = rs.getInt(1);
                            intent.putExtra("cid", cid);

                            Log.d("LoginActivity",cid+"#"+identity);

                            LoginActivity.this.startActivity(intent);
                        }

                    }}
                    Looper.loop();

            }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
}