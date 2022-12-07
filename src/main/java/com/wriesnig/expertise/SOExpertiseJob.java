package com.wriesnig.expertise;

import com.wriesnig.stackoverflow.db.DBConnection;
import com.wriesnig.stackoverflow.db.SODatabase;
import com.wriesnig.stackoverflow.db.VoteTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SOExpertiseJob implements Runnable{
    private User user;

    public SOExpertiseJob(User user){
        this.user = user;

    }
    @Override
    public void run() {
        DBConnection dbConnection = SODatabase.getConnectionPool().getDBConnection();
        ResultSet postResults = SODatabase.getPostsFromUser(dbConnection, user.getSo_id());
        try {

            HashMap<String, ArrayList<Double>> scoresPerTag = new HashMap<>();
            for(String tag: Tags.tagsToCharacterize){
                scoresPerTag.put(tag, new ArrayList<>());
            }

            while (postResults.next()) {
                int parentId = postResults.getInt("parentId");
                String tagsOfCurrentPost = parentId==0?postResults.getString("tags"):SODatabase.getTagsFromParentPost(dbConnection, parentId);
                if (tagsOfCurrentPost == null || !postTagsContainTagsToCharacterize(tagsOfCurrentPost)) continue;
                String postBody = postResults.getString("Body");
                int postId = postResults.getInt("id");
                ResultSet votesOfCurrentPost = SODatabase.getVotesOfPost(dbConnection, postId);
                int upVotes = 0;
                int downVotes = 0;
                int isAccepted = 0;

                while (votesOfCurrentPost.next()) {
                    int voteType = votesOfCurrentPost.getInt("VoteTypeId");
                    if (voteType == VoteTypes.IS_ACCEPTED.getValue()) isAccepted = 1;
                    if (voteType == VoteTypes.UP_VOTE.getValue()) upVotes++;
                    if (voteType == VoteTypes.DOWN_VOTE.getValue()) downVotes++;
                }
                double upKof = 0.0041;
                double downKof = -0.019;
                double isKof = 0.2388;
                double lenKof = 0.0023;

                double score = upKof*upVotes + downKof*downVotes + isAccepted*isKof + lenKof*postBody.length();
                for(String tag: Tags.tagsToCharacterize){
                    if(tagsOfCurrentPost.contains("<"+tag+">"))scoresPerTag.get(tag).add(score);
                }
            }

            scoresPerTag.forEach((key,value)-> {
                double score = value.stream().mapToDouble(Double::doubleValue).sum() / value.size();
                System.out.println(user.getSo_display_name() + " has " + String.format("%.4f",score) + " for " + key);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SODatabase.getConnectionPool().releaseDBConnection(dbConnection);
    }

    private boolean postTagsContainTagsToCharacterize(String inputStr) {
        return Arrays.stream(Tags.tagsToCharacterize).map(s -> "<" + s + ">").anyMatch(inputStr::contains);
    }
}
