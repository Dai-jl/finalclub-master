package com.czq.club;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.widget.EdgeEffectCompat;
import androidx.viewpager.widget.ViewPager;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IntroductionsActivity extends Activity implements ViewPager.OnPageChangeListener {
    private IntroductionsAdapter vpAdapter;
    private List<View> views;
    //引导图片资源
    private static final int[] pics = { R.drawable.page1, R.drawable.page3, R.drawable.page2 };
    EdgeEffectCompat leftEdge;
    EdgeEffectCompat rightEdge;
    ViewPager viewPager;
    ImageView img_dot1;
    ImageView img_dot2;
    ImageView img_dot3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagegroup);
        initViews();
        init();
    }

    private void initViews(){
        viewPager = findViewById(R.id.viewPager);
        img_dot1=findViewById(R.id.img_dot1);
        img_dot2=findViewById(R.id.img_dot2);
        img_dot3=findViewById(R.id.img_dot3);
    }

    void init() {
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);

        //初始化引导图片列表
        for (int pic : pics) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);    //使轮播的图片占满全屏
            iv.setImageResource(pic);
            views.add(iv);
        }
        //初始化Adapter
        vpAdapter = new IntroductionsAdapter(views);
        viewPager.setAdapter(vpAdapter);
        //绑定回调
        viewPager.setOnPageChangeListener(this);

        try {
            Field leftEdgeField = viewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position==views.size()-1){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(rightEdge!=null&&!rightEdge.isFinished()){//到了最后一张并且还继续拖动则跳转到其他activity
            Log.d("Introduction:","实现");
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setCurDot(int position) {
        resetDot();
        switch (position) {
            case 0:
                img_dot1.setImageResource(R.drawable.cycle);
                break;
            case 1:
                img_dot2.setImageResource(R.drawable.cycle);
                break;
            case 2:
                img_dot3.setImageResource(R.drawable.cycle);
                break;
        }
    }

    private void resetDot() {
        img_dot1.setImageResource(R.drawable.no_cycle);
        img_dot2.setImageResource(R.drawable.no_cycle);
        img_dot3.setImageResource(R.drawable.no_cycle);
    }

}