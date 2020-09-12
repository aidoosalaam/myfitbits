package com.charmedteeth.sala;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DevicesArrayAdaptor extends RecyclerView.Adapter<DevicesArrayAdaptor.ViewHolder> {

    private List<String> devicesLis;

    public DevicesArrayAdaptor(List<String> devicesLis) {
        this.devicesLis = devicesLis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_item_device_,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDeviceName.setText(devicesLis.get(position));
    }

    @Override
    public int getItemCount() {
        return devicesLis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDeviceName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeviceName = itemView.findViewById(R.id.txt_device_name);

        }
    }
}
