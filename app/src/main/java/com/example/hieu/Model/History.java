package com.example.hieu.Model;

public class History {
    private String idsp,timeend;
    private Double pricewin;
    private String state;
    private String imageURL;
    private String cart;

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdsp() {
        return idsp;
    }

    public void setIdsp(String idsp) {
        this.idsp = idsp;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public Double getPricewin() {
        return pricewin;
    }

    public void setPricewin(Double giawin) {
        this.pricewin = giawin;
    }
}
