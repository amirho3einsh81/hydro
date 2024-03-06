package com.nitrogen.hydro;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BestuserAdapter extends RecyclerView.Adapter<BestuserAdapter.ViewHolder> {

    Context ctx;
    List<UserModel> userModelList;
    public BestuserAdapter(Context ctx, List<UserModel> userModelList) {
        this.ctx = ctx;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_user, parent,false));
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

        AppCompatTextView name, thrift;
        LinearLayoutCompat color;
        RelativeLayout root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color);
            name = itemView.findViewById(R.id.name);
            root = itemView.findViewById(R.id.root);
            thrift = itemView.findViewById(R.id.thrift);
        }
        private void fill(UserModel model){
            name.setText(model.getName());
            color.setBackgroundColor(Color.parseColor("#"+model.getColor()));
            root.setBackgroundColor(Color.parseColor("#55"+model.getColor()));
            thrift.setText(model.getPresent()+" لیتر مصرف آب ");
        }
    }
}
