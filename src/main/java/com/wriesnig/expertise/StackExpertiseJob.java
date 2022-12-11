package com.wriesnig.expertise;

import com.wriesnig.stackoverflow.db.StackDbConnection;
import com.wriesnig.stackoverflow.db.StackDatabase;
import com.wriesnig.stackoverflow.db.VoteTypes;
import com.wriesnig.utils.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StackExpertiseJob implements Runnable{
    private final User user;

    public StackExpertiseJob(User user){
        this.user = user;

    }
    @Override
    public void run() {
        StackDbConnection stackDbConnection = StackDatabase.getConnectionPool().getDBConnection();
        ResultSet postResults = StackDatabase.getPostsFromUser(stackDbConnection, user.getStackId());
        try {

            HashMap<String, ArrayList<Double>> scoresPerTag = new HashMap<>();
            for(String tag: Tags.tagsToCharacterize){
                scoresPerTag.put(tag, new ArrayList<>());
            }

            while (postResults.next()) {
                String tagsOfCurrentPost = postResults.getString("tags");
                if (tagsOfCurrentPost == null || !postTagsContainTagsToCharacterize(tagsOfCurrentPost)) continue;
                int postBodyLength = postResults.getInt("bodyLength");
                int postId = postResults.getInt("id");
                ResultSet votesOfCurrentPost = StackDatabase.getVotesOfPost(stackDbConnection, postId);

                int upVotes=0;
                int downVotes=0;
                int isAccepted=0;

                if(votesOfCurrentPost.next()){
                    upVotes = votesOfCurrentPost.getInt("upVotes");
                    downVotes =votesOfCurrentPost.getInt("downVotes");;
                    isAccepted = votesOfCurrentPost.getInt("isAccepted");;
                }


                double expertise = Classifier.classify(upVotes, downVotes, isAccepted, postBodyLength);
                //if(tagsOfCurrentPost.contains("c#"))Logger.info("Post c# "+ postId +" -> " + upVotes + "/"+ downVotes + "/" + isAccepted + "/" + postBodyLength + " ... Expertise: " + expertise);
                for(String tag: Tags.tagsToCharacterize){
                    if(tagsOfCurrentPost.contains("<"+tag+">"))scoresPerTag.get(tag).add(expertise);
                }
            }

            scoresPerTag.forEach((key,value)-> {
                double score = value.stream().mapToDouble(Double::doubleValue).sum() / value.size();
                user.getExpertise().getStackExpertise().put(key, score);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StackDatabase.getConnectionPool().releaseDBConnection(stackDbConnection);
        Logger.info("Computed following expertise for " + user.getStackDisplayName() + " : " + user.getExpertise().getStackExpertise().toString());
    }

    private boolean postTagsContainTagsToCharacterize(String inputStr) {
        return Arrays.stream(Tags.tagsToCharacterize).map(s -> "<" + s + ">").anyMatch(inputStr::contains);
    }
}
