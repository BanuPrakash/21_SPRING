package com.adobe.rentalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="vehicles")
public class Vehicle implements Serializable {

    @Id
    @Column(name="REG_NO", length = 50)
    private String registrationNumber;

    @Column(name="FUEL_TYPE", length = 50)
    private String fuelType;

    @Column(name="COST_PER_DAY")
    private double costPerDay;
}
