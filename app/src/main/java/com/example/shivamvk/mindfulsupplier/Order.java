package com.example.shivamvk.mindfulsupplier;

public class Order {
    private String supplier,loadingPoint,tripDestination,truckType,materialType,loadingDate,remarks,completed,orderby;

    public Order(){

    }

    public Order(String supplier, String loadingPoint, String tripDestination, String truckType, String materialType, String loadingDate, String remarks, String completed, String orderby) {
        this.loadingPoint = loadingPoint;
        this.tripDestination = tripDestination;
        this.truckType = truckType;
        this.materialType = materialType;
        this.loadingDate = loadingDate;
        this.remarks = remarks;
        this.completed = completed;
        this.orderby = orderby;
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getLoadingPoint() {
        return loadingPoint;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public String getTruckType() {
        return truckType;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCompleted() {
        return completed;
    }

    public String getOrderby() {
        return orderby;
    }
}
