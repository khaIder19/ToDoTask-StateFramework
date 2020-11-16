package com.core.tasks;

import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.impl.adjustable.dependent.constraint.impl.ExactSideConstraint;
import com.core.model.impl.Side;
import com.core.model.impl.adjustable.adjuster.impl.DynamicAdjuster;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;

public class ParentActivityTask extends ActivityTask{


    protected ParentActivityTask() {
        super(0, 1, new DynamicAdjuster());
        progressState.setStartTimeConstraint(new ExactSideConstraint(Side.START,false));
        progressState.setEndTimeConstraint(new ExactSideConstraint(Side.END,false));
    }



    public boolean addSubActivityTask(ActivityTask activityTask) throws StateException {
        boolean startDepResult = progressState.addStartTimeDependency(activityTask.progressState);
        if(startDepResult){
            boolean endDepResult = progressState.addEndTimeDependency(activityTask.progressState);
            if(endDepResult){
                return true;
            }
        }
        return false;
    }



    public boolean removeSubActivityTask(ActivityTask activityTask){
        boolean startDepResult = progressState.forceRemoveStartTimeDependency(activityTask.progressState);
        if(startDepResult){
            boolean endDepResult = progressState.forceRemoveEndTimeDependency(activityTask.progressState);
            if(endDepResult){
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean removeDependency(ActivityTask activityTask) throws StateException {
        throw new NotSupportedDependency("Action not supported ");
    }



    @Override
    public boolean progressIfCompleted(ActivityTask activityTask) throws NotSupportedDependency {
        throw new NotSupportedDependency("Parent Activity Task can depend only on sub activitys");
    }



    @Override
    public boolean completedIfCompleted(ActivityTask activityTask) throws NotSupportedDependency {
        throw new NotSupportedDependency("Parent Activity Task can depend only on sub activitys");
    }



    @Override
    public boolean progressIfProgress(ActivityTask activityTask) throws NotSupportedDependency {
        throw new NotSupportedDependency("Parent Activity Task can depend only on sub activitys");
    }

}
