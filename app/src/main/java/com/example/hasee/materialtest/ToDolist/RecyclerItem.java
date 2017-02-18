package com.example.hasee.materialtest.ToDolist;

import org.litepal.crud.DataSupport;

/**
 * Created by hasee on 2017/1/29.
 */

public class RecyclerItem extends DataSupport{
    private String task;
    private int degree;
    private String exp;
    private String time;
    private long saveTime;

    public String getTask(){
        return task;
    }
    public void setTask(String task){
        this.task=task;
    }
    public int getDegree(){
        return degree;
    }
    public void setDegree(int degree){
        this.degree=degree;
    }
    public String getExp(){
        return exp;
    }
    public void setExp(String exp){
        this.exp=exp;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
    public long getSaveTime(){
        return saveTime;
    }
    public void setSaveTime(long saveTime){
        this.saveTime=saveTime;
    }

}
