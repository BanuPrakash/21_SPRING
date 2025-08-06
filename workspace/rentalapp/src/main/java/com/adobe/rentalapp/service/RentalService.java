package com.adobe.rentalapp.service;

import com.adobe.rentalapp.aop.Tx;
import com.adobe.rentalapp.entity.Rental;
import com.adobe.rentalapp.entity.Vehicle;
import com.adobe.rentalapp.repo.RentalRepo;
import com.adobe.rentalapp.repo.VehicleRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    // no need for @Autowired setter injection; it uses Constructor DI
    private  final VehicleRepo vehicleRepo;
    private final RentalRepo rentalRepo;

    // no need for @Transactional
    public Vehicle updateCost(String reg, double cost)  throws EntityNotFoundException{
        vehicleRepo.updateCostPerDay(reg, cost); // custom SQL
        return getVehicleByRegNo(reg);
    }

    public String rentVehicle(Rental rental) {
        rentalRepo.save(rental);
        return "Vehicle rented!!!";
    }

    public List<Rental> getRentals() {
        return rentalRepo.findAll();
    }


    @Transactional
    public String returnVehicle(int rentalId, Date returnDate) {
        Rental rental = rentalRepo.findById(rentalId).get();
        rental.setReturnedDate(returnDate); // DIRTY

        // PULL Vehicle complete details like cost
        Vehicle vehicle = vehicleRepo.findById(rental.getVehicle().getRegistrationNumber()).get();

        long daysRented = (returnDate.getTime() - rental.getRentedDate().getTime()) / (1000 * 60 * 60 * 24);
        if (daysRented <= 0) {
            throw new IllegalArgumentException("Invalid rental period. Returned date must be after rented date.");
        } else  if(daysRented == 0) {
            daysRented = 1;
        }

        rental.setAmount(daysRented * vehicle.getCostPerDay()); // DIRTY CHECKING -- UPDATE SQL
        return "Vehicle returned!!!";
    }

    @Transactional
    public Vehicle updateCostOfRental(String regNo, double cost)  throws EntityNotFoundException {
        vehicleRepo.updateCostPerDay(regNo, cost);
        return getVehicleByRegNo(regNo);
    }
    public long getVehicleCount() {
        return vehicleRepo.count();
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Tx
    public List<Vehicle> getVehicles() {
        return vehicleRepo.findAll();
    }


    public List<Vehicle> getByType(String fuelType) {
        return vehicleRepo.findByFuelType(fuelType);
    }

    public Vehicle getVehicleByRegNo(String regNo) throws EntityNotFoundException {
        Optional<Vehicle> opt = vehicleRepo.findById(regNo);
        if(opt.isPresent()) {
            return opt.get();
        }
       throw new EntityNotFoundException("Vehicle with Registration Number :" + regNo + " doesn't exist!!!");
    }
}
