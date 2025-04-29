package com.pluralsight;

public class ResultHelper {
    private char function = '0';
    private boolean keepGoing = true;


    public ResultHelper(char function, boolean keepGoing) {
        this.function = function;
        this.keepGoing = keepGoing;
    }

    public char getFunction() {
        return function;
    }
    public boolean getKeepGoing() {
        return keepGoing;
    }

    public void setFunction(char function) {
        this.function = function;
    }
    public void setKeepGoing(boolean keepGoing) {
        this.keepGoing = keepGoing;
    }



}
