package com.core.model.impl;

import com.core.model.api.MutableState;
import com.core.model.api.State;
import com.core.model.TimeRange;
import java.io.Serializable;

/**
 * A Fixed State represents a static state where its sides (start ; end) once specified will remain unchanged,
 * however its condition value can be mutated in order to respond properly to temporal or arbitrary events.
 *<p></p>
 * Is also possible to represent a state range undefinition (infinite) :
 * <p></p>
 * An undefined start side will assume the long max value (is recommended to use LONG.MIN constant).
 *<p></p>
 * An undifined end side will assume the long min value (is recommended to use LONG.MAX constant).
 * <p></p>
 * The responsibility to best emulate the time dynamics is given to the user, by assigning the to State condition the proper value at the right moment
 * depending by user current time and by the State interval parameters it describes.
 * <p></p>
 */
public class FixedState implements MutableState,Comparable<State>, Serializable {

    public Long id;

    private boolean state = false;

    private boolean isUndefined;

    protected TimeRange stateRange;

    protected boolean extinct;

    protected FixedState(){
    }

    /**
     *
     * @param range
     * @param isUndefined
     */
    public FixedState(TimeRange range, boolean isUndefined){
        if(range.getEnd() < 0){
            throw new IllegalArgumentException();
        }
        this.stateRange = range;
        this.isUndefined = isUndefined;
    }


    @Override
    public boolean isUndefined() {
        return isUndefined;
    }

    /**
     *
     * @param value value to be assigned to state condition property.
     * @return true if the state value is changed successfully; false if the method is invoked trying to assign the same current state condition value or
     * the state is considered "extinct" or other different reasons.
     */
    @Override
    public boolean setStateValue(boolean value) {
        if(getStateValue() == value){
            return false;
        }
        if(value){
            state = true;
            extinct = true;
        }
        if(!value){
            state = false;
        }
        return true;
    }


    @Override
    public boolean isExtinct() {
        return extinct;
    }


    @Override
    public boolean getStateValue() {
        return state;
    }


    @Override
    public TimeRange getRange() {
        TimeRange range;
        range = new TimeRange(stateRange.getStart(),stateRange.getEnd());
        return range;
    }

    /**
     * Gets a measure of the state interval duration.
     * @return duration value.
     */
    public long getDuration(){
        long start = stateRange.getStart();
        long end = stateRange.getEnd();
        if(!(start == Long.MAX_VALUE | end == Long.MAX_VALUE)){
            return end - start;
        }
        return Long.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "FixedState{" +
                "state=" + state +
                ", stateRange=" + stateRange +
                ", extinct=" + extinct +
                ", duration=" + getDuration() +
                '}';
    }


    @Override
    public int compareTo(State o) {
        return this.getRange().compareTo(o.getRange());
    }
}
