package com.example.kargotakip;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CargoAdapter extends RecyclerView.Adapter<CargoAdapter.ViewHolder> {
    public Context context;
    public ArrayList<Cargo> mList;
    public View.OnClickListener mOnItemClickListener;

    public CargoAdapter(Context context, ArrayList<Cargo> mList) {
        this.context = context;
        this.mList = mList;
    }
    @NonNull
    @Override
    public CargoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cargo_layout, parent, false);
        return new CargoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CargoAdapter.ViewHolder holder, int position) {
        holder.cargo_no.setText(mList.get(position).getCargo_no());
        holder.cargo_name.setText(mList.get(position).getCargo_name());
        holder.cargo_status.setText(mList.get(position).getCargo_status());
        if(mList.get(position).getCargo_name()=="ARAS KARGO"){
            holder.cargo_iv.setImageResource(R.drawable.aras);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cargo_no , cargo_name, cargo_status;
        ImageView cargo_iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cargo_no = itemView.findViewById(R.id.tv_CargoNo);
            cargo_name = itemView.findViewById(R.id.tv_CargoName);
            cargo_status = itemView.findViewById(R.id.tv_CargoStatus);
            cargo_iv = itemView.findViewById(R.id.iv_CargoImage);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

}
