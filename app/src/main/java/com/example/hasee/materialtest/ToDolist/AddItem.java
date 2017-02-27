package com.example.hasee.materialtest.ToDolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hasee.materialtest.R;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by hasee on 2017/1/29.
 */

public class AddItem extends AppCompatActivity{
    private EditText editTask;
    private Spinner spinnerDegree;
    private EditText editExp;
    private String reg="^[1-9]\\d*$";
    private String everTask;
    private int everDegree;
    private String everExp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        Toolbar toolbar=(Toolbar)findViewById(R.id.add_item_menu);
        setSupportActionBar(toolbar);
        everTask=getIntent().getStringExtra("task");
        everDegree=getIntent().getIntExtra("degree",0);
        everExp=getIntent().getStringExtra("exp");
        editTask=(EditText)findViewById(R.id.add_task);
        spinnerDegree=(Spinner) findViewById(R.id.add_degree);
        editExp=(EditText)findViewById(R.id.add_exp);
        editTask.setText(everTask);
        spinnerDegree.setSelection(everDegree-1);
        editExp.setText(everExp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.additem_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.finish:
                editTask=(EditText)findViewById(R.id.add_task);
                spinnerDegree=(Spinner) findViewById(R.id.add_degree);
                editExp=(EditText)findViewById(R.id.add_exp);
                String task;
                int degree;
                String exp;
                task=editTask.getText().toString();
                degree=Integer.parseInt(spinnerDegree.getSelectedItem().toString());
                exp=editExp.getText().toString();
                if (exp.equals(everExp)&&task.equals(everTask)&&degree==everDegree){
                    AddItem.this.finish();
                }else if (exp.matches(reg)&&task.length()>=3){
                    DataSupport.deleteAll(RecyclerItem.class,"task=?",everTask);
                    RecyclerItem recyclerItem=new RecyclerItem();
                    recyclerItem.setExp(exp);
                    recyclerItem.setDegree(degree);
                    recyclerItem.setTask(task);
                    new Date();
                    long time=System.currentTimeMillis();
                    recyclerItem.setSaveTime(time);
                    recyclerItem.save();
                    AddItem.this.finish();
                }else {
                    Toast.makeText(AddItem.this,"输入格式有误",Toast.LENGTH_SHORT).show();
                }

                default:
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        editTask=(EditText)findViewById(R.id.add_task);
        spinnerDegree=(Spinner) findViewById(R.id.add_degree);
        editExp=(EditText)findViewById(R.id.add_exp);
        final String task;
        final int degree;
        final String exp;
        task=editTask.getText().toString();
        degree=Integer.parseInt(spinnerDegree.getSelectedItem().toString());
        exp=editExp.getText().toString();
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            if (exp.equals(everExp)&&task.equals(everTask)&&degree==everDegree){
                AddItem.this.finish();
            }else if (!task.equals("")&&!exp.equals("")){
                DataSupport.deleteAll(RecyclerItem.class,"task=?",everTask);
                AlertDialog.Builder builder=new AlertDialog.Builder(AddItem.this);
                builder.setTitle("是否保存编辑");
                builder.setCancelable(false);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (exp.matches(reg)&&!task.equals("")){
                            RecyclerItem recyclerItem=new RecyclerItem();
                            new Date();
                            long time=System.currentTimeMillis();
                            recyclerItem.setSaveTime(time);
                            recyclerItem.setExp(exp);
                            recyclerItem.setDegree(degree);
                            recyclerItem.setTask(task);
                            recyclerItem.save();
                            AddItem.this.finish();
                        }else {
                            Toast.makeText(AddItem.this,"输入格式有误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddItem.this.finish();
                    }
                });
                builder.show();
            }else {
                AddItem.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
