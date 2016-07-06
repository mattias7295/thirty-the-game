package se.umu.cs.c12msr.thirtythegame;

/**
 * This interface that must be implemented by a class that
 * want to calculate points.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public interface PointsCalculator {

    /**
     * Calculate a value from an array of
     * numbers and any kind of choice.
     * @param diceValues    the array of numbers to calculate a value from
     * @param choice        the choice which decides the outcome of this method
     * @return              the calculated value
     */
    int calculate(int diceValues[], String choice);
}
