package com.core.model.impl.adjustable.dependent.constraint.impl;


import com.core.model.impl.adjustable.dependent.constraint.api.Constraint;

import java.io.Serializable;

/**
 * [For persistence purpouse]
 */
public abstract class ArgConstraint implements Constraint, Serializable {

    public ArgConstraint(){
    }

    public abstract void setArgs(Object[] args);

    public abstract Object[] getArgs();

}
