package com.core.tasks;

import com.core.model.impl.adjustable.dependent.constraint.impl.NoEndConstraint;
import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.api.State;
import com.core.model.api.StateObject;
import com.core.model.TimeRange;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.dependent.states.DependencyState;
import com.core.model.impl.adjustable.dependent.states.impl.RangeState;
import com.core.model.impl.adjustable.dependent.constraint.impl.ExactSideConstraint;
import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;
import com.core.model.impl.adjustable.adjuster.impl.DynamicAdjuster;
import com.core.model.impl.adjustable.adjuster.impl.StaticAdjuster;
import com.core.util.States;


public class ActivityTask implements StateObject {

    protected DependencyState progressState;

    protected ActivityTask(){
    }


    protected ActivityTask(long start, long end,RangeAdjuster adjuster){
        progressState = RangeState.getDefinedRange(start,end,adjuster);
    }


   public static ActivityTask getStaticActivity(long start,long end){
       return new ActivityTask(start,end,new StaticAdjuster());
    }



    public static ActivityTask getDynamicActivity(long start,long end){
       return new ActivityTask(start,end,new DynamicAdjuster());
    }



    public static ActivityTask getActivity(long start,long end,RangeAdjuster adjuster){
        return new ActivityTask(start,end,adjuster);
    }



    public static ParentActivityTask getParentActivityTask(){
        return new ParentActivityTask();
    }


    /**
     *
     * @return
     */
    public TimeRange getProgressRange(){
        return progressState.getRange();
    }


    /**
     *
     * @return
     */
    public long getDuration(){
        TimeRange range = getProgressRange();
        return range.getEnd() - range.getStart();
    }

    public boolean setTime(long start,long end){
        return progressState.setTime(start,end);
    }

    public boolean setStartTime(long start, boolean move) {
        if(move){
            return setTime(start,start + getDuration());
        }else {
            return setTime(start,progressState.getRange().getEnd());
        }
    }


    /**
     *
     * @param end
     * @param move
     * @return
     */
    public boolean setEndTime(long end,boolean move){
        if(move){
            return setTime(end - getDuration(),end);
        }else {
            return setTime(progressState.getRange().getStart(), end);
        }
    }


    /**
     *
     * @param activityTask
     * @return
     */
    public boolean removeDependency(ActivityTask activityTask) throws StateException {
            return (progressState.removeStartTimeDependency(activityTask.progressState) &
            progressState.removeEndTimeDependency(activityTask.progressState));
    }


    /**
     * Creates a "Start to Finish" (StoF) relationship with the given State.
     * @param activityTask
     * @return
     */
    public boolean progressIfCompleted(ActivityTask activityTask) throws StateException {
        if(progressState.getStartTimeConstraint() == null){
            return States.setConstraintAndDepend(progressState,new NoEndConstraint(),Side.START,activityTask.progressState);
        }else{
            return States.setConstraintAndDepend(progressState,null,Side.START,activityTask.progressState);
        }
    }

    /**
     *Creates a "Start to Start" (SS) relationship with the given State.
     * @param activityTask
     * @return
     */
    public boolean completedIfCompleted(ActivityTask activityTask) throws StateException {
        if(progressState.getEndTimeConstraint() == null) {
            return States.setConstraintAndDepend(progressState, new ExactSideConstraint(Side.END,true), Side.END, activityTask.progressState);
        }else {
           return States.setConstraintAndDepend(progressState, null, Side.END, activityTask.progressState);
        }
    }


    /**
     *Creates a "Finish to Finish" (FF) relationship with the given State.
     * @param
     * @return
     */
    public boolean progressIfProgress(ActivityTask activityTask) throws StateException {
        if(progressState.getStartTimeConstraint() == null){
            return States.setConstraintAndDepend(progressState,new ExactSideConstraint(Side.START,true),Side.START,activityTask.progressState);
        }else {
            return States.setConstraintAndDepend(progressState,null,Side.START,activityTask.progressState);
        }
    }


    @Override
    public String toString() {
        return "ActivityTask{" +
                " progressState=" + progressState.getRange().toString() +
                '}';
    }



    @Override
    public State[] getStateSet() {
        State[] states = new State[1];
        states[0] = progressState;
        return states;
    }

    @Override
    public boolean isStateObjectExtinct() {
        return (isInProgress() || isCompleted());
    }

    @Override
    public void destroy(){
        progressState.destroy();
    }

    /**
     * Test if this Activity Task is in progress state.
     * @return true if it is in progress; false if is completed, or not already in progress.
     */
    public boolean isInProgress(){
        return progressState.getStateValue();
    }

    public boolean isCompleted(){
        return progressState.isExtinct();
    }



}
