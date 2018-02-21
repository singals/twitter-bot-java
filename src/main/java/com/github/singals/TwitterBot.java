package com.github.singals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class TwitterBot {

    private static final Logger logger = LoggerFactory.getLogger(TwitterBot.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting app");

        TwitterService twitterService = new TwitterService();

        //find tweets by text
//        final List<Tweet> tweetsByText = twitterService.findTweetsByText("midst");
//        tweetsByText.forEach(t -> logger.info(t.toString()));

        //post a new tweet
//        final String tweetText = "this is a dummy tweet!";
//        twitterService.postTweet(tweetText);
//        logger.info("Posted a new tweet -> ", tweetText);

        //get tweets from timeline
//        final List<Tweet> myTweets = twitterService.getTweetsFromTimeLine();
//        myTweets.forEach(t -> logger.info(t.toString()));


        logger.info("Exiting app");
    }

}
