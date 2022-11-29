package com.wriesnig;

import com.wriesnig.stackoverflowapi.SOAPI;
import com.wriesnig.stackoverflowapi.SOUser;

import java.util.ArrayList;
import java.util.Arrays;

public class CharacterizerApplication {
    private ArrayList<Integer> ids;

    public CharacterizerApplication(){
        ids = new ArrayList<>(Arrays.asList(22656, 2333));
    }

    public void run(){
        //was muss gemacht werden?
        //wenn nicht schon so_ids dann diese
        //SO api fetch für image url etc
        //github fetch und match
        getSOUserData(ids);
    }

    private void getSOUserData(ArrayList<Integer> ids){
        SOAPI SOAPI = new SOAPI();
        ArrayList<SOUser> so_users = SOAPI.getSOUsers(ids);
        System.out.println(so_users);
    }
}
