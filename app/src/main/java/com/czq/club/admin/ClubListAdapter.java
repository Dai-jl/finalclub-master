package com.czq.club.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.czq.club.Club;
import com.czq.club.R;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ClubListAdapter extends BaseAdapter {
    private List<Club> list ;
    private Context context;
    private ByteArrayInputStream in;

    public ClubListAdapter(List<Club> list, Context context) {
        this.list = list;
        this.context=context;
    }


    public int getCount() {
        return list==null?0:list.size();
    }


    public Club getItem(int position) {
        return list.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        Club club = list.get(position);
        View view;
        Holder holder;

        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.admin_club_list_item,null);
            holder = new Holder();
            holder.logo = view.findViewById(R.id.club_logo);
            holder.name = view.findViewById(R.id.club_name);
            holder.content = view.findViewById(R.id.club_sumary);


            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (Holder)view.getTag();
        }

        holder.logo.setImageBitmap(club.ToBitMap());
        holder.name.setText(club.getName());
        holder.content.setText(club.getIntroduction());

        return view;
    }

    private class Holder{
        de.hdodenhof.circleimageview.CircleImageView logo;
        TextView name;
        TextView content;

    }
}
