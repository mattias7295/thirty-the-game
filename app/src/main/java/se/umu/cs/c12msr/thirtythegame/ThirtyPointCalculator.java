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
            res = maxPairs(numbers, allCombinations) * sum;
        }


        return res;
    }

    private int maxPairs(ArrayList<Integer> numbers, ArrayList<ArrayList<Integer>> allCombinations) {
        int pairs = 0;
        for (ArrayList<Integer> set : allCombinations) {
            Log.i(TAG, set.toString());
            if (allMatch(numbers, set)) {

                pairs++;
                for (Integer val :
                        set) {
                    numbers.remove(val);
                }
            }
        }
        return pairs;
    }


    private ArrayList<Integer> toArray(int[] arr) {
        ArrayList<Integer> res = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            res.add(arr[i]);
        }
        return res;
    }

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

    private void findAllCombRecursive(ArrayList<Integer> numbers, int target,
                                      ArrayList<Integer> partial, ArrayList<ArrayList<Integer>> allCombinations) {
        int s = 0;
        for (int x: partial) s += x;
        if (s == target) {
            allCombinations.add(partial);
        }
        if (s >= target)
            return;
        for(int i=0;i<numbers.size();i++) {
            ArrayList<Integer> remaining = new ArrayList<Integer>();
            int n = numbers.get(i);
            for (int j=i+1; j<numbers.size();j++) remaining.add(numbers.get(j));
            ArrayList<Integer> partial_rec = new ArrayList<Integer>(partial);
            partial_rec.add(n);
            findAllCombRecursive(remaining,target,partial_rec, allCombinations);
        }
    }

    private ArrayList<ArrayList<Integer>> findAllComb(ArrayList<Integer> numbers, int target) {
        ArrayList<ArrayList<Integer>> allCombinations = new ArrayList<>();
        findAllCombRecursive(numbers, target, new ArrayList<Integer>(), allCombinations);
        return allCombinations;
    }
}
