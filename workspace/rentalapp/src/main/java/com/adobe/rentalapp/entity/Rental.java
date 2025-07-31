package com.adobe.rentalapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rental_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="customer_fk")
    private Customer customer; // who is renting

    @ManyToOne
    @JoinColumn(name="vehicle_fk")
    private Vehicle vehicle; // which vehicle is rented

    @Temporal(TemporalType.DATE)
    @Column(name="rent_from")
    private Date rentedDate;

//    @Temporal(TemporalType.TIMESTAMP)
    @Temporal(TemporalType.DATE)
    @Column(name="rent_until")
    private Date returnedDate;

    private double amount;
}
