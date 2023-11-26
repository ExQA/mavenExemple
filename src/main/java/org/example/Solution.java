package org.example;

public class Solution {
    public int numJewelsInStones(String jewels, String stones) {
        int count = 0;
        for (char c : stones.toCharArray()) {
            if (jewels.indexOf(c) != -1) count++;
        }
        return count;
    }

    public int numIdenticalPairs(int[] nums) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    count++;
                }
            }
        }
        return count;
    }

    public int maxNumberOfBalloons(String text) {
        int b = 0, a = 0, l = 0, o = 0, n = 0;

        for (char ch : text.toCharArray()) {
            switch (ch) {
                case 'b' -> ++b;
                case 'a' -> ++a;
                case 'l' -> ++l;
                case 'o' -> ++o;
                case 'n' -> ++n;
            }
        }
        return Math.min(Math.min(o / 2, l / 2), Math.min(Math.min(b, a), n));
    }
}
