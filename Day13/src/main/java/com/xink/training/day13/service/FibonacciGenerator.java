package com.xink.training.day13.service;

import java.util.ArrayList;
import java.util.List;

public class FibonacciGenerator {
    
    public List<Integer> generate(int count) {
        List<Integer> sequence = new ArrayList<>();
        if (count <= 0) {
            return sequence;
        }
        
        if (count >= 1) {
            sequence.add(1);
        }
        if (count >= 2) {
            sequence.add(1);
        }
        
        for (int i = 2; i < count; i++) {
            sequence.add(sequence.get(i - 1) + sequence.get(i - 2));
        }
        
        return sequence;
    }
}

