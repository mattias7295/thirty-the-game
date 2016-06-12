package se.umu.cs.c12msr.thirtythegame;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Mattias Scherer on 2016-06-09.
 */
public class GameTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game("player", new ThirtyPointCalculator());
    }

    /*
    @Test
    public void testIsFinished() throws Exception {
        for (int i = 0; i < 30; i++) {
            game.roll();
        }
        assertTrue(game.isFinished());
    }
    */

    @Test
    public void testCalculate() throws Exception {
        Integer dices[] = new Integer[] {0,1,2,3,4,5};
        game.getAllDiceValues()[0] = 4;
        game.getAllDiceValues()[1] = 4;
        game.getAllDiceValues()[2] = 4;
        game.getAllDiceValues()[3] = 1;
        game.getAllDiceValues()[4] = 1;
        game.getAllDiceValues()[5] = 1;
        game.calculatePoints("4");
        System.out.println(Arrays.toString(game.getScore()));
        assertEquals(12, game.getScore()[1]);
    }

    @Test
    public void testRollsRemaining() throws Exception {
        Integer dices[] = new Integer[] {
                0,1,2,3,4,5
        };
        for (int i = 0; i < 3; i++) {
            game.roll(dices);
        }
        assertTrue(game.rollsRemaining()==0);
    }

}