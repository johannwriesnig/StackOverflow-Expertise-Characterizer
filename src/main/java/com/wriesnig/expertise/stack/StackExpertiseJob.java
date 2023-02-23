package com.wriesnig.expertise.stack;

import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.utils.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StackExpertiseJob implements Runnable {
    private final User user;

    public StackExpertiseJob(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        ResultSet postResults = StackDatabase.getPostsFromUser(user.getStackId());
        try {

            HashMap<String, ArrayList<Double>> scoresPerTag = new HashMap<>();
            Object[] postToClassify;
            String userIsEstablished = String.valueOf(user.getIsEstablishedOnStack());
            for (String tag : Tags.tagsToCharacterize) {
                scoresPerTag.put(tag, new ArrayList<>());
            }
            while (postResults.next()) {
                String tagsOfCurrentPost = postResults.getString("tags");
                if (tagsOfCurrentPost == null || !postTagsContainTagsToCharacterize(tagsOfCurrentPost)) continue;
                int postId = postResults.getInt("id");
                ResultSet votesOfCurrentPost = StackDatabase.getVotesOfPost(postId);

                double upVotes = 0;
                double downVotes = 0;
                String isAccepted = "0";

                if (votesOfCurrentPost.next()) {
                    upVotes = votesOfCurrentPost.getInt("upVotes");
                    downVotes = votesOfCurrentPost.getInt("downVotes");
                    isAccepted = votesOfCurrentPost.getString("isAccepted");

                }
                if(upVotes==0&&downVotes==0)continue;


                String isMainTag;

                for (String tag : Tags.tagsToCharacterize) {
                    if (tagsOfCurrentPost.contains("<" + tag + ">")){

                        if (user.getMainTags().contains(tag))
                            isMainTag = "1";
                        else isMainTag = "0";
                        boolean isActiveOnTag = (isMainTag.equals("1")) && userIsEstablished.equals("1");
                        double score = upVotes / (upVotes + downVotes);
                        double s = (double)((int)(score*100))/100.0;
                        postToClassify = new Object[]{upVotes, downVotes, s, isAccepted, isActiveOnTag?"1":"0"};
                        double expertise = StackClassifier.classify(postToClassify)+1;
                        scoresPerTag.get(tag).add(expertise);
                    }
                }
            }

            scoresPerTag.forEach((key, value) -> {
                double score = value.size()!=0?value.stream().mapToDouble(Double::doubleValue).sum() / value.size():1;
                score = (double)((int)(score*100))/100.0;
                user.getExpertise().getStackExpertise().put(key, score);
            });
        } catch (SQLException e) {
            Logger.error("");
        }

        Logger.info("Stack expertise for " + user.getStackDisplayName() + ": " + user.getExpertise().getStackExpertise().toString()+".");
    }

    private boolean postTagsContainTagsToCharacterize(String inputStr) {
        return Arrays.stream(Tags.tagsToCharacterize).map(s -> "<" + s + ">").anyMatch(inputStr::contains);
    }
}
