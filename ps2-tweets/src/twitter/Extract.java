package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
//        throw new RuntimeException("not implemented");
        Instant start = Instant.MAX;
        Instant end = Instant.MIN;
        if (tweets.size() == 0) {
            return new Timespan(start, start);
        }
        
        for (Tweet tweet: tweets) {
            if (tweet.getTimestamp().isBefore(start)) {
                start = tweet.getTimestamp();
            }
            if (tweet.getTimestamp().isAfter(end)) {
                end = tweet.getTimestamp();
            }
        }
        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> usernames = new HashSet<>();
        Set<String> authors = new HashSet<>();
        for (Tweet tweet: tweets) {
            authors.add(tweet.getAuthor());
        }
        for (Tweet tweet: tweets) {
            String text = tweet.getText();
//            System.out.println(text); 
            Pattern pattern = Pattern.compile("(\\w+(-*))*@(\\w+(-*))+");
            Matcher matcher = pattern.matcher(text);
            while(matcher.find()) {
                String[] pair = matcher.group().split("@");
//                System.out.println(pair.length);
//                for (String m: pair) {
//                    System.out.println("it is " +m);
//                }
//                if (pair.length == 1) {
//                    System.out.println("l=1, add word "+ pair[0]);
//                    usernames.add(pair[0]);
//                }
                if (pair.length == 2){
//                    System.out.println("first"+ pair[0]);
                    if (!authors.contains(pair[0]) || pair[0].equals("")) {
//                        System.out.println("not contains"+ pair[0]);
                        usernames.add(pair[1]);
                    }
                }
//                else { // more then one @   @@tu
//                    usernames.add(pair[pair.length - 1]);
//                }
            }
        }
        return usernames;
//        throw new RuntimeException("not implemented");
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
