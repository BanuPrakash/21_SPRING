package com.adobe.datarestdemo.repo;

import com.adobe.datarestdemo.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Generate CRUD operations for Vehicle whose primary key is String type
// Spring Data JPA generates @Repository class for this interface
public interface VehicleRepo extends JpaRepository<Vehicle, String> {
    // JPQL and SQL Projections

    List<Vehicle> findByFuelType(String fuelType);

    List<Vehicle> findByCostPerDayBetween(double low, double high);

    // select * from vehicles where fuel_type = ? and cost_per_day < ?
    List<Vehicle> findByFuelTypeOrCostPerDayLessThan(String fuelType, double cost);

    @Query(value = "select reg_no, fuel_type from vehicles", nativeQuery = true)
    //@Query("select registrationNumber, fuelType from Vehicle ")
    List<Object[]> getVehicleInfo();

    @Modifying
    @Query("update Vehicle  set costPerDay = :amt where registrationNumber = :reg")
    void updateCostPerDay(@Param("reg") String regNo, @Param("amt") double cost);
}
