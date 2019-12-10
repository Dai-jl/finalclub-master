package com.czq.club;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ClubManagerActivityItemAdapter extends ArrayAdapter<ClubManagerActivityItems> {
    private int resourceId;

    public ClubManagerActivityItemAdapter(Context context, int textViewResourceId, List<ClubManagerActivityItems> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ClubManagerActivityItems item=getItem(position);
        View view;
        ViewHolder viewHolder;


        view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        if(convertView==null){


            viewHolder=new ViewHolder();
            viewHolder.name=(TextView)view.findViewById(R.id.activity_name);
            viewHolder.state=(TextView)view.findViewById(R.id.activity_state);

            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.name.setText(item.getName());
        viewHolder.state.setText(item.getState());
        return view;
    }

    class ViewHolder{
        TextView name;
        TextView state;
    }
}
