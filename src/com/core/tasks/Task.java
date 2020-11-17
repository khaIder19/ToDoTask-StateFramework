package com.core.tasks;

import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.api.State;
import com.core.model.api.StateObject;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;
import com.core.model.impl.adjustable.dependent.states.DependencyState;
import com.core.model.impl.adjustable.dependent.states.impl.NoEndState;
import com.core.model.impl.adjustable.dependent.ReversibleState;
import com.core.model.impl.adjustable.dependent.constraint.impl.NoEndConstraint;

public class Task implements StateObject {

    private DependencyState completedState;

    protected Task(){
    }

    public Task(long start){
        completedState = NoEndState.getStartTimeRange(start,true);
    }

    public boolean setCompleted(){
        return completedState.setStateValue(true);
    }



    private boolean completedIf(State task) throws StateException {
        if(completedState.setStartTimeConstraint(new NoEndConstraint())) {
            return completedState.addStartTimeDependency(task);
        }else {
            return false;
        }
    }


    /**
     * Creates a "Completed if Completed" (CtoC) relationship with the given StateObject.
     * @param stateObject
     * @return
     * @throws StateException
     */
    public boolean completedIfCompleted(StateObject stateObject) throws StateException {
        State s = stateObject.getStateSet()[0];
            if(s instanceof ReversibleState){
                return completedIf(s);
            }else{
                throw new NotSupportedDependency("Dependency given must be Reversible");
            }
    }



    public boolean getStatus(){
        return completedState.getStateValue();
    }


    public boolean removeDependency(StateObject obj) throws StateException {
        return completedState.removeStartTimeDependency(obj.getStateSet()[0]);
    }


    @Override
    public State[] getStateSet() {
        State[] states = new State[1];
        states[0] = completedState;
        return states;
    }

    /**
     * A Task is "extinct" when it has been "completed".
     * @return
     */
    @Override
    public boolean isStateObjectExtinct() {
        return completedState.isExtinct();
    }


    @Override
    public void destroy(){
        completedState.destroy();
    }


    @Override
    public String toString() {
        return "Task{" +
                "completedState=" + completedState.getStateValue() + ","+
                "range="+ completedState.getRange() +
                '}';
    }


    public long getStartRange(){
        return completedState.getRange().getStart();
    }

}
