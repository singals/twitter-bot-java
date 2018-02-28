package com.github.singals;

import org.junit.Test;
import twitter4j.Status;
import twitter4j.User;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class TweetTest {

    private Status status = mock(Status.class);
    private User user = mock(User.class);

    @Test
    public void shouldMapFromStatus() throws Exception {
        final String tweetText = "text of tweet";
        final long userId = 1L;
        final String userName = "test";
        when(status.getText()).thenReturn(tweetText);
        when(status.getUser()).thenReturn(user);
        when(user.getScreenName()).thenReturn(userName);
        when(status.getId()).thenReturn(userId);

        final Tweet tweet = Tweet.mapFromStatus(status);

        assertThat(tweet.getId(), is(userId));
        assertThat(tweet.getUser(), is(userName));
        assertThat(tweet.getTweetText(), is(tweetText));
    }

}