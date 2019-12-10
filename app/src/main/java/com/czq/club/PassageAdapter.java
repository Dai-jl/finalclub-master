package com.czq.club;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class PassageAdapter extends BaseAdapter {
    private List<Passage> list ;
    private Context context;


    public PassageAdapter(List<Passage> list, Context context) {
        this.list = list;
        this.context=context;
    }


    public int getCount() {
        return list==null?0:list.size();
    }


    public Passage getItem(int position) {
        return list.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        Passage passage = list.get(position);
        View view;
        Holder holder;

        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.passagelist_item,null);
            holder = new Holder();
            holder.header = view.findViewById(R.id.clubimage);
            holder.header.setVisibility(view.VISIBLE);
            holder.name = view.findViewById(R.id.clubname);
            holder.name.setVisibility(view.VISIBLE);
            holder.time = view.findViewById(R.id.time);
            holder.time.setVisibility(view.VISIBLE);
            holder.title = view.findViewById(R.id.passagetitle);
            holder.title.setVisibility(view.VISIBLE);

            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (Holder)view.getTag();
        }

        holder.header.setImageResource(passage.getClub().getImageId());
        holder.name.setText(passage.getClub().getName());
        holder.time.setText(passage.getTime());
        holder.title.setText(passage.getTitle());

        return view;
    }

    private class Holder{
        ImageView header;
        TextView name;
        TextView time;
        TextView title;
        RelativeLayout title1;
        RelativeLayout title2;
    }


}

