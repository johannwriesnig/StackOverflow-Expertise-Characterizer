package com.wriesnig.expertise.stack;

import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.db.stack.StackDbConnection;
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
        StackDbConnection stackDbConnection = StackDatabase.getConnectionPool().getDBConnection();
        ResultSet postResults = StackDatabase.getPostsFromUser(stackDbConnection, user.getStackId());
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
                ResultSet votesOfCurrentPost = StackDatabase.getVotesOfPost(stackDbConnection, postId);

                double upVotes = 0;
                double downVotes = 0;
                String isAccepted = "0";

                if (votesOfCurrentPost.next()) {
                    upVotes = votesOfCurrentPost.getInt("upVotes");
                    downVotes = votesOfCurrentPost.getInt("downVotes");
                    isAccepted = votesOfCurrentPost.getString("isAccepted");

                }

                double score = (upVotes + downVotes) == 0 ? 0 : upVotes / (upVotes + downVotes);

                String isMainTag = "0";
                for (String tag : user.getMainTags())
                    if (tagsOfCurrentPost.contains("<" + tag + ">")) {
                        isMainTag = "1";
                    }

                postToClassify = new Object[]{upVotes, downVotes, score, isAccepted, userIsEstablished, isMainTag};


                double expertise = StackClassifier.classify(postToClassify);
                if (expertise == 0) continue;

                for (String tag : Tags.tagsToCharacterize) {
                    if (tagsOfCurrentPost.contains("<" + tag + ">"))
                        scoresPerTag.get(tag).add(expertise);
                }
            }

            scoresPerTag.forEach((key, value) -> {
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
