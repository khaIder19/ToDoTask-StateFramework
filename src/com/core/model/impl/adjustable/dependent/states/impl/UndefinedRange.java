package com.core.model.impl.adjustable.dependent.states.impl;

import com.core.model.impl.adjustable.adjuster.impl.FailAdjuster;
import com.core.model.impl.adjustable.dependent.exc.StateException;
import com.core.model.TimeRange;
import com.core.model.api.State;
import com.core.model.impl.adjustable.adjuster.impl.StaticAdjuster;
import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;
import com.core.model.impl.adjustable.dependent.exc.NotSupportedDependency;
import com.core.model.impl.adjustable.dependent.states.DependencyState;

/**
 * An Undefined Range has an infinite range with no start and no end.
 *<p></p>
 * Thi abstraction can be used to represent a single action in time.
 *<p></p>
 * NOTE : An Undefined Range can only have dependency with "undefined" states types.
 */
public class UndefinedRange extends DependencyState {

    private Long id;

    protected UndefinedRange(){
        super(new TimeRange(Long.MIN_VALUE,Long.MAX_VALUE),true,new FailAdjuster(null,null));
    }

    @Override
    public boolean addStartTimeDependency(State state) throws StateException {
        if(!isUndefined()){
            throw new NotSupportedDependency("This UndefinedRange is not considered 'undefined'");
        }
        return super.addStartTimeDependency(state);
    }

    @Override
    public boolean setStartTimeConstraint(Constraint startTimeConstraint) {
        if(!isUndefined()){
            return false;
        }
        return super.setStartTimeConstraint(startTimeConstraint);
    }

    @Override
    public boolean addEndTimeDependency(State state) throws StateException {
        throw new NotSupportedDependency("UndefinedRange does not support end time dependencies");
    }


    @Override
    public boolean setEndTimeConstraint(Constraint endTimeConstraint) {
        return false;
    }
}
