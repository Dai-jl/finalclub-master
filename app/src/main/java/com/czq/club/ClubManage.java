package com.czq.club;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubManage extends Activity {
    private List<club_manager_member_items> memberlist=new ArrayList<>();
    private List<ClubManagerActivityItems> activityItems=new ArrayList<>();
    //创建推送

    private static final int LEADER = 2;
    private static final int NOTLEADER = 1;
    private static final int COMPLETED = 0;
    private Button btn_take_photo, btn_pick_photo,btn_cancle,cancel_save;
    private LinearLayout layout;
    private ImageView picture;
    private EditText et2,et3,et4;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private Uri imageUri;
    private ChangeType changeType;
    ClubManagerActivityItemAdapter activityItemAdapter;
    //页面刷新
    private SwipeRefreshLayout refreshactivity;

    //人员的管理
    //人员管理
    private String sNo,identity;
    private ImageView change_pwd_imgback;

    private BottomNavigationView bottomNavigationView;
    //底部导航控件点击事件弹框
    private AlertDialog alertDialog5,alertDialog4;
    private AlertDialog.Builder alertDialog7;
    private List<String> items5;   //单选框列表
    private Map<String,String> name_num=new HashMap<String,String>();
    private int cid,index,cidd;

    //底部管理社团人员的导航item的点击事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_member:
                    alertDialog7 = new AlertDialog.Builder(ClubManage.this);
                    View view1 = View.inflate(ClubManage.this, R.layout.club_manager_member_add, null);
                    final EditText number = view1.findViewById(R.id.add_number);
                    final EditText name = view1.findViewById(R.id.add_name);
                    final EditText time = view1.findViewById(R.id.add_time);
                    alertDialog7
                            .setTitle("新增社团成员：")
                            .setIcon(R.drawable.tips)
                            .setView(view1)
                            .setPositiveButton("新增成员", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MyThread3 myThread3=new MyThread3(number.getText().toString(),time.getText().toString());
                                    myThread3.start();

                                    finish();Intent intent = new Intent(ClubManage.this, ClubManage.class);
                                    intent.putExtra("sNo",sNo);
                                    intent.putExtra("identity",identity);
                                    intent.putExtra("cid",cidd);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create();
                    alertDialog7.show();
                    return true;
                case R.id.dele_member:
                    int size=items5.size();
                    final String[] aitem = items5.toArray(new String[size]);
                    final boolean[] booleans = new boolean[size];
                    for(int i=0;i<size;i++)
                        booleans[i]=false;
                    alertDialog5 = new AlertDialog.Builder(ClubManage.this)
                            .setTitle("选择你要删除的成员：")
                            .setIcon(R.drawable.tips)
                            .setMultiChoiceItems(aitem, booleans, new DialogInterface.OnMultiChoiceClickListener() {//创建多选框
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                    booleans[i] = b;
                                }
                            })
                            .setPositiveButton("删除成员", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int j = 0; j < booleans.length; j++) {
                                        if (booleans[j]) {
                                            //删除成员操作
                                            Thread thread = new MyThread1(name_num.get(aitem[j]));
                                            thread.start();
                                        }
                                    }
                                    finish();
                                    Intent intent = new Intent(ClubManage.this, ClubManage.class);
                                    intent.putExtra("sNo",sNo);
                                    intent.putExtra("identity",identity);
                                    intent.putExtra("cid",cidd);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog5.hide();
                                }
                            })
                            .create();
                    alertDialog5.show();
                    return true;
                case R.id.transfer_leader:
                    int size1=items5.size();
                    final String[] aitem1 = items5.toArray(new String[size1]);
                    alertDialog4 = new AlertDialog.Builder(ClubManage.this)
                            .setTitle("选择你要转让社长的成员：")
                            .setIcon(R.drawable.tips)
                            .setSingleChoiceItems(aitem1, 0, new DialogInterface.OnClickListener() {//添加单选框
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    index = i;
                                }
                            })
                            .setPositiveButton("转让社长", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MyThread2 mtd=new MyThread2(name_num.get(aitem1[index]),aitem1[index]);
                                    mtd.start();

                                    setContentView(R.layout.club_manage_none);
                                    //返回按钮
                                    ImageView change_pwd_imgback1=(ImageView)findViewById(R.id.change_pwd_imgback);
                                    change_pwd_imgback1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog4.hide();
                                }
                            })
                            .create();
                    alertDialog4.show();
                    return true;
            }
            return false;
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      setContentView(R.layout.club_manage);
//页面刷新
        //获得社团成员列表
        sNo = (String) getIntent().getSerializableExtra("sNo");
        identity=(String)getIntent().getSerializableExtra("identity");
        if(getIntent().getSerializableExtra("cid")!=null)
            cidd=(int)getIntent().getSerializableExtra("cid");

        Log.d("ClubManage",identity+"#"+cidd);

        Mythread mythread = new Mythread();
        new Thread(mythread).start();

        if(identity.equals("leader")){
            setContentView(R.layout.club_manage);



            refreshactivity = (SwipeRefreshLayout) findViewById(R.id.refresh_activity);
            refreshactivity.setColorSchemeResources(R.color.colorPrimary);

            refreshactivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshActivity();
                }
            });
            Mythreadactivity mythreadactivity = new Mythreadactivity();
            new Thread(mythreadactivity).start();


            items5 = new ArrayList<String>();

            bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                //返回按钮
                change_pwd_imgback = (ImageView) findViewById(R.id.change_pwd_imgback);
                change_pwd_imgback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });


                //头部三个按钮和按钮下面的线
                final Button people = (Button) findViewById(R.id.button_people);
                final Button task = (Button) findViewById(R.id.button_activity);
                final Button passage = (Button) findViewById(R.id.button_passage);
                final TextView linepeople = (TextView) findViewById(R.id.line_people);
                final TextView linetask = (TextView) findViewById(R.id.line_task);
                final TextView linepassage = (TextView) findViewById(R.id.line_passage);


                final LinearLayout linearpeople = (LinearLayout) findViewById(R.id.club_manager_member);

                final LinearLayout lineartask = (LinearLayout) findViewById(R.id.clubmanage_activity);
                final RelativeLayout relativeLayoutpassage = (RelativeLayout) findViewById(R.id.club_manager_passage);

                final ListView memberlistview = (ListView) findViewById(R.id.listview_member);

                final ListView activitylistview = (ListView) findViewById(R.id.listview_activity);
                final Button createactivity = (Button) findViewById(R.id.button_create_activity);

                //点击人员按钮的点击事件
                people.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        people.setTextColor(0xff0AAD32);
                        task.setTextColor(0xff000000);
                        passage.setTextColor(0xff000000);
                        linepeople.setVisibility(View.VISIBLE);
                        linepeople.setBackgroundColor(0xff0AAD32);
                        linepassage.setVisibility(View.GONE);
                        linetask.setVisibility(View.GONE);

                        lineartask.setVisibility(View.GONE);
                        relativeLayoutpassage.setVisibility(View.GONE);
                        linearpeople.setVisibility(View.VISIBLE);

                    }
                });

