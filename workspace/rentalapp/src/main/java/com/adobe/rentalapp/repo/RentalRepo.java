package com.adobe.rentalapp.repo;

import com.adobe.rentalapp.entity.Rental;
import com.adobe.rentalapp.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RentalRepo extends JpaRepository<Rental, Integer> {

    @Query(value = "SELECT * FROM vehicles v WHERE v.reg_no NOT IN " +
            "(SELECT r.vehicle_fk FROM rentals r WHERE :dt " +
            "BETWEEN r.rent_from AND r.rent_until )", nativeQuery = true)
//    @Query("select v from Rental  r inner  join r.vehicle v where  :dt " +
//        " BETWEEN  r.rentedDate and r.returnedDate")
    List<Vehicle> getAvailableVehicles(@Param("dt") Date date);
}
