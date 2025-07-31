package com.adobe.rentalapp.client;

import com.adobe.rentalapp.entity.Customer;
import com.adobe.rentalapp.entity.Rental;
import com.adobe.rentalapp.entity.Vehicle;
import com.adobe.rentalapp.service.RentalService;
import com.adobe.rentalapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalClient implements CommandLineRunner {
    private  final RentalService rentalService;
    private final DateUtil dateUtil;

    @Override
    public void run(String... args) throws Exception {
//        rent();
        returnVehicle();
    }

    private void returnVehicle() {
     String output = rentalService.returnVehicle(1, dateUtil.dateFromString("31-07-2025")) ;
     System.out.println(output);
    }

    private void rent() {
        Rental rental = new Rental();
        Vehicle vehicle = Vehicle.builder().registrationNumber("KA01EA621").build();
        Customer customer = Customer.builder().email("roger@adobe.com").build();
        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentedDate(dateUtil.dateFromString("30-07-2025"));
        rentalService.rentVehicle(rental);
    }
}
