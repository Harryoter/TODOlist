package com.example.hasee.materialtest.ToDolist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hasee.materialtest.R;
import com.example.hasee.materialtest.Weather.CoolWeather;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.hasee.materialtest.R.id.nav_bin;
import static com.example.hasee.materialtest.R.id.nav_degree;
import static com.example.hasee.materialtest.R.id.nav_delete;
import static com.example.hasee.materialtest.R.id.nav_mine;
import static com.example.hasee.materialtest.R.id.nav_time;
import static com.example.hasee.materialtest.R.id.nav_weather;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<RecyclerItem> saveRecyclerItemList=new ArrayList<>();
    private List<RecyclerItem> nowRecyclerItemList=new ArrayList<>();
    private MyAdapter adapter;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private CoordinatorLayout mCoordinatorLayout;
    private SearchView searchView;
    private TextView myExpView;
    private TextView myTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView=(RecyclerView) findViewById(R.id.recycler_view) ;
        mCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_layout);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView=(NavigationView)findViewById(R.id.nav_view);

        View view=mNavigationView.inflateHeaderView(R.layout.nav_header);
        myExpView=(TextView)view.findViewById(R.id.my_exp);
        myTitleView=(TextView)view.findViewById(R.id.title) ;
        MyExp mine=DataSupport.findLast(MyExp.class);
        if (mine!=null){
            int exp=mine.getMyExp();
            String myExp=String.valueOf(exp);
            myExpView.setText(myExp);
            String title=titleTranslator(exp);
            myTitleView.setText(title);
        }else {
            int exp=Integer.parseInt(myExpView.getText().toString());
            String title=titleTranslator(exp);
            myTitleView.setText(title);
        }


        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_white_18dp);
        }


        mNavigationView.setCheckedItem(nav_mine);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case nav_mine:
                        Intent intent=new Intent(MainActivity.this,Mine.class);
                        startActivity(intent);
                        break;
                    case nav_weather:
                        Intent intent1=new Intent(MainActivity.this, CoolWeather.class);
                        startActivity(intent1);
                        mDrawerLayout.closeDrawers();
                        break;
                    case nav_degree:
                        degreeSort();
                        mDrawerLayout.closeDrawers();
                        break;
                    case nav_time:
                        timeSort();
                        mDrawerLayout.closeDrawers();
                        break;
                    case nav_delete:
                        List<RecyclerItem>  recyclerItemList=DataSupport.findAll(RecyclerItem.class);
                        for (RecyclerItem recyclerItem:recyclerItemList){
                            RecyclerBinItem recyclerBinItem=new RecyclerBinItem();
                            recyclerBinItem.setTask(recyclerItem.getTask());
                            recyclerBinItem.setDegree(recyclerItem.getDegree());
                            recyclerBinItem.setExp(recyclerItem.getExp());
                            recyclerBinItem.save();
                        }
                        DataSupport.deleteAll(RecyclerItem.class);
                        Snackbar.make(mCoordinatorLayout,"数据已全部删除",Snackbar.LENGTH_SHORT).setAction("添加数据", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this,AddItem.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
                        mDrawerLayout.closeDrawers();
                        refresh();
                        break;
                    case nav_bin:
                        Intent intent2=new Intent(MainActivity.this,RecyclerBin.class);
                        startActivity(intent2);
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                }
                return true;
            }
        });


        LitePal.getDatabase();


        final FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddItem.class);
                startActivity(intent);
            }
        });


        init();
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new MyAdapter(MainActivity.this,nowRecyclerItemList);
        mRecyclerView.setAdapter(adapter);

        adapter.setmOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String task = nowRecyclerItemList.get(position).getTask();
                int degree = nowRecyclerItemList.get(position).getDegree();
                String exp = nowRecyclerItemList.get(position).getExp();
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                intent.putExtra("task", task);
                intent.putExtra("degree", degree);
                intent.putExtra("exp", exp);
                startActivity(intent);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(0, nowRecyclerItemList.size());
            }


            @Override
            public void OnItemLongClick(View view, final int position) {
                View popupView=getLayoutInflater().inflate(R.layout.pop_window,null);
                final PopupWindow mPopupWindow=new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
                mPopupWindow.setContentView(popupView);

                final List<String> done=new ArrayList<String>();
                done.add("完成");
                done.add("删除");
                final ListView listView=(ListView)popupView.findViewById(R.id.pop_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, done);
                listView.setAdapter(adapter);

                mPopupWindow.setFocusable(true);
                mPopupWindow.setTouchable(true);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
                mPopupWindow.showAsDropDown(view);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (done.get(i).equals("完成")){
                            myExpView=(TextView)findViewById(R.id.my_exp);
                            myTitleView=(TextView)findViewById(R.id.title);

                            MyExp myExp1=new MyExp();
                            myExp1=DataSupport.findLast(MyExp.class);
                            int savedExp=Integer.parseInt(myExpView.getText().toString());
                            String task = nowRecyclerItemList.get(position).getTask();
                            int exp =Integer.parseInt(nowRecyclerItemList.get(position).getExp().toString());
                            int nowExp=exp+savedExp;
                            String title=titleTranslator(nowExp);
                            String reverseExp=String.valueOf(nowExp);
                            myExpView.setText(reverseExp);
                            myTitleView.setText(title);
                            DataSupport.deleteAll(RecyclerItem.class,"task=?",task);
                            DataSupport.deleteAll(MyExp.class);
                            MyExp myExp2=new MyExp();
                            myExp2.setMyExp(nowExp);
                            myExp2.setTitle(title);
                            myExp2.save();
                            refresh();
                            mPopupWindow.dismiss();
                        }
                        if (done.get(i).equals("删除")){
                            RecyclerBinItem recyclerBinItem=new RecyclerBinItem();
                            recyclerBinItem.setTask(nowRecyclerItemList.get(position).getTask());
                            recyclerBinItem.setDegree(nowRecyclerItemList.get(position).getDegree());
                            recyclerBinItem.setExp(nowRecyclerItemList.get(position).getExp());
                            recyclerBinItem.save();
                            String task = nowRecyclerItemList.get(position).getTask();
                            DataSupport.deleteAll(RecyclerItem.class,"task=?",task);
                            refresh();
                            mPopupWindow.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void timeSort(){
        List<RecyclerItem> recyclerItemList=DataSupport.findAll(RecyclerItem.class);
        nowRecyclerItemList.clear();
        DataSupport.deleteAll(RecyclerItem.class);
        int []om=new int[recyclerItemList.size()];
        for (int i=0;i<recyclerItemList.size();i++){
            long iTime=recyclerItemList.get(i).getSaveTime();
            om[i]=i;
            for (int j=0;j<i;j++){
                if (recyclerItemList.get(j).getSaveTime()>iTime){
                    recyclerItemList.add(j,recyclerItemList.get(i));
                    recyclerItemList.add(i+1,recyclerItemList.get(j+1));
                    recyclerItemList.remove(j+1);
                    recyclerItemList.remove(i+1);
                }
            }
        }
        for (int i=0;i<recyclerItemList.size();i++){
            RecyclerItem recyclerItem=new RecyclerItem();
            recyclerItem.setTask(recyclerItemList.get(i).getTask());
            recyclerItem.setExp(recyclerItemList.get(i).getExp());
            recyclerItem.setTime(recyclerItemList.get(i).getTime());
            recyclerItem.setDegree(recyclerItemList.get(i).getDegree());
            recyclerItem.setSaveTime(recyclerItemList.get(i).getSaveTime());
            recyclerItem.save();
        }
        refresh();
    }

    private void degreeSort(){
        List<RecyclerItem> recyclerItemList=DataSupport.findAll(RecyclerItem.class);
        nowRecyclerItemList.clear();
        DataSupport.deleteAll(RecyclerItem.class);
        for (int i=1;i<=5;i++){
            for (int j=0;j<recyclerItemList.size();j++){
                if (recyclerItemList.get(j).getDegree()==i){
                    RecyclerItem recyclerItem=new RecyclerItem();
                    recyclerItem.setTask(recyclerItemList.get(j).getTask());
                    recyclerItem.setExp(recyclerItemList.get(j).getExp());
                    recyclerItem.setTime(recyclerItemList.get(j).getTime());
                    recyclerItem.setDegree(recyclerItemList.get(j).getDegree());
                    recyclerItem.setSaveTime(recyclerItemList.get(j).getSaveTime());
                    recyclerItem.save();
                }
            }
        }
        refresh();
    }

    private void init(){
        new Date();
        long nowTime=System.currentTimeMillis();
        saveRecyclerItemList.clear();
        nowRecyclerItemList.clear();
        saveRecyclerItemList= DataSupport.findAll(RecyclerItem.class);
        for (RecyclerItem recyclerItem:saveRecyclerItemList){
            RecyclerItem data1=new RecyclerItem();
            data1.setTask(recyclerItem.getTask());
            data1.setDegree(recyclerItem.getDegree());
            data1.setExp(recyclerItem.getExp());
            data1.setSaveTime(recyclerItem.getSaveTime());
            data1.setTime(timeTranslator(recyclerItem.getSaveTime(),nowTime));
            nowRecyclerItemList.add(data1);
        }
    }

    public String timeTranslator(long savetime,long nowtime){
        long shicha=nowtime-savetime;
        String time1="刚刚";
        int min=(int) shicha/(60*1000);
        String time2=min+"分钟前";
        int hour=(int) shicha/(3600*1000);
        String time3=hour+"小时前";
        int day=(int)shicha/(3600*24*1000);
        String time4=day+"天前";
        if (shicha<60*1000){
            return time1;
        }else if (shicha<60*60*1000){
            return time2;
        }else if (shicha<3600*24*1000){
            return time3;
        }else{
            return time4;
        }
    }

    public String titleTranslator(int exp){
        if (exp<=3000){
            return "萌级小菜鸟";
        }else if (exp<=10000){
            return "小菜鸟";
        }else if (exp<=25000) {
            return "菜鸟";
        }else if (exp<=50000){
            return "鸟";
        }else if (exp<=100000){
            return "资深鸟";
        }else if (exp<=200000){
            return "头领鸟";
        }else if (exp<=350000){
            return "鹰";
        }else if (exp<=600000){
            return "巨鹰";
        }else if (exp<=1000000){
            return "神鹰";
        }else {
            return "传说中的神鹰";
        }
    }

    public void refresh(){
        saveRecyclerItemList.clear();
        nowRecyclerItemList.clear();
        saveRecyclerItemList=DataSupport.findAll(RecyclerItem.class);
        new Date();
        long nowTime=System.currentTimeMillis();
        for (RecyclerItem recyclerItem:saveRecyclerItemList){
            RecyclerItem data1=new RecyclerItem();
            data1.setTask(recyclerItem.getTask());
            data1.setDegree(recyclerItem.getDegree());
            data1.setExp(recyclerItem.getExp());
            data1.setSaveTime(recyclerItem.getSaveTime());
            data1.setTime(timeTranslator(recyclerItem.getSaveTime(),nowTime));
            nowRecyclerItemList.add(data1);
        }
        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MyAdapter(MainActivity.this,nowRecyclerItemList);
    }

    @Override
    protected void onRestart() {
        refresh();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        MenuItem item=menu.findItem(R.id.ab_search);
        searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                adapter.setFilter(nowRecyclerItemList);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<RecyclerItem> filteredRecyclerList=filter(nowRecyclerItemList,newText);
        adapter.setFilter(filteredRecyclerList);
        return false;
    }

    private List<RecyclerItem> filter(List<RecyclerItem> recyclerItems,String query){
        query=query.toLowerCase();
        final List<RecyclerItem> filteredRecyclerList=new ArrayList<>();
        for (RecyclerItem recyclerItem:recyclerItems){
            String task=recyclerItem.getTask();
            if (task.contains(query)){
                filteredRecyclerList.add(recyclerItem);
            }
        }
        return filteredRecyclerList;
    }
}