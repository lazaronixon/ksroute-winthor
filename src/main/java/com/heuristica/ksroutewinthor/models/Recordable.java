package com.heuristica.ksroutewinthor.models;


public interface Recordable {
    
    public String getRecordableId();
    public String getRecordableType();

    public void fetchRecord();    
}
