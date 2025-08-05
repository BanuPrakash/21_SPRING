package com.adobe.datarestdemo.api;

import com.adobe.datarestdemo.entity.Vehicle;
import com.adobe.datarestdemo.repo.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@BasePathAwareController
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleRepo vehicleRepo;

    @RequestMapping(path = "vehicles", method = RequestMethod.GET)
    public @ResponseBody List<Vehicle> get() {
        List<Vehicle> vehicles = vehicleRepo.findAll();
        // can use WebMvcLinkBuilder to add custom links
        return Arrays.asList(Vehicle.builder().registrationNumber("A123").costPerDay(9000).build(),
                Vehicle.builder().registrationNumber("P3424").costPerDay(6342).build());
    }
}
