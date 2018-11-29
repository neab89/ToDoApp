package com.todoapp.data;

/**
 * Created by neerajaabraham on 29/11/18.
 */

public class ToDoItem {

    private int mId;
    private String mToDo;
    private int isChecked;

    public int getChecked() {
        return isChecked;
    }

    public void setChecked(int checked) {
        isChecked = checked;
    }


    public ToDoItem() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmToDo() {
        return mToDo;
    }

    public void setmToDo(String mToDo) {
        this.mToDo = mToDo;
    }


}