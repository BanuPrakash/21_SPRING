package com.adobe.rentalapp.api;

import com.adobe.rentalapp.entity.Vehicle;
import com.adobe.rentalapp.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final RentalService service;

    @GetMapping()
    public   List<Vehicle> getVehicles() {
        return service.getVehicles();
    }

    @PostMapping
    public  Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return service.addVehicle(vehicle);
    }
}
