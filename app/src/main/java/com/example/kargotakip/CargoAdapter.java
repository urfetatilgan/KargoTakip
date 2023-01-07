package com.example.kargotakip;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cargo_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cargo_no = itemView.findViewById(R.id.tv_Cargo);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

}
