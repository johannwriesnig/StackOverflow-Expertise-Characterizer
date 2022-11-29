package wriesnig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CharacterizerApplication {
    private ArrayList<Integer> ids;

    public CharacterizerApplication(){
        ids = new ArrayList<>(Arrays.asList(22656));
    }

    public void run(){
        //was muss gemacht werden?
        //wenn nicht schon so_ids dann diese
        //SO api fetch für image url etc
        //github fetch und match
        getSOUserData(ids);
    }

    private void getSOUserData(ArrayList<Integer> ids){
        StackOverflowAPI stackOverflowAPI = new StackOverflowAPI();
        JSONObject user_data = stackOverflowAPI.getUserInfo(ids);

    }
}
