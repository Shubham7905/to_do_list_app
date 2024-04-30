package com.shubham79059.android.todolistapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubham79059.android.todolistapp.MainActivity;
import com.shubham79059.android.todolistapp.R;
import com.shubham79059.android.todolistapp.db.entity.Work;

import java.util.ArrayList;

public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.MyViewHolder> {

    // 1 - Variables
    private Context context;
    private ArrayList<Work> worksList;
    private MainActivity mainActivity;

    // 2 - ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.desc = itemView.findViewById(R.id.desc);
        }
    }

    public WorksAdapter(Context context, ArrayList<Work> works, MainActivity mainActivity){
        this.context = context;
        this.worksList = works;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.work_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int positions) {
        final Work work = worksList.get(positions);

        holder.name.setText(work.getName());
        holder.desc.setText(work.getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.addAndEditWorks(true, work, positions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return worksList.size();
    }
}
