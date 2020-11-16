package com.core.model.impl;

import com.core.model.api.BoundedRange;
import com.core.model.api.AdjustableRange;
import com.core.model.TimeRange;
import com.core.util.Constraints;

/**
 * A Bound state extends the properties of a mutable state. A Bound State's sides (start ; end) are constrained to a certain interval of times,
 * for each side there is one or more constraint interval (called "constraint range/s") in which the side is restricted in.
 *<p></p>
 * In order to make constrained a Bound State to a given intervals, the Bound State allows to set an implementation of "Bounded Range"
 * which is given the responsibility to provide them.
 *<p></p>
 * Bound State side's constraints imply consequently the state condition value to be constrained, in fact the start side constraint range will determine
 * when the state can become "active" and the end side constraint range will determine when the state condition can left the "active" condition.
 */
public abstract class BoundState extends MutableState implements AdjustableRange{

    private BoundedRange boundedRange;

    private boolean validationFlag = true;

    protected BoundState(){
    }


    /**
     *  Sets the state range with the given side parameters (start ; end).
     *<p></p>
     *  This operation will be subject to validation in order to make sure the given parameters will not end in an "out of range".
     * @param start The start parameter value to set.
     * @param end The end parameter value to set.
     * @return true if the state range is changed successfully ; false if the given parameters result in an "out of range" , or any other reason.
     */
    @Override
    public boolean setTime(long start, long end) {
        if(validationFlag) {
            if (validateConstraint(start, end)) {
                return super.setTime(start, end);
            }else {
                return false;
            }
        }else {
            return super.setTime(start,end);
        }
    }


    /**
     * The validation flag is used to make a distinction between a range set by the user or a value set by the internal implementation.
     *<p></p>
     * When the flag is activated it will drop the validations of the parameters given when setting a range.
     * @param value
     */
    protected void setValidationFlag(boolean value){
        validationFlag = value;
    }


    public BoundState(TimeRange range, boolean isUndefined){
        super(range,isUndefined);
        this.boundedRange = new BoundedRange() {
            @Override
            public TimeRange[] getStartSideValidationRanges() {
                return new TimeRange[]{TimeRange.UNDEF_TIME_RANGE};
            }

            @Override
            public TimeRange[] getEndSideValidationRanges() {
                return new TimeRange[]{TimeRange.UNDEF_TIME_RANGE};
            }
        };
    }

    /**
     * Tests if the given parameters will result in an "out of range" of one of the state sides.
     * @param start start of the range to evaluate.
     * @param end end of the range to evaluate.
     * @return true if the given parameters does not violate one of the state constraint range leading to an "out of range" ; false if not.
     */
    private boolean validateConstraint(long start,long end){
        return (
                Constraints.anyMatch(start,boundedRange.getStartSideValidationRanges()) &&
                Constraints.anyMatch(end,boundedRange.getEndSideValidationRanges())
        );
    }




    public abstract TimeRange adjust(TimeRange[] validationRange,TimeRange[] endValidationRange);


    /**
     * Returns the current BoundRange
     * @return
     */
    public BoundedRange getBoundedRange(){
        return boundedRange;
    }


    /**
     * Sets a constraint range of a specific side between (start ; end) with a given range parameter.
     *<p></p>
     * Setting a constraint range leads to a "resolution" process defined by underlying concrete implementations.
     *<p></p>
     * @param boundedRange
     * @return true if the constraint range has been successfully changed ; false if the "resolution" process fails, or any other reason.
     */
    @Override
    public boolean setValidationRange(BoundedRange boundedRange){
        boolean result = false;

        if(boundedRange == null){
            return false;
        }

        TimeRange toAdjustStateRange = adjust(boundedRange.getStartSideValidationRanges(),boundedRange.getEndSideValidationRanges());

        if(toAdjustStateRange != null){
            this.boundedRange = boundedRange;
            setValidationFlag(false);
            setTime(toAdjustStateRange.getStart(),toAdjustStateRange.getEnd());
            setValidationFlag(true);
            result = true;
        }else{
            result = false;
        }
            return result;
        }
}
