package com.android2025.tips;

import java.util.ArrayList;

public class DemoClass {

    public DemoClass() {
        int[] numbers = {1,1,2,3,4,5};
        ArrayList<Integer> list = new ArrayList<>();
        for (int number : numbers) {
            if (!list.contains(number)) {
                list.add(number);
            }
        }

        System.out.println(list);
    }
}
