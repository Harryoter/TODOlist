package com.example.hasee.materialtest.ToDolist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hasee.materialtest.R;

import java.util.List;

/**
 * Created by hasee on 2017/2/13.
 */

public class MyBinAdapter extends RecyclerView.Adapter<MyBinAdapter.MyBinViewHolder> {
    private Context mContext;
    private List<RecyclerBinItem> mList;
    public MyBinAdapter(Context context, List<RecyclerBinItem> list){
        this.mList=list;
        this.mContext=context;
    }

    public interface OnItemClickListener{
        void OnItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public MyBinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyBinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_bin_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyBinViewHolder holder, int position) {
        RecyclerBinItem recyclerBinItem=mList.get(position);
        holder.taskView.setText(recyclerBinItem.getTask());
        holder.degreeView.setText("难度:"+String.valueOf(recyclerBinItem.getDegree()));
        holder.expView.setText("Exp:"+recyclerBinItem.getExp());
        if (mOnItemClickListener!=null){
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

    class MyBinViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView taskView;
        TextView degreeView;
        TextView expView;
        public MyBinViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView;
            taskView=(TextView)itemView.findViewById(R.id.recycler_bin_item_task);
            degreeView=(TextView)itemView.findViewById(R.id.recycler_bin_item_degree);
            expView=(TextView)itemView.findViewById(R.id.recycler_bin_item_exp);
        }
    }
}
