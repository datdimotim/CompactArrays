package com.dimotim.compact_arrays;

public class CompactIntegerArrayDivide implements IntegerArray {
    public final int length;
    public final int MAX_VALUE;
    private final long[] data;
    private final int numbers_per_entry;
    private final long[] powers;
    public CompactIntegerArrayDivide(final int length, final int max){
        if(length<0)throw new RuntimeException("length<0 length="+length);
        if(max<2)throw new RuntimeException("max<2 max="+max);

        MAX_VALUE=max;
        this.length=length;

        long t=-1; // unsigned max /0x11111111/
        int count=-1;
        while (t!=0){
            count++;
            t=Long.divideUnsigned(t,max);
        }
        numbers_per_entry=count;

        powers=new long[count+1];
        long pow=1;
        for(int i=0;i<count+1;i++){
            powers[i]=pow;
            pow*=max;
        }

        int dataLength= length % numbers_per_entry==0 ? length/numbers_per_entry : length/numbers_per_entry + 1;
        data=new long[dataLength];
    }
    public void set(int position, long value){
        checkBounds(position);
        checkValue(value);
        int index=position/numbers_per_entry;
        int offset=position%numbers_per_entry;

        long low=Long.remainderUnsigned(data[index],powers[offset]);

        long high;
        if(offset+2<powers.length-1) high=data[index]-Long.remainderUnsigned(data[index],powers[offset+2]);
        else high=0;

        long middle=value*powers[offset];
        data[index]=low+middle+high;
    }
    public long get(int position){
        checkBounds(position);
        int index=position/numbers_per_entry;
        int offset=position%numbers_per_entry;
        return (int) Long.remainderUnsigned(Long.divideUnsigned(data[index],powers[offset]),MAX_VALUE);
    }

    private void checkBounds(int position){
        if(position<0)throw new IllegalArgumentException("position<0 position="+position);
        if(position>=length)throw new IllegalArgumentException("position>=length position="+position+"  length="+length);
    }

    private void checkValue(long value){
        if(value<0)throw new IllegalArgumentException("value<0 value="+value);
        if(value>MAX_VALUE)throw new IllegalArgumentException("value>MAX_VALUE value="+value+"  MAX_VALUE="+MAX_VALUE);
    }
}