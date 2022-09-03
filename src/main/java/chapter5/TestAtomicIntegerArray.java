package chapter5;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class TestAtomicIntegerArray {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3};
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(arr);
        System.out.println(atomicIntegerArray.length());
    }
}
