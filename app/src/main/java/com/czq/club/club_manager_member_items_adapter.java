package com.czq.club;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.czq.club.club_manager_member_items;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class club_manager_member_items_adapter extends ArrayAdapter<club_manager_member_items>{


    private int resourceId;

    public club_manager_member_items_adapter(Context context, int textViewResourceId, List<club_manager_member_items> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        club_manager_member_items item=getItem(position);
        View view;
        ViewHolder viewHolder;


        view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView title=view.findViewById(R.id.title);

        if(convertView==null){
            if (position==0){
                title.setVisibility(View.VISIBLE);
                title.setText("社长");
            }
            else if (position==1){
                title.setVisibility(View.VISIBLE);
                title.setText("社员");
            }

            viewHolder=new ViewHolder();
            viewHolder.name=(TextView)view.findViewById(R.id.stuname);
            viewHolder.branch=(TextView)view.findViewById(R.id.member_fenyuan);
            viewHolder.header=(CircleImageView)view.findViewById(R.id.img_header);
            viewHolder.number=(TextView)view.findViewById(R.id.stunumber);
            viewHolder.phone=(TextView)view.findViewById(R.id.member_phone);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.name.setText(item.getName());
        viewHolder.number.setText(item.getNumber());
        viewHolder.branch.setText(item.getBranch());
        viewHolder.phone.setText(item.getPhone());
        viewHolder.header.setImageDrawable(item.getImageid());
        return view;
    }

    class ViewHolder{
        TextView name;
        TextView number;
        TextView phone;
        TextView branch;
        ImageView header;
    }
}
