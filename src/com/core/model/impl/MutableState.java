package com.core.model.impl;

import com.core.model.TimeRange;
import com.core.model.api.ObservableState;
import com.core.model.api.StateObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Mutable State extends the properties of a Fixed State where its range is able to be mutated adding a more dynamic capability.
 * The responsibility to best emulate the time dynamics is given to the user, by assigning the to State condition the proper value at the right moment
 * depending by user current time and by the State interval parameters it describes.
 * <p></p>
 * This implementation allows to change at any moment the State range, except for situation in which the state has reached the "extinct" condition.
 *<p></p>
 * A variation of its condition value and its range can be handled as event by subscribing one or more observers to it.
 */
public class MutableState extends FixedState implements ObservableState {

    private Long id;

    private List<StateObserver> stateObserverListeners;

    protected MutableState(){
    }



    public MutableState(TimeRange range, boolean isUndefined) {
        super(range, isUndefined);
        stateObserverListeners = new CopyOnWriteArrayList<>();
    }


    /**
     * Sets the state range with the given side parameters (start ; end).
     * @param start The start parameter value to set.
     * @param end The end parameter value to set.
     * @return true if the state range has been changed successfully ; false if the state is "extinct" ; or any other reason.
     */
    public boolean setTime(long start,long end){
        if(isExtinct()){
            return false;
        }
        stateRange = new TimeRange(start,end);
        notifyRangeChange();
        return true;
    }



    @Override
    public boolean setStateValue(boolean value) {
        boolean operation = super.setStateValue(value);
        if(operation){
            notifyStateChange(value);
        }
        return operation;
    }



    @Override
    public void addStateObserver(StateObserver listener) {
        stateObserverListeners.add(listener);
    }



    @Override
    public void removeStateObserver(StateObserver listener) {
        stateObserverListeners.remove(listener);
    }



    /**
     * Sends a notification event when the state condition value is changed.
     * @param value The new boolean value taken by the state condition.
     */
    private void notifyStateChange(boolean value){
        Iterator<StateObserver> iterator =  stateObserverListeners.iterator();
        while (iterator.hasNext()){
            iterator.next().onStateChanged(value);
        }
    }


    /**
     * Sends a notification event when the state range is changed.
     */
    private void notifyRangeChange(){
        Iterator<StateObserver> iterator =  stateObserverListeners.iterator();
           while (iterator.hasNext()){
               iterator.next().onRangeChanged(stateRange,this);
           }
    }
}
