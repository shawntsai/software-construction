package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     * test empty, one follow empty, one follow many, many follow many
     * many follow none
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "@a @b @c", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes", d1);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@d for tweet3", d1);
    private static final Tweet tweet4 = new Tweet(4, "jessy", "rivest talk in 30 minutes", d1);

    @Test
    public void testGuessFollowsGraphOneToMany() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        Map<String, Set<String>> ourFollowGraph = new HashMap<>();
        ourFollowGraph.put("alyssa", new HashSet<String>(Arrays.asList("a", "b", "c")));
        assertEquals("one follow three guys", ourFollowGraph, followsGraph);
    }
     
    @Test
    public void testGuessFollowsGraphOneToEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphManyToMany() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet3));
        Map<String, Set<String>> ourFollowGraph = new HashMap<>();
        ourFollowGraph.put("alyssa", new HashSet<String>(Arrays.asList("a", "b", "c")));
        ourFollowGraph.put("bbitdiddle", new HashSet<String>(Arrays.asList("d")));
        assertEquals("one follow three guys", ourFollowGraph, followsGraph);
    }
    
    @Test
    public void testGuessFollowsGraphManyToEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2, tweet4));
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    
    /**
     * test no influencers 
     * test one influencer
     * test many influencers
     */
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersOne() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("jessy",new HashSet<>(Arrays.asList("namie-amuro")));
        followsGraph.put("shawn",new HashSet<>(Arrays.asList("namie-amuro")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected only one followee", Arrays.asList("namie-amuro"), influencers);
    }
    @Test 
    public void testInfluencersMany() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("jessy",new HashSet<>(Arrays.asList("namie-amuro")));
        followsGraph.put("jolin",new HashSet<>(Arrays.asList("namie-amuro", "yanzi")));
        followsGraph.put("shawn",new HashSet<>(Arrays.asList("namie-amuro", "jolin", "yanzi")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected only one followee", Arrays.asList("namie-amuro", "yanzi", "jolin"), influencers);
    }
    

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
