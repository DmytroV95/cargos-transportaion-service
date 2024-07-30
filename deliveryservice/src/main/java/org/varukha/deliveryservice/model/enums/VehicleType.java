package org.varukha.deliveryservice.model.enums;

import lombok.Getter;

/**
 * Enumeration representing types of vehicles.
 */
@Getter
public enum VehicleType {
    CAR("Car"),
    TRUCK("Truck"),
    TRAIN("Train"),
    PLANE("Plane"),
    SHIP("Ship"),
    HELICOPTER("Helicopter"),
    DRONE("Drone");

    private final String vehicleType;

    VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

}
