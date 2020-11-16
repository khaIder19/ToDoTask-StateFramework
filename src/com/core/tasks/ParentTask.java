package com.core.tasks;

import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.api.StateObject;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;

public class ParentTask extends Task{

    protected ParentTask(){

    }

    public ParentTask(long start) {
        super(start);
    }



    public boolean addSubTask(Task task) throws StateException {
        return super.completedIfCompleted(task);
    }



    public boolean removeSubTask(Task task) throws StateException {
        return removeDependency(task);
    }



    @Override
    public boolean completedIfCompleted(StateObject stateObject) throws NotSupportedDependency {
        throw new NotSupportedDependency("Parent Task can depend only on Sub Tasks");
    }

}
