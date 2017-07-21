package twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T11:00:00Z");
    
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "for tweet3", d3);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanMoreThanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected start", d3, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("epected end", d1, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanZeroTweet() {
        Timespan timespan = Extract.getTimespan(new ArrayList<Tweet>());
        assertEquals(timespan.getStart(), timespan.getEnd());
    }
    
    
    

    private static final Tweet tweet4 = new Tweet(4, "user1", "user1 text mention @user2", d3);
    private static final Tweet tweet5 = new Tweet(5, "user1", "user1 text mention two times @user2 @user2", d3);
    private static final Tweet tweet6 = new Tweet(6, "user1", "user1 text mention two distinct times @user1 @user2", d3);
    private static final Tweet tweet7 = new Tweet(7, "bitdiddle", "bitdiddle@mit.edu", d3);
    private static final Tweet tweet8 = new Tweet(8, "user1", "hihi @bitdiddle!", d3);

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersMentionOne() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertEquals(new HashSet<String> (Arrays.asList("user2")), mentionedUsers);
    }
    
    @Test 
    public void testGetMentionedUsersMentionDuplicate() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        assertEquals(new HashSet<String> (Arrays.asList("user2")), mentionedUsers);
    }
    
    @Test 
    public void testGetMentionedUsersMentionTwoAtEnd() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));
        System.out.println(mentionedUsers);
        assertEquals(new HashSet<String> (Arrays.asList("user1", "user2")), mentionedUsers);
    }
    
    @Test 
    public void testGetMentionedUsersMentionFollowedByValid() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet7));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test 
    public void testGetMentionedUsersMentionPreceededByPunctuation() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet8));
        assertEquals(new HashSet<String>(Arrays.asList("bitdiddle")), mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersMentionMutipleTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6, tweet8));
        assertEquals(new HashSet<String>(Arrays.asList("bitdiddle", "user1", "user2")), mentionedUsers);
    }
    
    private static final Tweet tweet9 = new Tweet(9, "user1", "bitdiddle@@mit.edu", d3);
    @Test
    public void testGetMentionedUsersMentionSurroundBySpecialCharacter() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet9));
        assertEquals(new HashSet<String> (Arrays.asList("mit")), mentionedUsers);
    }

    private static final Tweet tweet10 = new Tweet(10, "user1", "!@Jessy-young-yang!", d3);
    @Test
    public void testGetMentionUsersMentionHasMinus() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet10));
        assertEquals(new HashSet<String> (Arrays.asList("Jessy-young-yang")), mentionedUsers);
    }
    
    private static final Tweet tweet11 = new Tweet(11, "bitdiddle", "bitdiddle6@mit.edu", d3);
    @Test
    public void testGetMentionusersMentionMailNotValid() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet11));
        assertEquals(new HashSet<String> (Arrays.asList("mit")), mentionedUsers);
    }
    
    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
