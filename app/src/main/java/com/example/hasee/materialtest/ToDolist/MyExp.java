package com.example.hasee.materialtest.ToDolist;

import org.litepal.crud.DataSupport;

/**
 * Created by hasee on 2017/2/9.
 */

public class MyExp extends DataSupport{
    private int myExp;
    private String title;
    public int getMyExp(){
        return myExp;
    }
    public void setMyExp(int myExp){
        this.myExp=myExp;
    }
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
}
