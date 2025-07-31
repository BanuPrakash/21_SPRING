package com.adobe.rentalapp.repo;

import com.adobe.rentalapp.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepo extends JpaRepository<Rental, Integer> {
}
