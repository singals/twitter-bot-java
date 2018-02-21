package com.github.singals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import twitter4j.*;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;

public class TwitterServiceTest {

    private TwitterService twitterService;

    private Twitter twitter = mock(Twitter.class);
    private String username = "test";
    private Status status = mock(Status.class);
    private QueryResult queryResult = mock(QueryResult.class);
    private User user = mock(User.class);

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

    @Test
    public void shouldFindTweetByText() throws Exception {
        final String textToSearch = "some random text";
        Query query = new Query(textToSearch);
        String tweetText =  "tweet with " + textToSearch;

        when(twitter.search(eq(query))).thenReturn(queryResult);
        when(queryResult.getTweets()).thenReturn(asList(status));
        when(status.getText()).thenReturn(tweetText);
        when(status.getUser()).thenReturn(user);
        when(user.getScreenName()).thenReturn(username);


        final List<Tweet> tweetsByText = twitterService.findTweetsByText(textToSearch);


        assertThat(tweetsByText.size(), is(1));
        final Tweet actualTweet = tweetsByText.get(0);
        assertThat(actualTweet.getTweetText(), is(tweetText));
        assertThat(actualTweet.getUser(), is(username));
    }

    @Test
    public void shouldHandleWhenUnableToFindTweetByText() throws Exception {
        final String textToSearch = "some random text";
        ArgumentCaptor<Query> argumentCaptor = ArgumentCaptor.forClass(Query.class);
        doThrow(new TwitterException("test expt"))
                .when(twitter).search(argumentCaptor.capture());

        final List<Tweet> tweetsByText = twitterService.findTweetsByText(textToSearch);

        assertTrue(tweetsByText.isEmpty());
        assertThat(argumentCaptor.getValue().getQuery(), is(textToSearch));
    }

    @Test
    @Ignore
    public void shouldGetTweetsFromTimeline() throws Exception {
        //TODO impl this
    }

    @Test
    public void shouldHandleWhenUnableToGetTweetFromTimeline() throws Exception {
        doThrow(new TwitterException("test expt"))
                .when(twitter).getHomeTimeline();

        final List<Tweet> tweetsFromTimeLine = twitterService.getTweetsFromTimeLine();

        assertTrue(tweetsFromTimeLine.isEmpty());
        verify(twitter).getHomeTimeline();
    }
}