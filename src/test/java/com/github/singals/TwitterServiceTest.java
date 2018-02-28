package com.github.singals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import twitter4j.*;

import java.util.Collections;
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
        Long tweetId = 1L;

        when(twitter.search(eq(query))).thenReturn(queryResult);
        when(queryResult.getTweets()).thenReturn(asList(status));
        when(status.getText()).thenReturn(tweetText);
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(tweetId);
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

    @Test
    public void shouldReplyToTweet() throws Exception {
        String textToSearch = "text to search";
        String tweetText =  "original tweet " + textToSearch;
        String replyText = "text to tweet";
        final long tweetId = 1L;
        Query query = new Query(textToSearch);

        when(twitter.search(eq(query))).thenReturn(queryResult);
        when(queryResult.getTweets()).thenReturn(asList(status));
        when(status.getText()).thenReturn(tweetText);
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(tweetId);
        when(user.getScreenName()).thenReturn(username);

        twitterService.replyToTweet(textToSearch, tweetText);

        ArgumentCaptor<StatusUpdate> argumentCaptor = ArgumentCaptor.forClass(StatusUpdate.class);
        verify(twitter).updateStatus(argumentCaptor.capture());
        final StatusUpdate actualStatusUpdate = argumentCaptor.getValue();
        assertThat(actualStatusUpdate.getInReplyToStatusId(), is(tweetId));
        assertThat(actualStatusUpdate.getStatus(), is("@test original tweet text to search"));
    }

    @Test
    public void shouldHandleWhenNoTweetExists() throws Exception {
        String textToSearch = "text to search";
        String tweetText =  "original tweet " + textToSearch;
        String replyText = "text to tweet";
        final long tweetId = 1L;
        Query query = new Query(textToSearch);

        when(twitter.search(eq(query))).thenReturn(queryResult);
        when(queryResult.getTweets()).thenReturn(Collections.emptyList());

        verify(twitter, never()).updateStatus(any(StatusUpdate.class));
    }

    @Test
    public void shouldHandleWhenUnableToReply() throws Exception {
        String textToSearch = "text to search";
        String tweetText =  "original tweet " + textToSearch;
        String replyText = "text to tweet";
        final long tweetId = 1L;
        Query query = new Query(textToSearch);

        when(twitter.search(eq(query))).thenReturn(queryResult);
        when(queryResult.getTweets()).thenReturn(asList(status));
        when(status.getText()).thenReturn(tweetText);
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(tweetId);
        when(user.getScreenName()).thenReturn(username);
        doThrow(new TwitterException("test expt"))
                .when(twitter).updateStatus(any(StatusUpdate.class));

        twitterService.replyToTweet(textToSearch, tweetText);
    }
}