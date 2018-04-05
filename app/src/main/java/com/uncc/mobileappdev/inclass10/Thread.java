package com.uncc.mobileappdev.inclass10;

import java.io.Serializable;

/**
 * Created by StephenWeber on 4/4/2018.
 */

public class Thread implements Serializable{

//    "user_fname":"john","user_lname":"rambo","user_id":"884","id":"1340","title":"none john 1","created_at":"2018-04-05 02:05:01"

    String user_fname, user_lname, user_id, id, title, created_at;

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
}
