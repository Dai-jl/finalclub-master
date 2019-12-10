package com.czq.club;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ClubActivity1 extends AppCompatActivity {
    private ChangeType changeType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_describtion);

        //返回的点击事件
        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent =new Intent();
//                intent.setClass(MainActivity1.this,MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //对对应点击社团信息赋值
        changeType=new ChangeType();

        ImageView club_logo=(ImageView) findViewById(R.id.club_logo);
        TextView member_count=(TextView) findViewById(R.id.member_count);
        TextView club_des=(TextView) findViewById(R.id.club_describtion);
        TextView club_name=(TextView) findViewById(R.id.club_name);
        TextView club_thingd=(TextView) findViewById(R.id.club_things);

        byte[] in = getIntent().getByteArrayExtra("myclub_logo");
        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        Drawable drawable = new BitmapDrawable(bmpout);
        club_logo.setImageDrawable(drawable);
        member_count.setText(Integer.toString((int)getIntent().getSerializableExtra("myclub_num")));
        club_name.setText((String)getIntent().getSerializableExtra("myclub_name"));
        club_des.setText((String)getIntent().getSerializableExtra("myclub_des"));
        club_thingd.setText((String)getIntent().getSerializableExtra("myclub_things"));


    }
}
