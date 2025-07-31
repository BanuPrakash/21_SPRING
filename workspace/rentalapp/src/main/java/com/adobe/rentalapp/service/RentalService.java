package com.adobe.rentalapp.service;

import com.adobe.rentalapp.entity.Vehicle;
import com.adobe.rentalapp.repo.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    // no need for @Autowired setter injection; it uses Constructor DI
    private  final VehicleRepo vehicleRepo;

    public long getVehicleCount() {
        return vehicleRepo.count();
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return vehicleRepo.findAll();
    }

    public Vehicle getVehicleByRegNo(String regNo) {
        Optional<Vehicle> opt = vehicleRepo.findById(regNo);
        if(opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
}
