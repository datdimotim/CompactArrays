package com.dimotim.compact_arrays;

public class CompactIntegerArrayShift implements IntegerArray {
    public final long MAX_VALUE;
    public final int length;
    private final int bitsForInt;

    private final long[] data;
    private final long mask;

    public CompactIntegerArrayShift(int length, int bitsForInt) {
        if(bitsForInt<=0)throw new IllegalArgumentException("bitsForInt<=0 bitsForInt="+bitsForInt);
        if(bitsForInt>64)throw new IllegalArgumentException("bitsForInt>64 bitsForInt="+bitsForInt);
        if(length<0)throw new IllegalArgumentException("length<0 length="+length);

        this.bitsForInt=bitsForInt;

        long bitCount=bitsForInt*(long)length;
        int size= (int) (bitCount%64==0 ? bitCount/64 : bitCount/64+1);
        data=new long[size];

        this.length=length;
        long max=1;
        for(int i=0;i<bitsForInt;i++)max*=2;
        max--;
        MAX_VALUE=max;

        long mask=0x00;
        long bit=0x01;
        for(int i=0;i<bitsForInt;i++){
            mask|=bit;
            bit<<=1;
        }
        this.mask=mask;
    }
    public void set(int position, long value){
        checkBounds(position);
        checkValue(value);
        int index=position*bitsForInt/64;
        int offset=position*bitsForInt-index*64;

        if(offset+bitsForInt<=64){
            long cleanMask=~(mask<<offset);
            data[index]&=cleanMask;
            data[index]|=(value<<offset);
        }
        else {
            {
                long cleanMask = ~(mask << offset);
                data[index] &= cleanMask;
                data[index] |= (value << offset);
            }
            {
                long written = 64-offset;
                long cleanMask = ~(mask>>>written);
                data[index+1] &= cleanMask;
                data[index+1] |= (value >>> written);
            }
        }
    }
    public long get(int position){
        checkBounds(position);
        int index=position*bitsForInt/64;
        int offset=position*bitsForInt-index*64;

        if(offset+bitsForInt<=64){
            long readMask=(mask<<offset);
            long value=(data[index]&readMask);
            return value>>>offset;
        }
        else {
            long readMaskR=(mask<<offset);
            long valueR=(data[index]&readMaskR);
            valueR>>>=offset;

            long readed =64-offset;
            long readMaskL=(mask>>>readed);
            long valueL=data[index+1]&readMaskL;
            valueL<<=readed;
            return valueL|valueR;
        }
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