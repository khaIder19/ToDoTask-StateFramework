package com.core.util;

import com.core.model.TimeRange;
import com.core.model.api.State;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.impl.adjustable.dependent.states.DependencyState;
import java.util.*;

/**
 * This class contains utility method useful when working with State(s) objects.
 */
public class States {


    public static State[] tempDependencySet(State [] old, State[] last){
        State[] joinedSet = null;
        if(old.length == 0){
            joinedSet = new State[last.length];
            for(int i = 0 ; i < last.length;i++){
                joinedSet[i] = last[i];
            }
        }else {
            int size = old.length + last.length;
            joinedSet = new State[size];
            for(int i = 0 ; i < old.length;i++){
                joinedSet[i] = old[i];

            }
            for(int i = old.length,j = 0; i < size;i++,j++){
                joinedSet[i] = last[j];
            }
        }
        return joinedSet;
    }

    /**
     *
     * @param old
     * @param last
     * @return
     */
    public static State[] joinSets(State [] old,State[] last){
        State[] joinedSet = null;
        int size = old.length + last.length;
        joinedSet = new State[size];
        for(int i = 0 ; i < old.length ; i++){
            joinedSet[i] = old[i];
        }
        for(int i = old.length,j = 0; i < size;i++,j++){
            joinedSet[i] = last[j];
        }
        return joinedSet;
    }


    /**
     * Retrive the maximum end-time of a give set of State(s).
     * @param states State set to evaluate.
     * @return  max end-time.
     */
    public static long maxEndTime(State[] states){
        List l = Arrays.asList(states);
        return Collections.max(l,(State x,State y)-> Long.compare(x.getRange().getEnd(),y.getRange().getEnd())).getRange().getEnd();
    }


    /**
     *  Retrive the maximum start-time for a give set of State(s).
     * @param states State set to evaluate.
     * @return maximum start-time; if the set contains only one State, returns just its start-time.
     */
    public static long maxStartTime(State[] states){
        List l = Arrays.asList(states);
        return Collections.max(l,(State x,State y)-> Long.compare(x.getRange().getStart(),y.getRange().getStart())).getRange().getStart();
    }


    /**
     *
     * @param states
     * @return
     */
    public static long minStartTime(State[] states){
        List l = Arrays.asList(states);
        return Collections.min(l,(State x,State y)-> Long.compare(x.getRange().getStart(),y.getRange().getStart())).getRange().getStart();
    }

    /**
     * Retrive the State with the minimum start-time of a given set of State(s).
     * @param states
     * @return
     */
    public static State minStartTimeState(State[] states){
        long minKey = minStartTime(states);
        for(int i = 0;i<states.length;i++){
            if(states[i].getRange().getStart() == minKey){
                return states[i];
            }
        }
        return null;
    }


    /**
     * Retrive the State with the minimum start-time of a given set of State(s).
     * @param states
     * @return
     */
    public static State maxEndTimeState(State[] states){
        long minKey = maxEndTime(states);
        for(int i = 0;i<states.length;i++){
            if(states[i].getRange().getEnd() == minKey){
                return states[i];
            }
        }
        return null;
    }


    /**
     *
     * @param states
     * @param toSubtract
     * @return
     */
    public static State[] subtractFromSet(State[] states,State toSubtract){
        State[] toReturn = null;
        LinkedList<State> set = new LinkedList<>(Arrays.asList(states));
        if(set.remove(toSubtract)){
            toReturn = new State[states.length - 1];
            for(int i = 0; i < toReturn.length; i++){
                toReturn[i] = set.get(i);
            }
        }else {
            toReturn = states;
        }
        return toReturn;
    }





    public static boolean setConstraintAndDepend(DependencyState state, Constraint constrait, Side side, State dependency) throws StateException {
        boolean result = false;
        switch (side){
            case START:
                if(constrait != null) {
                    if (state.setStartTimeConstraint(constrait)) {
                        result = state.addStartTimeDependency(dependency);
                    }
                }else {
                    result = state.addStartTimeDependency(dependency);
                }
                break;
            case END:
                if(constrait != null) {
                    if (state.setEndTimeConstraint(constrait)) {
                        result = state.addEndTimeDependency(dependency);
                    }
                }else {
                    result =state.addEndTimeDependency(dependency);
                }
                break;
        }
        return result;
    }

    public static TimeRange[] greaterThanRanges(long n, TimeRange[] ranges,boolean absoluteTest){
        ArrayList<TimeRange> validRanges = new ArrayList<>();
        for(TimeRange r : ranges){
            if(absoluteTest){
                if(r.getStart() >= n){
                    validRanges.add(r);
                }
            }else{
                if(r.getEnd() >= n){
                    validRanges.add(r);
                }
            }
        }
        return (TimeRange[]) validRanges.toArray();
    }

    public static TimeRange[] lowerThanRanges(long n, TimeRange[] ranges,boolean absoluteTest){
        ArrayList<TimeRange> validRanges = new ArrayList<>();
        for(TimeRange r : ranges){
            if(absoluteTest){
                if(r.getEnd() <= n){
                    validRanges.add(r);
                }
            }else{
                if(r.getStart() <= n){
                    validRanges.add(r);
                }
            }
        }
        return (TimeRange[]) validRanges.toArray();
    }
}
