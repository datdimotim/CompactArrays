package com.dimotim.compact_arrays;

import java.io.Serializable;

public interface IntegerArray extends Serializable{
    void set(int position, long value);
    long get(int position);
}