//点击任务按钮的点击事件
                task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task.setTextColor(0xff0AAD32);
                        people.setTextColor(0xff000000);
                        passage.setTextColor(0xff000000);
                        linetask.setVisibility(View.VISIBLE);

                        linetask.setBackgroundColor(0xff0AAD32);
                        linepassage.setVisibility(View.GONE);
                        linepeople.setVisibility(View.GONE);

                        lineartask.setVisibility(View.VISIBLE);
                        relativeLayoutpassage.setVisibility(View.GONE);
                        linearpeople.setVisibility(View.GONE);

                    }
                });

                //点击推送按钮的点击事件
                passage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passage.setTextColor(0xff0AAD32);
                        task.setTextColor(0xff000000);
                        people.setTextColor(0xff000000);
                        linepassage.setVisibility(View.VISIBLE);
                        linepassage.setBackgroundColor(0xff0AAD32);
                        linepeople.setVisibility(View.GONE);
                        linetask.setVisibility(View.GONE);

                        lineartask.setVisibility(View.GONE);
                        relativeLayoutpassage.setVisibility(View.VISIBLE);
                        linearpeople.setVisibility(View.GONE);
                    }
                });

                club_manager_member_items_adapter member_items_adapter = new club_manager_member_items_adapter(ClubManage.this, R.layout.club_manager_menber_item, memberlist);
                memberlistview.setAdapter(member_items_adapter);
