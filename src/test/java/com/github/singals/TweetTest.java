package com.github.singals;

import org.junit.Test;
import twitter4j.Status;
import twitter4j.User;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class TweetTest {

    private Tweet tweet;


    private Status status = mock(Status.class);
    private User user = mock(User.class);

    @Test
    public void shouldMapFromStatus() throws Exception {
        final String tweetText = "text of tweet";
        when(status.getText()).thenReturn(tweetText);
        when(status.getUser()).thenReturn(user);

    }

}