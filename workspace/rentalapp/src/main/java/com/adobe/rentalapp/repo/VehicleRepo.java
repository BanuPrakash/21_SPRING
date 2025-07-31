package com.adobe.rentalapp.repo;

import com.adobe.rentalapp.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

// Generate CRUD operations for Vehicle whose primary key is String type
// Spring Data JPA generates @Repository class for this interface
public interface VehicleRepo extends JpaRepository<Vehicle, String> {
}
