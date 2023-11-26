package org.example;

import org.junit.Assert;
import org.junit.Test;

public class SolutionTest {
    @Test
    public void numJewelsInStonesTest() {
        Solution solution = new Solution();

        String jewels = "aA";
        String stones = "aAAbbbb";
        int expect = 3;
        int actual = solution.numJewelsInStones(jewels, stones);

        Assert.assertEquals(expect, actual);
    }

    @Test
    public void numIdenticalPairsTest() {
        Solution solution = new Solution();

        int nums[] = {1, 2, 3, 1, 1, 3};
        int expect = 4;
        int actual = solution.numIdenticalPairs(nums);

        Assert.assertEquals(expect, actual);
    }

    @Test
    public void maxNumberOfBalloonsTest() {
        Solution solution = new Solution();

        String text = "nlaebolko";
        int expect = 1;
        int actual = solution.maxNumberOfBalloons(text);

        Assert.assertEquals(expect, actual);
    }
}