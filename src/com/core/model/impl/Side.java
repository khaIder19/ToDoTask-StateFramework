package com.core.model.impl;

public enum Side {
    START,END;

    public static Side valueOf(int ordinal){
        Side side = null;
        switch (ordinal){
            case 0:
                side = START;
                break;
            case 1:
                side = END;
                break;
            default:
                break;
        }
        return side;
    }

    public static int getSideOrdinal(Side side){
        switch (side){
            case START:
                return -1;
            case END:
                return  1;
        }
        return 0;
    }
}
