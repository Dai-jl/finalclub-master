package com.czq.club;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Myclub_passageAdapter extends RecyclerView.Adapter<Myclub_passageAdapter.ViewHolder> {
    private List<BeanMyclub_passage> myclub_passages;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myclub_logo;
        TextView myclub_name;
        TextView time;
        TextView passage_sumary;
        ImageView passage_photo;

        public ViewHolder(View view) {
            super(view);
            myclub_logo = (ImageView) view.findViewById(R.id.club_logo);
            myclub_name = (TextView) view.findViewById(R.id.club_name);
            time = (TextView) view.findViewById(R.id.time);
            passage_sumary=(TextView)view.findViewById(R.id.passage_sumary);
            passage_photo=(ImageView)view.findViewById(R.id.passage_photo);
        }
    }

    public Myclub_passageAdapter(List<BeanMyclub_passage> Myclub_passages){
        myclub_passages=Myclub_passages;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myclub_passage, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.passage_sumary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                BeanMyclub_passage passage = myclub_passages.get(position);
                //Toast.makeText(v.getContext(), "社团头像点击：你点击了社团：" + myclub.getMyclub_name(), Toast.LENGTH_SHORT).show();
                //跳转到推送的网页
                Intent intent =new Intent();
                intent.setClass(v.getContext(),Passage_web.class);
                intent.putExtra("passage_url", passage.getPassage_url());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanMyclub_passage myclub_passage = myclub_passages.get(position);
        holder.myclub_name.setText(myclub_passage.getMyclub_name());
        holder.myclub_logo.setImageDrawable(myclub_passage.getMyclub_logo());
        holder.time.setText(myclub_passage.getTime());
        holder.passage_sumary.setText(myclub_passage.getPassage_sumary());
        holder.passage_photo.setImageDrawable(myclub_passage.getPassage_photo());
    }

    public int getItemCount() {
        return myclub_passages.size();
    }
}
