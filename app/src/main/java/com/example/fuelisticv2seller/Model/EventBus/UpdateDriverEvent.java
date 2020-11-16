package com.example.fuelisticv2seller.Model.EventBus;

import com.example.fuelisticv2seller.Model.DriverModel;

public class UpdateDriverEvent {
    private DriverModel driverModel;
    private boolean active;

    public UpdateDriverEvent(DriverModel driverModel, boolean active) {
        this.driverModel = driverModel;
        this.active = active;
    }

    public DriverModel getDriverModel() {
        return driverModel;
    }

    public void setDriverModel(DriverModel driverModel) {
        this.driverModel = driverModel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
