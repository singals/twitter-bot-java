package com.github.singals;

import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.mockito.Mockito.*;

public class TwitterServiceTest {

    private Twitter twitter = mock(Twitter.class);
    private Status status = mock(Status.class);
    private TwitterService twitterService;

    @Before
    public void setUp() throws Exception {
        twitterService = new TwitterService(twitter);
    }

    @Test
    public void shouldBeAbleToPostTweet() throws Exception {
        final String tweetText = "some random postTweet!";
        when(status.getText()).thenReturn("success");
        when(twitter.updateStatus(tweetText)).thenReturn(status);

        twitterService.postTweet(tweetText);

        verify(twitter).updateStatus(tweetText);
        verify(status).getText();
    }

    @Test
    public void shouldHandleWhenUnableToTweet() throws Exception {
        final String tweetText = "some random postTweet!";
        doThrow(new TwitterException("test expt"))
            .when(twitter).updateStatus(tweetText);

        twitterService.postTweet(tweetText);

        verify(twitter).updateStatus(tweetText);
        verify(status, never()).getText();
    }
}