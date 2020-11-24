package com.example.fuelisticv2seller.Model;

public class ShippingOrderModel {
    private String driverPhone, driverName, driverLicensePlate;
    private double currentLat, currentLng;
    private OrderModel orderModel;
    private boolean isStartTrip;

    private String sellerKey;

    public ShippingOrderModel() {
    }

    public String getSellerKey() {
        return sellerKey;
    }

    public void setSellerKey(String sellerKey) {
        this.sellerKey = sellerKey;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverLicensePlate() {
        return driverLicensePlate;
    }

    public void setDriverLicensePlate(String driverLicensePlate) {
        this.driverLicensePlate = driverLicensePlate;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(double currentLng) {
        this.currentLng = currentLng;
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    public boolean isStartTrip() {
        return isStartTrip;
    }

    public void setStartTrip(boolean startTrip) {
        isStartTrip = startTrip;
    }
}
