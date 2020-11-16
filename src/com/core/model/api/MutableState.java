package com.core.model.api;

/**
 * This interface represents a mutable state, a mutable state defines a function that assigns a value between true and false to underlying state condition.
 * <p></p>
 * A mutable state describes also its "extinct" property, a state is considered "extinct" when its condition is false and can not become true again.
 */
public interface MutableState extends State {

    /** Assigns a value between true and  false to the state condition.
     * The definition of State forces the invocation of this method at certain period depending on the state typology (Defined ; Undefined).
     * If the state is "Defined" this method should be called at exactly start-time setting the state condition property to true,
     * and at end-time setting it to false.
     * If the state is "Undefined" this method can be invoked after or exactly at state's start time, setting the condition property to true ; and then at exactly state's end time setting
     * the condition to false.
     * @param value value to be assigned to the condition property.
     * @return true if the state value is changed successfully; false if not.
     */
    public boolean setStateValue(boolean value);



    /**
     * This method defines when a State becomes extinct, that is : State "active" property can not be true a second time.
     * @return true if this state can not be "active" again.
     */
    public boolean isExtinct();
}
