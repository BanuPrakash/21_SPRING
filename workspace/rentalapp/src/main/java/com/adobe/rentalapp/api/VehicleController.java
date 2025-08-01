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

    // Query Parameter [ ? ]
    // GET http://localhost:8080/api/vehicles
    // GET http://localhost:8080/api/vehicles?fuelType=PETROL
    @GetMapping()
    public   List<Vehicle> getVehicles(@RequestParam( name = "fuelType", required = false) String type) {
        if(type == null) {
            return service.getVehicles();
        } else  {
            return service.getByType(type);
        }
    }

    // Using Path Parameter [/]
    // GET http://localhost:8080/api/vehicles/GA05A1411
    @GetMapping("/{no}")
    public Vehicle getByRegistrationNumber(@PathVariable("no") String regNo) {
        return service.getVehicleByRegNo(regNo);
    }

    @PostMapping
    public  Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return service.addVehicle(vehicle);
    }

    // PATCH http://localhost:8080/api/vehicles/DH02AE1241?cost=5100
    @PatchMapping("api/vehicles/{no}")
    public Vehicle modifyCostOfRentForVehicle(@PathVariable("no") String regNo, @RequestParam("cost") double cost) {
        return service.updateCostOfRental(regNo, cost);
    }

    // PUT http://localhost:8080/api/vehicles/DH02AE1241
    // ContentType: application/json
    // Accept: application/json

    /*
        {
            costPerDay: 4250
        }
     */
//    @PutMapping("api/vehicles/{no}")
//    public Vehicle modifyCostOfRentForVehiclePut(@PathVariable("no") String regNo, @RequestBody Vehicle v) {
//        return service.updateCostOfRental(regNo, v.getCostPerDay());
//    }
}
