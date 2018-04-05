package com.uncc.mobileappdev.inclass10;

import java.io.Serializable;

/**
 * Created by Stephen on 4/2/2018.
 */

public class TokenResponse implements Serializable{
    String status;
    String message;
    String token;
    String user_id;
    String user_email;
    String user_fname;
    String user_lname;
    String user_role;

    public String getToken(){
        return token;
    }

    public String getStatus(){
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_fname() {
        return user_fname;
    }
}
