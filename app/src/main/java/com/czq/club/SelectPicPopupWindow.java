package com.czq.club;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;



import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

public class SelectPicPopupWindow extends Activity {
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private Uri imageUri;
    private ChangeType changeType;



    private Button btn_take_photo, btn_pick_photo,btn_cancle,cancel_save;
    private LinearLayout layout;
    private ImageView picture;
    private EditText et1,et2,et3,et4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_manage);
        btn_take_photo = (Button) this.findViewById(R.id.take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.choose_from_album);
        btn_cancle=(Button)this.findViewById(R.id.cancle);
        picture=(ImageView) this.findViewById(R.id.picture);
        cancel_save=(Button)this.findViewById(R.id.cancel_save);
        et1=(EditText)this.findViewById(R.id.et1);et2=(EditText)this.findViewById(R.id.et2);et3=(EditText)this.findViewById(R.id.et3);et4=(EditText)this.findViewById(R.id.et4);

        layout=(LinearLayout)findViewById(R.id.pop_layout);

        picture.setOnClickListener(new View.OnClickListener(){
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
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(SelectPicPopupWindow.this,"com.example.cameraalbumtest.fileprovider",outputImage);
                }else{
                    imageUri=Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        //相册选择监听
        btn_pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(SelectPicPopupWindow.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SelectPicPopupWindow.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
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
                String club_name=et1.getText().toString();
                String theme=et2.getText().toString();
                String time=et3.getText().toString();
                String url=et4.getText().toString();
                Log.d("SelectPicPopupWindow:",club_name+"#"+theme+"#"+time+"#"+url);

//                MyThread thread=new MyThread();
//                new Thread(thread).start();
            }
        });
    }

//    class MyThread implements Runnable{
//        @Override
//        public void run() {
//            int pid=1;int cid=-1;
//            changeType=new ChangeType();
//            DBUtils dbUtils = new DBUtils();
//            try {
//                Connection conn = dbUtils.getConnection();
//                String sql="select max(pId) from passage";
//                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
//                java.sql.ResultSet rs=pst.executeQuery();
//                if(rs.next())
//                    pid=rs.getInt(1)+1;
//
//                sql="select cId from club where cName=?";
//                pst=conn.prepareStatement(sql);
//                pst.setString(1,et1.getText().toString());
//                rs=pst.executeQuery();
//                if(rs.next())
//                    cid=rs.getInt(1);
////                else
////                    Toast.makeText(getApplicationContext(), "输入社团不存在！",
////                            Toast.LENGTH_SHORT).show();
//
//                sql="insert into passage(pId,aId,pName,cId,pContent,pImage,pTime) values(?,?,?,?,?,?,?)";
//                pst=conn.prepareStatement(sql);
//                pst.setInt(1,pid);
//                pst.setInt(2,1);
//                pst.setString(3,et2.getText().toString());
//                pst.setInt(4,cid);
//                pst.setString(5,et4.getText().toString());
//                pst.setString(7,et3.getText().toString());
//
//                try {
//                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                    byte[] bytes=changeType.bitmap2Bytes(bitmap);
//                    pst.setBlob(6,getContentResolver().openInputStream(imageUri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                pst.execute();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }

    @Override
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


}