package com.dimotim.compact_arrays.test;


import com.dimotim.compact_arrays.CompactIntegerArrayShift;
import com.dimotim.compact_arrays.IntegerArray;
import com.dimotim.compact_arrays.CompactIntegerArrayDivide;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        benchmark();
        //stableTest();
    }

    static void stableTest(){
        IntegerArray IntegerArray =new CompactIntegerArrayShift(1000000,3);
        int[] m=new int[1000000];
        Random random=new Random();
        while (true){
            for (int i=0;i<1000000;i++){
                m[i]=random.nextInt(3);
                IntegerArray.set(i,m[i]);

            }
            int s=0;
            for(int i=0;i<1000000;i++){
                if(IntegerArray.get(i)!=m[i])throw new RuntimeException();
                s+=(int) IntegerArray.get(i);
            }
            System.out.println(s);
        }
    }

    static void benchmark(){
        final int ITER=100000000;
        final int SIZE=100000000;
        byte[] array=initArray(SIZE);
        IntegerArray integerArray =initArrayDevide(SIZE);
        Random random=new Random();

        {
            long ts = System.currentTimeMillis();
            int s = 0;
            for (int i = 0; i < ITER; i++) {
                s += array[random.nextInt(SIZE)];
            }
            System.out.println(array.getClass());
            System.out.println(s + "\ntime= " + (System.currentTimeMillis() - ts) + "\n");
        }

        {
            long ts = System.currentTimeMillis();
            int s = 0;
            for (int i = 0; i < ITER; i++) {
                s += integerArray.get(random.nextInt(SIZE));
            }
            System.out.println(integerArray.getClass());
            System.out.println(s + "\ntime= " + (System.currentTimeMillis() - ts) + "\n");
        }



    }

    static IntegerArray initArrayShift(int SIZE){
        IntegerArray integerArray =new CompactIntegerArrayShift(SIZE,3);
        Random random=new Random();
        for(int i = 0; i< SIZE; i++) integerArray.set(i,random.nextInt(8));
        return integerArray;
    }
    static IntegerArray initArrayDevide(int SIZE){
        IntegerArray integerArray =new CompactIntegerArrayDivide(SIZE,3);
        Random random=new Random();
        for(int i = 0; i< SIZE; i++) integerArray.set(i,random.nextInt(3));
        return integerArray;
    }
    static byte[] initArray(int SIZE){
        byte[] arr=new byte[SIZE];
        Random random=new Random();
        for(int i=0;i<arr.length;i++)arr[i]=(byte) random.nextInt(3);
        return arr;
    }
}
