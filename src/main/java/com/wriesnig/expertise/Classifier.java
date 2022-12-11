package com.wriesnig.expertise;

public class Classifier {
    private Classifier(){

    }

    public static int classify(int upVotes, int downVotes, int isAccepted, int bodyLength){
        int expertise;
        if(upVotes<=60){
            if(upVotes>7){
                if(isAccepted==0){
                    if(upVotes>49){
                        expertise = 3;
                    } else expertise = 2;
                } else expertise = 3;
            } else expertise = 1;
        } else{
            if(upVotes<=284){
                if(isAccepted==1){
                    if(upVotes<=131) expertise=4;
                    else expertise=5;
                } else {
                    expertise=3;
                }
            } else {
                expertise=5;
            }
        }
        return expertise;
    }
}
