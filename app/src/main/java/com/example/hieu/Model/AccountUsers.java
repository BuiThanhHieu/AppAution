package com.example.hieu.Model;

import java.util.HashMap;
import java.util.Map;

public class AccountUsers {
   private String Name,Address,Emai,Phone;
    public Map<String,Boolean> stars =new HashMap<>();

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getEmai() {
        return Emai;
    }

    public void setEmai(String emai) {
        this.Emai = emai;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Address", Address);
        result.put("Email", Emai);
        result.put("Phone", Phone);

        return result;
    }
    public AccountUsers(){

    }
}
