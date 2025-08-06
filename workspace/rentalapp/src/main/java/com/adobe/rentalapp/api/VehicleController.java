package com.adobe.rentalapp.api;

import com.adobe.rentalapp.aop.Tx;
import com.adobe.rentalapp.entity.Vehicle;
import com.adobe.rentalapp.service.EntityNotFoundException;
import com.adobe.rentalapp.service.RentalService;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import  static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import  static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import  static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;

@RestController
@RequestMapping("api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle API", description = "Vehicles API Service")
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

    @GetMapping("/hateos/{no}")
    public EntityModel<Vehicle> getByRegistrationNumberLinks(@PathVariable("no") String regNo) throws EntityNotFoundException {
        Vehicle vehicle =  service.getVehicleByRegNo(regNo);
        EntityModel<Vehicle> entityModel = EntityModel.of(vehicle,
                linkTo(methodOn(VehicleController.class)
                        .getByRegistrationNumberLinks(regNo)).withSelfRel()
                        .andAffordance(afford(methodOn(VehicleController.class).modifyCostOfRentForVehicle(regNo, 0)))
                        .andAffordance(afford(methodOn(VehicleController.class).addVehicle(null))),
                linkTo(methodOn(VehicleController.class)
                        .getVehicles(null)).withRel("vehicles"));
        return  entityModel;
    }


    // Using Path Parameter [/]
    // GET http://localhost:8080/api/vehicles/GA05A1411

    @Operation(
            description = "Service that return a Vehicle",
            summary = "This service returns a Vehicle by Registration Number",
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Vehicle.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle  Not found", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{no}")
    public Vehicle getByRegistrationNumber(@PathVariable("no") String regNo) throws EntityNotFoundException {
        return service.getVehicleByRegNo(regNo);
    }


    // Using Cache Manager
    //GET http://localhost:8080/api/vehicles/cache/GA05A1411
    // SPeL
    @Cacheable(value = "vehiclesCache", key = "#regNo")
    @GetMapping("/cache/{no}")
    public Vehicle getByRegistrationNumberCache(@PathVariable("no") String regNo) throws EntityNotFoundException {
        System.out.println("Cache Miss!!!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return service.getVehicleByRegNo(regNo);
    }


    @GetMapping("/etag/{no}")
    public ResponseEntity<Vehicle> getByRegistrationNumberETag(@PathVariable("no") String regNo) throws EntityNotFoundException {
        Vehicle vehicle = service.getVehicleByRegNo(regNo);
        return ResponseEntity.ok().eTag(String.valueOf(vehicle.hashCode())).body(vehicle);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public  Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return service.addVehicle(vehicle);
    }

    // PATCH http://localhost:8080/api/vehicles/DH02AE1241?cost=5100
    @PatchMapping("/{no}")
    @CachePut(value = "vehiclesCache", key = "#regNo")
    public Vehicle modifyCostOfRentForVehicle(@PathVariable("no") String regNo, @RequestParam("cost") double cost) throws EntityNotFoundException {
        return service.updateCostOfRental(regNo, cost);
    }

    @Hidden
    @CacheEvict(value = "vehiclesCache", key = "#regNo")
    @DeleteMapping("/{no}")
    public String deleteVehicle(@PathVariable("no") String regNo) {
        return "Deleted!!!";
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
