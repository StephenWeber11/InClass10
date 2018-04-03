package com.uncc.mobileappdev.inclass10;

/**
 * Created by Stephen on 4/2/2018.
 */

public class TokenResponse {
    String status, message, token, user_id, user_email, user_fname, user_lname, user_role;

    public String getToken(){
        return token;
    }

    public String getStatus(){
        return status;
    }
}
