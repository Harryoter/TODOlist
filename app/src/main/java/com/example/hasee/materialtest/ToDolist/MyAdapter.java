package com.example.hasee.materialtest.ToDolist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hasee.materialtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/1/29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private List<RecyclerItem> mDatas;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void OnItemLongClick(View view,int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }

    public MyAdapter(Context context, List<RecyclerItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RecyclerItem recyclerItem = mDatas.get(position);
        holder.taskView.setText(recyclerItem.getTask());
        holder.degreeView.setText("难度:" + recyclerItem.getDegree());
        holder.expView.setText("Exp:" + recyclerItem.getExp());
        holder.timeView.setText(recyclerItem.getTime());
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.OnItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView taskView;
        TextView degreeView;
        TextView expView;
        TextView timeView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            taskView = (TextView) view.findViewById(R.id.recycler_item_1);
            degreeView = (TextView) view.findViewById(R.id.recycler_item_2);
            expView = (TextView) view.findViewById(R.id.recycler_item_3);
            timeView=(TextView)view.findViewById(R.id.recycler_item_4);
        }
    }
    public void setFilter(List<RecyclerItem> recyclerItems){
        mDatas=new ArrayList<>();
        mDatas.addAll(recyclerItems);
        notifyDataSetChanged();
    }
}
