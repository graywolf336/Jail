package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailVoteManager;
import com.graywolf336.jail.beans.JailVote;
import com.graywolf336.jail.enums.JailVoteResult;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailVote {
    private UUID[] ids = new UUID[] { UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() };
    private static TestInstanceCreator creator;
    private static JailMain main;
    
    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The JailMain class is null.", main);
        assertTrue("The adding of a jail failed.", creator.addJail());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }
    
    @Test
    public void verifyJailVote() {
        assertNotNull("The JailVoteManager is null.", main.getJailVoteManager());
        assertNotNull("The Jail Votes collection is null.", main.getJailVoteManager().getVotes());
        assertNotNull("The task ids list is null.", main.getJailVoteManager().getRunningTasks());
        assertEquals("There are items in the task list.", 0, main.getJailVoteManager().getRunningTasks().size());
        assertNull("There is a jail vote for graywolf336.", main.getJailVoteManager().getVoteForPlayer("graywolf336"));
        assertFalse("Someone has been voted for to be jailed?", main.getJailVoteManager().isVotedFor("graywolf336"));
        assertEquals("The broadcast duration isn't correct.", "1m0s", main.getJailVoteManager().getTimerLengthDescription());
        assertEquals("The default minimum amount of yes votes is not 5.", 5, main.getJailVoteManager().getMinimumYesVotes());
    }
    
    @Test
    public void testYesVote() {
        JailVoteManager jvm = main.getJailVoteManager();
        String name = "yesVote";
        UUID id = UUID.randomUUID();
        creator.addMockPlayer(name, id);
        
        //adding a new vote
        assertTrue("There is already a vote for yesVote.", jvm.addVote(new JailVote(name)));
        assertNotNull("The JailVote for yesVote is null.", jvm.getVoteForPlayer(name));
        assertTrue("There is no vote to jail yesVote.", jvm.isVotedFor(name));
        
        //0-6 yes
        for(int i = 0; i < 6; i++) {
            jvm.addVote(name, ids[i], true);
            assertTrue(ids[i].toString() + " has already voted for yesVote.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't six yes votes to jail yesVote.", 6, jvm.getVoteForPlayer(name).getYesVotes());
        
        for(int i = 6; i < ids.length; i++) {
            jvm.addVote(name, ids[i], false);
            assertTrue(ids[i].toString() + " has already voted for yesVote.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't four no votes to jail yesVote.", 4, jvm.getVoteForPlayer(name).getNoVotes());
        
        assertEquals("The vote result was not successful.", JailVoteResult.YES, jvm.doTheVoteCalculation(jvm.getVoteForPlayer(name)));
        assertNull("The vote for yesVote is still active", jvm.getVoteForPlayer(name));
        assertFalse("The vote for yesVote is still active.", jvm.isVotedFor(name));
        assertTrue("yesVote is not jailed.", main.getJailManager().isPlayerJailedByLastKnownUsername(name));
        assertEquals("The jail sentence from the jail vote against yesVote isn't the default.", 300000, main.getJailManager().getPrisoner(id).getRemainingTime());
        assertEquals("The reason from jail vote isn't the default.", "Jailed by players via Jail Vote", main.getJailManager().getPrisoner(id).getReason());
    }
    
    @Test
    public void testNoVote() {
        JailVoteManager jvm = main.getJailVoteManager();
        String name = "noVote";
        UUID id = UUID.randomUUID();
        creator.addMockPlayer(name, id);
        
        //adding a new vote
        assertTrue("There is already a vote for noVote.", jvm.addVote(new JailVote(name)));
        assertNotNull("The JailVote for noVote is null.", jvm.getVoteForPlayer(name));
        assertTrue("There is no vote to jail noVote.", jvm.isVotedFor(name));
        
        //0-6 no
        for(int i = 0; i < 6; i++) {
            jvm.addVote(name, ids[i], false);
            assertTrue(ids[i].toString() + " has already voted for noVote.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't six no votes to jail noVote.", 6, jvm.getVoteForPlayer(name).getNoVotes());
        
        for(int i = 6; i < ids.length; i++) {
            jvm.addVote(name, ids[i], true);
            assertTrue(ids[i].toString() + " has already voted for noVote.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't four yes votes to jail noVote.", 4, jvm.getVoteForPlayer(name).getYesVotes());
        
        assertEquals("The vote result was not NO.", JailVoteResult.NO, jvm.doTheVoteCalculation(jvm.getVoteForPlayer(name)));
        assertNull("The vote for noVote is still active", jvm.getVoteForPlayer(name));
        assertFalse("The vote for noVote is still active.", jvm.isVotedFor(name));
        assertFalse("noVote is jailed.", main.getJailManager().isPlayerJailed(id));
    }
    
    @Test
    public void testTiedVote() {
        JailVoteManager jvm = main.getJailVoteManager();
        String name = "tiedVote";
        UUID id = UUID.randomUUID();
        creator.addMockPlayer(name, id);
        
        //adding a new vote
        assertTrue("There is already a vote for tiedVote.", jvm.addVote(new JailVote(name)));
        assertNotNull("The JailVote for tiedVote is null.", jvm.getVoteForPlayer(name));
        assertTrue("There is no vote to jail tiedVote.", jvm.isVotedFor(name));
        
        //0-6 no
        for(int i = 0; i < 5; i++) {
            jvm.addVote(name, ids[i], false);
            assertTrue(ids[i].toString() + " has already voted for tiedVote.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't five no votes to jail tiedVote.", 5, jvm.getVoteForPlayer(name).getNoVotes());
        
        for(int i = 5; i < ids.length; i++) {
            jvm.addVote(name, ids[i], true);
            assertTrue(ids[i].toString() + " has already voted for tiedVote.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't five yes votes to jail tiedVote.", 5, jvm.getVoteForPlayer(name).getYesVotes());
        
        assertEquals("The vote result was not TIED.", JailVoteResult.TIED, jvm.doTheVoteCalculation(jvm.getVoteForPlayer(name)));
        assertNull("The vote for tiedVote is still active", jvm.getVoteForPlayer(name));
        assertFalse("The vote for tiedVote is still active.", jvm.isVotedFor(name));
        assertFalse("tiedVote is jailed.", main.getJailManager().isPlayerJailed(id));
    }
    
    @Test
    public void testTiedButNotOnlineVote() {
        JailVoteManager jvm = main.getJailVoteManager();
        String name = "notOnline";
        
        //adding a new vote
        assertTrue("There is already a vote for notOnline.", jvm.addVote(new JailVote(name)));
        assertNotNull("The JailVote for notOnline is null.", jvm.getVoteForPlayer(name));
        assertTrue("There is no vote to jail notOnline.", jvm.isVotedFor(name));
        
        //0-6 no
        for(int i = 0; i < 5; i++) {
            jvm.addVote(name, ids[i], false);
            assertTrue(ids[i].toString() + " has already voted for notOnline.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't five no votes to jail notOnline.", 5, jvm.getVoteForPlayer(name).getNoVotes());
        
        for(int i = 5; i < ids.length; i++) {
            jvm.addVote(name, ids[i], true);
            assertTrue(ids[i].toString() + " has already voted for notOnline.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't five yes votes to jail notOnline.", 5, jvm.getVoteForPlayer(name).getYesVotes());
        
        assertEquals("The notOnline player was treated as online, thus the result is tied.", JailVoteResult.NOTONLINE, jvm.doTheVoteCalculation(jvm.getVoteForPlayer(name)));
        assertNull("The vote for tiedVote is still active", jvm.getVoteForPlayer(name));
        assertFalse("The vote for tiedVote is still active.", jvm.isVotedFor(name));
        assertFalse("tiedVote is jailed.", main.getJailManager().isPlayerJailedByLastKnownUsername(name));
    }
    
    @Test
    public void testNotEnoughYesVotesVote() {
        JailVoteManager jvm = main.getJailVoteManager();
        String name = "notEnoughYes";
        UUID id = UUID.randomUUID();
        creator.addMockPlayer(name, id);
        
        assertTrue("There is already a vote for notEnoughYes.", jvm.addVote(new JailVote(name)));
        assertNotNull("The JailVote for notEnoughYes is null.", jvm.getVoteForPlayer(name));
        assertTrue("There is no vote to jail notEnoughYes.", jvm.isVotedFor(name));

        for(int i = 0; i < 4; i++) {
            jvm.addVote(name, ids[i], true);
            assertTrue(ids[i].toString() + " has already voted for notEnoughYes.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't four yes votes to jail notEnoughYes.", 4, jvm.getVoteForPlayer(name).getYesVotes());
        
        for(int i = 8; i < ids.length; i++) {
            jvm.addVote(name, ids[i], false);
            assertTrue(ids[i].toString() + " has already voted for notEnoughYes.", jvm.hasVotedAlready(name, ids[i]));
        }
        assertEquals("There aren't two no votes to jail notEnoughYes.", 2, jvm.getVoteForPlayer(name).getNoVotes());
        
        assertEquals("The vote result was not Not Enough Yes Votes.", JailVoteResult.NOTENOUGHYESVOTES, jvm.doTheVoteCalculation(jvm.getVoteForPlayer(name)));
        assertNull("The vote for notEnoughYes is still active", jvm.getVoteForPlayer(name));
        assertFalse("The vote for notEnoughYes is still active.", jvm.isVotedFor(name));
        assertFalse("notEnoughYes is jailed.", main.getJailManager().isPlayerJailed(id));
    }
}
