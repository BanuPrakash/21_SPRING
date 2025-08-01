package com.adobe.rentalapp.api;

import com.adobe.rentalapp.entity.Rental;
import com.adobe.rentalapp.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rentals")
@RequiredArgsConstructor
public class RentalController {
    private  final RentalService service;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String rentVehicle(@RequestBody Rental rental) {
        return service.rentVehicle(rental);
    }

    @GetMapping()
    public List<Rental> getRentals() {
        return service.getRentals();
    }
}
