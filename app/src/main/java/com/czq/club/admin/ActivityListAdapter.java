package com.czq.club.admin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.czq.club.Activity;
import com.czq.club.R;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ActivityListAdapter extends BaseAdapter {
    private List<Activity> list ;
    private Context context;
    private ByteArrayInputStream in;

    public ActivityListAdapter(List<Activity> list, Context context) {
        this.list = list;
        this.context=context;
    }


    public int getCount() {
        return list==null?0:list.size();
    }


    public Activity getItem(int position) {
        return list.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        Activity activity = list.get(position);
        View view;
        Holder holder;

        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.admin_activity_list_item,null);
            holder = new Holder();
            holder.logo = view.findViewById(R.id.club_logo);
            holder.clubName = view.findViewById(R.id.club_name);
            holder.aName = view.findViewById(R.id.activityName);
            holder.aTime = view.findViewById(R.id.activityTime);
            holder.aStatus = view.findViewById(R.id.activityStatus);
            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (Holder)view.getTag();
        }

        holder.logo.setImageBitmap(activity.getClub().ToBitMap());
        holder.clubName.setText(activity.getClub().getName());
        holder.aName.setText("活动名称："+activity.getaName());
        String[] times = activity.getaTime().split(" ");
        holder.aTime.setText("活动时间："+times[0]);
        holder.aStatus.setText(activity.getStatus());
        //Log.i("no"+position,activity.getStatus());
        if(activity.getStatus().equals("未审核")){
            holder.aStatus.setTextColor(0xFFcc0000);
        }

        return view;
    }

    private class Holder{
        de.hdodenhof.circleimageview.CircleImageView logo;
        TextView clubName;
        TextView aName;
        TextView aTime;
        TextView aStatus;


    }
}
