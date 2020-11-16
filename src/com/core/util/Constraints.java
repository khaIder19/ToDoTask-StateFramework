package com.core.util;

import com.core.model.TimeRange;
import com.core.model.api.MutableState;
import com.core.model.api.State;
import com.core.model.impl.adjustable.dependent.ReversibleState;
import com.core.model.impl.adjustable.dependent.constraint.ConstraintException;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.Arrays;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Constraints {

    public static boolean applyAll(State[] states){
        Boolean[] stateValues = new Boolean[states.length];
        for(int i = 0; i<stateValues.length;i++){
            stateValues[i] = states[i].getStateValue();
        }
        Iterable<Boolean> iterable = Arrays.asList(stateValues);
        return Iterables.all(iterable, Predicates.equalTo(TRUE));
    }

    public static boolean applyAllNoEnd(State[] states){
        Boolean[] stateValues = new Boolean[states.length];
        for(int i = 0; i<stateValues.length;i++){
            if(states[i] instanceof ReversibleState) {
                if (((ReversibleState) states[i]).getRight() != null) {
                    stateValues[i] = ((MutableState) states[i]).isExtinct();
                } else {
                    stateValues[i] = states[i].getStateValue();
                }
            }
        }
        Iterable<Boolean> iterable = Arrays.asList(stateValues);
        return Iterables.all(iterable, Predicates.equalTo(TRUE));
    }

    public static boolean applyAllNonMatch(State[] states){
        Boolean[] stateValues = new Boolean[states.length];
        for(int i = 0; i<stateValues.length;i++){
            stateValues[i] = states[i].getStateValue();
        }
        Iterable<Boolean> iterable = Arrays.asList(stateValues);
        return Iterables.all(iterable, Predicates.equalTo(FALSE));
    }


    public static boolean anyMatch(long n, TimeRange[] ranges){
        for(int i = 0; i < ranges.length;i++){
            if(n <= ranges[i].getEnd() & n >= ranges[i].getStart()){
                return true;
            }
        }
        return false;
    }

    public static boolean noneMatch(long n,TimeRange[] ranges){
        return !anyMatch(n,ranges);
    }
}
