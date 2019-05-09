package com.example.asus.studentmgr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2019/5/6.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Bitmap> list;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 设置点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置长按点击事件
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public Adapter(List<Bitmap> list) {
        this.list = list;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_mutiply, parent, false);
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(list.get(position));
        int adapterPosition = holder.getAdapterPosition();
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new MyOnClickListener(position, list.get(adapterPosition)));
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new MyOnLongClickListener(position, list.get(adapterPosition)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_multiply);
        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {
        private int position;
        private Bitmap data;

        public MyOnLongClickListener(int position, Bitmap data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(v, position, data);
            return true;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private Bitmap data;

        public MyOnClickListener(int position, Bitmap data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position, data);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, Bitmap data);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, Bitmap data);
    }
}




