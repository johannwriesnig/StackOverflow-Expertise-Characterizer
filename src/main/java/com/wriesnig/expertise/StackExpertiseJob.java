package com.wriesnig.expertise;

import com.wriesnig.stackoverflow.db.StackDbConnection;
import com.wriesnig.stackoverflow.db.StackDatabase;
import com.wriesnig.stackoverflow.db.VoteTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StackExpertiseJob implements Runnable{
    private User user;

    public StackExpertiseJob(User user){
        this.user = user;

    }
    @Override
    public void run() {
        StackDbConnection stackDbConnection = StackDatabase.getConnectionPool().getDBConnection();
        ResultSet postResults = StackDatabase.getPostsFromUser(stackDbConnection, user.getSoId());
        try {

            HashMap<String, ArrayList<Double>> scoresPerTag = new HashMap<>();
            for(String tag: Tags.tagsToCharacterize){
                scoresPerTag.put(tag, new ArrayList<>());
            }

            while (postResults.next()) {
                int parentId = postResults.getInt("parentId");
                String tagsOfCurrentPost = parentId==0?postResults.getString("tags"): StackDatabase.getTagsFromParentPost(stackDbConnection, parentId);
                if (tagsOfCurrentPost == null || !postTagsContainTagsToCharacterize(tagsOfCurrentPost)) continue;
                String postBody = postResults.getString("Body");
                int postId = postResults.getInt("id");
                ResultSet votesOfCurrentPost = StackDatabase.getVotesOfPost(stackDbConnection, postId);
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
                System.out.println(user.getSoDisplayName() + " has " + String.format("%.4f",score) + " for " + key);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StackDatabase.getConnectionPool().releaseDBConnection(stackDbConnection);
    }

    private boolean postTagsContainTagsToCharacterize(String inputStr) {
        return Arrays.stream(Tags.tagsToCharacterize).map(s -> "<" + s + ">").anyMatch(inputStr::contains);
    }
}
