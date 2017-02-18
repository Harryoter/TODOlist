package com.example.hasee.materialtest.ToDolist;

import org.litepal.crud.DataSupport;

/**
 * Created by hasee on 2017/2/13.
 */

public class RecyclerBinItem extends DataSupport{
    private String task;
    private int degree;
    private String exp;


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

}
