package se.umu.cs.c12msr.thirtythegame;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Mattias Scherer on 2016-06-09.
 */
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() throws Exception {
        String[] choices = new String[] {"Low", "1"};
        player = new Player("player", new ThirtyPointCalculator(), choices);
    }

    @Test
    public void testCalculate() throws Exception {
        Integer dices[] = new Integer[] {0,1,2,3,4,5};
        player.getAllDiceValues()[0] = 4;
        player.getAllDiceValues()[1] = 4;
        player.getAllDiceValues()[2] = 4;
        player.getAllDiceValues()[3] = 1;
        player.getAllDiceValues()[4] = 1;
        player.getAllDiceValues()[5] = 1;
        player.calculatePoints("4");
        System.out.println(Arrays.toString(player.getScore()));
        assertEquals(12, player.getScore()[1]);
    }

    @Test
    public void testRollsRemaining() throws Exception {
        Integer dices[] = new Integer[] {
                0,1,2,3,4,5
        };
        for (int i = 0; i < 3; i++) {
            player.roll(dices);
        }
        assertTrue(player.rollsRemaining()==0);
    }

}