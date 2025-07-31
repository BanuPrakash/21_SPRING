package com.adobe.rentalapp.client;

import com.adobe.rentalapp.entity.Vehicle;
import com.adobe.rentalapp.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VehicleClient implements CommandLineRunner {
    private final RentalService service;

    @Override
    public void run(String... args) throws Exception {
        addVehicles();
        listVehicles();
    }

    private void listVehicles() {
        List<Vehicle> vehicles = service.getVehicles();
        for(Vehicle vehicle :  vehicles) {
            System.out.println(vehicle); // toString()
        }
    }

    private void addVehicles() {
        if(service.getVehicleCount() == 0) {
            service.addVehicle(new Vehicle("DH02AE1241", "PETROL", 4500.00));
            service.addVehicle(Vehicle.builder()
                            .registrationNumber("KA01EA621")
                            .fuelType("DIESEL")
                            .costPerDay(3500.00)
                    .build());
            service.addVehicle(Vehicle.builder()
                    .registrationNumber("UP41A900")
                    .fuelType("ELECTRIC")
                    .costPerDay(5500.00)
                    .build());
        }
    }
}
