package com.example.asus.studentmgr;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by asus on 2019/5/8.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private Bitmap bitmap;
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

    public PictureAdapter(Bitmap bitmap) {
        this.bitmap=bitmap;
    }

    @Override
    public PictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_big_show, parent, false);
        PictureAdapter.ViewHolder viewHolder = new PictureAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PictureAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmap);
        //int adapterPosition = holder.getAdapterPosition();
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new MyOnClickListener(position, bitmap));
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new MyOnLongClickListener(position, bitmap));
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.big_image);
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
