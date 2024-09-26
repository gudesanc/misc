package org.gds.poc.orch.manager.library;

public enum BusinessState  {
    RUNNING(false), COMPLETED(true), FAILED(true), PARTIALLY_FAILED(true); //quest'ultimo solo per gli "aggregatori"
    private final boolean finalState;

    BusinessState(boolean finalState) {
        this.finalState = finalState;
    }

    public boolean isFinalState(){
        return finalState;
    }
}