//活动List view


                activityItemAdapter = new ClubManagerActivityItemAdapter(ClubManage.this, R.layout.club_manager_task_item, activityItems);
                activitylistview.setAdapter(activityItemAdapter);

                createactivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(ClubManage.this, ClubManagerCreateActivity.class);
                        intent1.putExtra("cid",cidd);
                        Log.d("bbb",cidd+"");
                        startActivity(intent1);
                    }
                });


                //创建推送
                btn_take_photo = (Button) this.findViewById(R.id.take_photo);
                btn_pick_photo = (Button) this.findViewById(R.id.choose_from_album);
                btn_cancle = (Button) this.findViewById(R.id.cancle);
                picture = (ImageView) this.findViewById(R.id.picture);
                cancel_save = (Button) this.findViewById(R.id.cancel_save);
                et2 = (EditText) this.findViewById(R.id.et2);
                et3 = (EditText) this.findViewById(R.id.et3);
                et4 = (EditText) this.findViewById(R.id.et4);

                layout = (LinearLayout) findViewById(R.id.pop_layout);

                picture.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        layout.setVisibility(View.VISIBLE);
                        cancel_save.setVisibility(View.GONE);
                    }
                });
                //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
                layout.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                //添加按钮监听
                //拍照监听
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //创建File对象用于存储拍照后的照片
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(ClubManage.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        //启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                });

                //相册选择监听
                btn_pick_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(ClubManage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ClubManage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                    }
                });

                //取消监听
                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layout.setVisibility(View.GONE);
                        cancel_save.setVisibility(View.VISIBLE);
                    }
                });

                //发布监听
                cancel_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String theme = et2.getText().toString();
                        String time = et3.getText().toString();
                        String url = et4.getText().toString();
                        //  Log.d("SelectPicPopupWindow:", club_name + "#" + theme + "#" + time + "#" + url);

                        addpassage addp=new addpassage();
                        new Thread(addp).start();
                    }
                });


        }
        else{
            setContentView(R.layout.club_manage_none);
            //返回按钮
            change_pwd_imgback = (ImageView) findViewById(R.id.change_pwd_imgback);
            change_pwd_imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

    }




    //创建推送
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        //将拍照照片显示出来
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setBackgroundColor(android.graphics.Color.parseColor("#00ffffff"));
                        picture.setImageBitmap(bitmap);
                        layout.setVisibility(View.GONE);
                        cancel_save.setVisibility(View.VISIBLE);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19)
                        handleImageOnKitKat(data);
                    else
                        handleImageBeforeKitKat(data);
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);    //打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documnets".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setBackgroundColor(android.graphics.Color.parseColor("#00ffffff"));
            picture.setImageBitmap(bitmap);
            layout.setVisibility(View.GONE);
            cancel_save.setVisibility(View.VISIBLE);
        }else
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
    }
//activity 线程以及刷新
    //handle得到线程的数据
