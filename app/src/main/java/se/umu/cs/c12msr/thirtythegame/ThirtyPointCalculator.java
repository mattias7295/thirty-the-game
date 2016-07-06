package se.umu.cs.c12msr.thirtythegame;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Default implementation of a point calculator.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public class ThirtyPointCalculator implements PointsCalculator {

    private static final String TAG = "ThirtyPointCalculator";

    @Override
    public int calculate(int[] diceValues, String choice) {
        int res = 0;

        if (choice.equals("Low")) {
            for (int i = 0; i < diceValues.length; i++) {
                res += (diceValues[i] <= 3) ? diceValues[i] : 0;
            }
        } else {
            int sum = Integer.parseInt(choice);


            ArrayList<Integer> numbers = toArray(diceValues);
            ArrayList<ArrayList<Integer>> allCombinations =
                    findAllComb(numbers, sum);

            Collections.sort(allCombinations, new Comparator<ArrayList<Integer>>() {
                @Override
                public int compare(ArrayList<Integer> lhs, ArrayList<Integer> rhs) {
                    return lhs.size() > rhs.size() ? 1 : -1;
                }
            });
            Log.i(TAG, allCombinations.toString());
            res = maxCombPossible(numbers, allCombinations) * sum;
        }


        return res;
    }

    /**
     * Counts how many combinations can be used
     * without running out of numbers, as a number can
     * only be used one time. Note: if for example <tt>numbers</tt>
     * contains [1,1] and a combination contains [1], one the first
     * occurrence of 1 will be removed.
     * @param numbers           list of available numbers
     * @param allCombinations   list of combinations
     * @return                  maximum number of combinations possible
     */
    private int maxCombPossible(ArrayList<Integer> numbers, ArrayList<ArrayList<Integer>> allCombinations) {
        int nComb = 0;
        for (ArrayList<Integer> set : allCombinations) {
            Log.i(TAG, set.toString());
            if (allMatch(numbers, set)) {

                nComb++;
                for (Integer val :
                        set) {
                    numbers.remove(val);
                }
            }
        }
        return nComb;
    }

    private ArrayList<Integer> toArray(int[] arr) {
        ArrayList<Integer> res = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            res.add(arr[i]);
        }
        return res;
    }

    /**
     * Does a match between two list to check
     * if the frequency of each number in the <tt>wantedValues</tt>
     * is less than the frequency of the number in <tt>base</tt>.
     * @param base          the base list
     * @param wantedValues  the list of wanted values
     * @return              true if all the frequency of each number in
     *                      <tt>wantedValues</tt> is less than the
     *                      frequency of the number in <tt>base</tt>
     */
    private boolean allMatch(ArrayList<Integer> base, ArrayList<Integer> wantedValues) {
        for (Integer val :
                wantedValues) {
            int freqBase = Collections.frequency(base, val);
            int freqWanted = Collections.frequency(wantedValues, val);
            if (freqBase < freqWanted) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recursive function to find all combination
     * @param numbers           the list of numbers
     * @param target            the target value
     * @param partial           one combination of the numbers
     * @param allCombinations   list of all combination with the sum equal to <tt>target</tt>
     */
    private void findAllCombRecursive(ArrayList<Integer> numbers, int target,
                                      ArrayList<Integer> partial, ArrayList<ArrayList<Integer>> allCombinations) {
        int s = 0;
        for (int x: partial) {
            s += x;
        }
        if (s == target) {
            allCombinations.add(partial);
        }
        if (s >= target) {
            return;
        }
        for(int i = 0; i < numbers.size(); i++) {
            ArrayList<Integer> remaining = new ArrayList<Integer>();
            int n = numbers.get(i);
            for (int j = i+1; j < numbers.size(); j++) {
                remaining.add(numbers.get(j));
            }
            ArrayList<Integer> partialRec = new ArrayList<Integer>(partial);
            partialRec.add(n);
            findAllCombRecursive(remaining,target,partialRec, allCombinations);
        }
    }

    /**
     * Looks for all combination of one or more numbers
     * which the sum is equal to <tt>target</tt>.
     * @param numbers   the list of numbers
     * @param target    the target value
     * @return the list of all combinations with the sum equal to <tt>target</tt>
     */
    private ArrayList<ArrayList<Integer>> findAllComb(ArrayList<Integer> numbers, int target) {
        ArrayList<ArrayList<Integer>> allCombinations = new ArrayList<>();
        findAllCombRecursive(numbers, target, new ArrayList<Integer>(), allCombinations);
        return allCombinations;
    }
}
