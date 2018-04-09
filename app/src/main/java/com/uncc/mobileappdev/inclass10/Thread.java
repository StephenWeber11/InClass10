package com.uncc.mobileappdev.inclass10;

import java.io.Serializable;

/**
 * Created by StephenWeber on 4/4/2018.
 */

public class Thread implements Serializable{

//    "user_fname":"john","user_lname":"rambo","user_id":"884","id":"1340","title":"none john 1","created_at":"2018-04-05 02:05:01"

    String user_fname, user_lname, user_id, id, title, created_at;
    boolean userAdded;

    public String getUser_fname() {
        return user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public boolean isUserAdded(){
        return userAdded;
    }

    public void setUserAdded(boolean userAdded){
        this.userAdded = userAdded;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
