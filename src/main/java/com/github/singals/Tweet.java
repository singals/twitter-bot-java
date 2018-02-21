package com.github.singals;

import lombok.*;
import twitter4j.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tweet {
    private String user;
    private String tweetText;

    public static Tweet mapFromStatus(Status status){
        return new Tweet(status.getUser().getScreenName(), status.getText());
    }
}
