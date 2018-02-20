package com.github.singals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

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


}
