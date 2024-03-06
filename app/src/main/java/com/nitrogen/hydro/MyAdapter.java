package com.nitrogen.hydro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context ctx;
    List<MyModel> userModelList;
    public MyAdapter(Context ctx, List<MyModel> userModelList) {
        this.ctx = ctx;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.my_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fill(userModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView shenase, thrift, date, rank, score;
        CircularProgressIndicator circularProgress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shenase = itemView.findViewById(R.id.shenase);
            date = itemView.findViewById(R.id.date);
            rank = itemView.findViewById(R.id.rank);
            circularProgress = itemView.findViewById(R.id.progress);
            thrift = itemView.findViewById(R.id.thrift);
            score = itemView.findViewById(R.id.score);
        }
        private void fill(MyModel model){
            shenase.setText("شناسه: "+model.getShenase());
            circularProgress.setProgress(model.getThrift(),true);
            if (model.getThrift() >= 0 && model.getThrift() <= 30){
                score.setText("کم مصرف");
            }else if (model.getThrift() >= 30 && model.getThrift() <= 60){
                score.setText("بهینه");
            }else if (model.getThrift() >= 60 && model.getThrift() <= 90){
                score.setText("پر مصرف");
            }
            thrift.setText(""+model.getThrift()+" لیتر مصرف آب ");
            date.setText("تاریخ: "+model.getDate());
            rank.setText("رتبه مشترک: "+model.getScore());

        }
    }
}
