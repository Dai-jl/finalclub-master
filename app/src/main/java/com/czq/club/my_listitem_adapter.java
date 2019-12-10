package com.czq.club;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class my_listitem_adapter extends ArrayAdapter<my_listitem> {
    private int resourceId;

    public my_listitem_adapter(Context context, int textViewResourceId, List<my_listitem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        my_listitem item=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.name=(TextView)view.findViewById(R.id.my_listitem_name);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.name.setText(item.getName());
        return view;
    }
    class ViewHolder{
        TextView name;
    }
}
