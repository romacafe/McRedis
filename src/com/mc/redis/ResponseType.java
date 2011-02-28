package com.mc.redis;

public enum ResponseType {
    SINGLE_LINE('+'),
    NUMBER(':'),
    BULK('$'),
    MULTI_BULK('*'),
    ;
    
    public final char indicator;
    
    private ResponseType (char indicator) {
        this.indicator = indicator;
    }
}
