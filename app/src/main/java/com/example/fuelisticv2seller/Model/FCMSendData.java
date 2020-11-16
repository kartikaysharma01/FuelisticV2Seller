package com.example.fuelisticv2seller.Model;

import java.util.Map;

public class FCMSendData {
    private String to;
    private Map<String,String> data;


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public FCMSendData(String to, Map<String, String> data) {
        this.to = to;
        this.data = data;
    }
}
