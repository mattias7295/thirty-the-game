package se.umu.cs.c12msr.thirtythegame;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mattias Scherer on 2016-06-10.
 */
public class ThirtyPointCalculatorTest {

    private ThirtyPointCalculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new ThirtyPointCalculator();
    }

    @Test
    public void testCalculate() throws Exception {
        int diceValues[] = new int[] {3,1,4,1,3,1};

        String choice = "Low";

        assertEquals(9, calculator.calculate(diceValues, choice));

        choice = "4";
        assertEquals(12, calculator.calculate(diceValues, choice));

        choice = "5";
        assertEquals(10, calculator.calculate(diceValues, choice));

        choice = "6";
        assertEquals(12, calculator.calculate(diceValues, choice));
    }

    @Test
    public void testCalculate2() throws Exception {
        int diceValues[] = new int[] {6,5,4,1,6,2};

        String choice = "7";
        assertEquals(14, calculator.calculate(diceValues, choice));

    }
}