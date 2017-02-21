package com.example.hasee.materialtest.ToDolist;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.hasee.materialtest.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hasee on 2017/2/10.
 */

public class RecyclerBin extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private List<RecyclerBinItem> mRecyclerBinItem=new ArrayList<RecyclerBinItem>();
    private MyBinAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_bin);

        Toolbar toolbar=(Toolbar)findViewById(R.id.bin_toolbar);
        setSupportActionBar(toolbar);

        init();
        mRecyclerView=(RecyclerView)findViewById(R.id.bin_recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter=new MyBinAdapter(RecyclerBin.this,mRecyclerBinItem);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setmOnItemClickListener(new MyBinAdapter.OnItemClickListener() {
            @Override
            public void OnItemLongClick(View view, final int position) {
                    View popupView=getLayoutInflater().inflate(R.layout.pop_window,null);
                    final PopupWindow popupWindow=new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
                    popupWindow.setContentView(popupView);

                    final List<String> data=new ArrayList<String>();
                    data.add("恢复");
                    data.add("删除");
                    ListView listView=(ListView)popupView.findViewById(R.id.pop_list);
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecyclerBin.this, android.R.layout.simple_list_item_1, data);
                    listView.setAdapter(adapter);

                    popupWindow.setFocusable(true);
                    popupWindow.setTouchable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupView.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
                    popupWindow.showAsDropDown(view);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (data.get(i).equals("恢复")){
                                RecyclerItem recyclerItem=new RecyclerItem();
                                recyclerItem.setTask(mRecyclerBinItem.get(position).getTask());
                                recyclerItem.setDegree(mRecyclerBinItem.get(position).getDegree());
                                recyclerItem.setExp(mRecyclerBinItem.get(position).getExp());
                                new Date();
                                long nowTime=System.currentTimeMillis();
                                recyclerItem.setSaveTime(nowTime);
                                recyclerItem.save();
                                String task=mRecyclerBinItem.get(position).getTask();
                                DataSupport.deleteAll(RecyclerBinItem.class,"task=?",task);
                                refresh();
                                popupWindow.dismiss();
                            }
                            if (data.get(i).equals("删除")){
                                String task=mRecyclerBinItem.get(position).getTask();
                                DataSupport.deleteAll(RecyclerBinItem.class,"task=?",task);
                                refresh();
                                popupWindow.dismiss();
                            }
                        }
                    });
                }
        });
    }
    private void init(){
        List<RecyclerBinItem> recyclerBinItems= DataSupport.findAll(RecyclerBinItem.class);
        for (RecyclerBinItem recyclerBinItem:recyclerBinItems){
            mRecyclerBinItem.add(recyclerBinItem);
        }
    }

    private void refresh(){
        mRecyclerBinItem.clear();
        init();
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter=new MyBinAdapter(RecyclerBin.this,mRecyclerBinItem);
    }

}