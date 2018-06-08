package com.example.hieu.Model;

import java.util.HashMap;
import java.util.Map;

public class SessionAution {
    private String key;
    private String keyss;
    private String keysp;
    private String namesp;
    private String username;
    private String datetime;
    private Double price;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyss() {
        return keyss;
    }

    public void setKeyss(String keyss) {
        this.keyss = keyss;
    }

    public String getKeysp() {
        return keysp;
    }

    public void setKeysp(String keysp) {
        this.keysp = keysp;
    }

    public String getNamesp() {
        return namesp;
    }

    public void setNamesp(String namesp) {
        this.namesp = namesp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idSanPham", keysp);
        result.put("pricewin", price);
        result.put("userwin", username);
        result.put("nameSP",namesp);
        return result;
    }
    public SessionAution(){

    }
}