private Handler handler=new Handler() {
    public void handleMessage(Message msg) {
        switch(msg.what){
            case COMPLETED :
            activityItemAdapter.notifyDataSetChanged();
            refreshactivity.setRefreshing(false);
            break;
        }

    }
};

    private void refreshActivity(){
        new Thread(new Runnable() {
            public void run() {
                DBUtils dbUtils = new DBUtils();
                try {
                    Connection conn = dbUtils.getConnection();
                    String sql="select count(*) from activity where cid = ?";
                    java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                    pst.setInt(1,cid);
                    java.sql.ResultSet rs=pst.executeQuery();



                    while(rs.next()) {
                        int i=rs.getInt(1);
                        if (i> activityItems.size()) {
                            sql = "select aname , astatus from activity where cid = ? order by aid DESC LIMIT ? ";
                            pst = conn.prepareStatement(sql);
                            pst.setInt(2, i-activityItems.size());
                            pst.setInt(1,cid);
                            rs = pst.executeQuery();
                            while(rs.next()) {
                                String aname = rs.getString(1);
                                String astatus = rs.getString(2);

                                //index = rs.getInt(3);
                                ClubManagerActivityItems item1 = new ClubManagerActivityItems(aname, astatus);
                                activityItems.add(item1);
                            }

                        }
                    }
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler.sendMessage(msg);
                    pst.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //活动信息拿出来
    private class Mythreadactivity implements Runnable{
        public void run() {
            //   BeanActivity activity = new BeanActivity();
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();

                String sql="select cId from joinclub where sNo=? and type=?";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,sNo);
                pst.setString(2,"leader");
                java.sql.ResultSet rs=pst.executeQuery();

                if(rs.next()) {
                    cid = rs.getInt(1);

                    sql = "select aname , astatus from activity where cid = ?";//还要加一个where的条件
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, cid);

                    Log.d("aa", String.valueOf(cid));

                    rs = pst.executeQuery();
                    while (rs.next()) {
                        String aname = rs.getString(1);
                        String astatus = rs.getString(2);
                        ClubManagerActivityItems item1 = new ClubManagerActivityItems(aname, astatus);
                        activityItems.add(item1);
                    }


                    pst.close();
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //人员管理线程
    private class Mythread implements Runnable{
        public void run() {
            ChangeType changeType = new ChangeType();
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql="select cId from joinclub where sNo=? and type=?";
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,sNo);
                pst.setString(2,"leader");
                java.sql.ResultSet rs=pst.executeQuery();
                if(rs.next()) {
                    cid = rs.getInt(1);
                    sql = "select s.sNo,s.sName,s.sPhone,s.collegeName,s.sImage from student s,joinclub j where j.cId=? and j.sNo=s.sNo order by j.type asc";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, cid);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        club_manager_member_items member = new club_manager_member_items();

                        member.setNumber(rs.getString(1));
                        member.setName(rs.getString(2));
                        member.setPhone(rs.getString(3));
                        member.setBranch(rs.getString(4));

                        items5.add(rs.getString(2));  //保存学生姓名
                        name_num.put(rs.getString(2), rs.getString(1));

                        Log.d("学生",rs.getString(2));
                        byte[] in = changeType.blobToBytes(rs.getBlob(5));
                        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                        Drawable drawable = new BitmapDrawable(bmpout);
                        member.setImageid(drawable);

                        memberlist.add(member);
                    }

                }

                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class addpassage implements Runnable {
        public void run() {
            ChangeType changeType = new ChangeType();
            DBUtils dbUtils = new DBUtils();
            int pid = 0;
            try {
                Looper.prepare();
                Connection conn = dbUtils.getConnection();


                String sql = "select max(pId) from passage";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery();
                if (rs.next())
                    pid = rs.getInt(1) + 1;


                sql = "select cId from joinclub where sNo=? and type=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, sNo);
                pst.setString(2, "leader");
                rs = pst.executeQuery();

                if (rs.next()) {

                    Log.d("aaaa", "run: ");

                    cid = rs.getInt(1);
                    sql = "insert into passage(pId,aId,pName,cId,pContent,pImage,pTime) values(?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, pid);
                    pst.setInt(2, 1);
                    pst.setString(3, et2.getText().toString());
                    pst.setInt(4, cid);
                    pst.setString(5, et4.getText().toString());
                    pst.setString(7, et3.getText().toString());

                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        byte[] bytes = changeType.bitmap2Bytes(bitmap);
                        pst.setBlob(6, getContentResolver().openInputStream(imageUri));
                        pst.execute();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "推送发布成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "输入社团不存在！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                Looper.loop();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyThread1 extends Thread {
        private String number;
        public MyThread1(String number)
        {
            this.number = number;
        }
        public void run()
        {
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql = "delete from joinclub where sNo=? and cId=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,number);
                pst.setInt(2,cid);
                Log.d("删除学号",number);
                Log.d("删除所在的社团",cid+"");
                pst.execute();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //转让社长事件
    private class MyThread2 extends Thread {
        private String tran_number;
        private String tran_name;
        public MyThread2(String number,String name)
        {
            tran_number = number;
            tran_name=name;
        }
        public void run()
        {
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql = "update club set leader=? where cId=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,tran_name);
                Log.d("姓名",tran_name);
                pst.setInt(2,cid);
                pst.execute();

                sql="delete from joinclub where sNo=? and cId=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1,sNo);
                pst.setInt(2,cid);
                pst.execute();

                sql="update joinclub set type=? where sNo=? and cId=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1,"leader");
                pst.setString(2,tran_number);
                pst.setInt(3,cid);
                pst.execute();

                sql="update student set identity=? where sNo=?";
                pst=conn.prepareStatement(sql);
                pst.setString(1,"student");
                pst.setString(2,sNo);
                pst.execute();

                sql="update student set identity=? where sNo=?";
                pst=conn.prepareStatement(sql);
                pst.setString(1,"leader");
                pst.setString(2,tran_number);
                pst.execute();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //增加社员事件
    private class MyThread3 extends Thread {
        private String add_number;
        private String add_time;
        public MyThread3(String add_number,String add_time)
        {
            this.add_number = add_number;
            this.add_time=add_time;
        }
        public void run()
        {
            DBUtils dbUtils = new DBUtils();
            try {
                Connection conn = dbUtils.getConnection();
                String sql = "insert into joinclub values(?,?,?,?)";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1,add_number);
                pst.setInt(2,cid);
                pst.setString(3,"member");
                pst.setString(4,add_time);
                pst.execute();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
