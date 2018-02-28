package com.github.singals;

import lombok.*;
import twitter4j.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tweet {
    private Long id;
    private String user;
    private String tweetText;

    public static Tweet mapFromStatus(Status status){
        return new Tweet(status.getId(), status.getUser().getScreenName(), status.getText());
    }
}
