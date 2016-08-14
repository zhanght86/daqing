package com.midas.vo;

public class KeyValue {
    
    
    public KeyValue(int key,String value){
        this.value=value;
        this.key=key;
        
    }
    
    
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    private int key;
    private String value;
}