package com.github.singals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TwitterService {

    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    private Twitter twitter = TwitterFactory.getSingleton();

    public void postTweet(String tweetText){
        try {
            final Status status = twitter.updateStatus(tweetText);
            logger.info("Tweeted successfully {} with status {}.", tweetText, status.getText());
        } catch (TwitterException e) {
            logger.error("Unable to postTweet", e);
        }
    }

    public List<Tweet> findTweetsByText(String textToSearch){
        try {
            final QueryResult queryResult = twitter.search(new Query(textToSearch));
            return queryResult.getTweets().stream()
                    .map(Tweet::mapFromStatus)
                    .collect(Collectors.toList());

        } catch (TwitterException e) {
            logger.error("Unable to search tweets by text", e);
            return emptyList();
        }
    }

    public List<Tweet> getTweetsFromTimeLine(){
        try {
            final List<Status> homeTimeline = twitter.getHomeTimeline();
            return homeTimeline.stream().map(Tweet::mapFromStatus).collect(Collectors.toList());
        } catch (TwitterException e) {
            logger.error("Unable to get home time line", e);
            return emptyList();
        }
    }

    public void replyToTweet(String textToSearch, String tweetText){
        try {
            final List<Tweet> tweets = findTweetsByText(textToSearch);
            if (tweets.isEmpty()) {
                logger.warn("No tweet found with text {}", textToSearch);
            } else {
                final Tweet tweet = tweets.get(0);
                StatusUpdate statusUpdate = new StatusUpdate("@" + tweet.getUser() + " " + tweetText);
                statusUpdate.inReplyToStatusId(tweet.getId());
                twitter.updateStatus(statusUpdate);
            }
        } catch (TwitterException e) {
            logger.error("Unable to reply to tweets", e);
        }
    }

}
