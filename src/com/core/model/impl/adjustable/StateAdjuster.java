package com.core.model.impl.adjustable;

import com.core.model.impl.adjustable.adjuster.api.RangeAdjuster;
import com.core.model.TimeRange;
import com.core.model.impl.BoundState;

/**
 * The State Adjuster extends the properties of Bound State,
 * by delegating to a Range Adjuster the function of producing a range that will be the result of a "resolution" process.
 */
public class StateAdjuster extends BoundState {

    private Long id;

    private RangeAdjuster adjuster;


    protected StateAdjuster(){
    }

    public StateAdjuster(TimeRange range, boolean isUndefined,RangeAdjuster adjuster) {
        super(range, isUndefined);
        this.adjuster = adjuster;
    }

    /**
     * Sets the current Range Adjuster with the given implementation parameter.
     * @param adjuster
     */
    public void setAdjuster(RangeAdjuster adjuster){
            this.adjuster = adjuster;
    }

    /**
     * Returns the current implementation of a Range Adjuster in use by this State Adjuster.
     * @return the Range Adjuster implementation.
     */
    public RangeAdjuster getAdjuster(){
        return this.adjuster;
    }


    @Override
    public TimeRange adjust(TimeRange[] startValidationRanges,TimeRange[] endValidationRanges){

            if(startValidationRanges == null && endValidationRanges == null){
                return null;
            }

            return adjuster.adjustTo(getRange(),startValidationRanges,endValidationRanges);
    }

}
